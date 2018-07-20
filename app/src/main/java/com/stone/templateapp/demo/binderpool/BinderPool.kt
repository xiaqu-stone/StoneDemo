package com.stone.templateapp.demo.binderpool

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import com.stone.log.Logs
import java.util.concurrent.CountDownLatch

/**
 * Created By: sqq
 * Created Time: 18/7/19 下午8:00.
 */
class BinderPool private constructor(context: Context) {
    private val ctx: Context = context.applicationContext

    private lateinit var mConnectBinderPoolCountDownLatch: CountDownLatch
    private var mBinderPool: IBinderPool? = null

    companion object {
        private const val TAG = "BinderPool"
        const val BINDER_COMPUTE = 0
        const val BINDER_SECURITY_CENTER = 1

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var sInstance: BinderPool? = null

        /**
         * 懒汉式单例来处理BinderPool的对象获取
         */
        fun getInstance(context: Context): BinderPool {
            if (sInstance == null) {
                synchronized(BinderPool::class.java) {
                    if (sInstance == null) {
                        sInstance = BinderPool(context)
                    }
                }
            }
            return sInstance!!
        }
    }

    private val mBinderPoolDeathRecipient = object : IBinder.DeathRecipient {
        override fun binderDied() {
            Logs.w(TAG, "binder died.")
            mBinderPool?.asBinder()?.unlinkToDeath(this, 0)
            mBinderPool = null
            connectBinderPoolService()
        }
    }

    private val mBinderPoolConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            //do nothing
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mBinderPool = IBinderPool.Stub.asInterface(service)
            try {
                //设置死亡代理
                mBinderPool!!.asBinder().linkToDeath(mBinderPoolDeathRecipient, 0)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
            //连接上服务之后，调用countDown，使得被阻塞的线程继续执行
            mConnectBinderPoolCountDownLatch.countDown()
        }
    }

    init {
        //初始化中 绑定服务
        connectBinderPoolService()
    }


    @Synchronized
    private fun connectBinderPoolService() {
        //可以阻塞线程，在这里用来将异步操作 转换为同步操作
        mConnectBinderPoolCountDownLatch = CountDownLatch(1)
        //具体执行绑定服务
        ctx.bindService(Intent(ctx, BinderPoolService::class.java), mBinderPoolConnection, Context.BIND_AUTO_CREATE)
        try {
            //阻塞当前线程，等待 CountDown 为0。例如初始化中 count==1，那么只需要有一次的调用 countDown 方法，那么此处被阻塞的线程就会被唤醒 并继续执行
            mConnectBinderPoolCountDownLatch.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun queryBinder(binderCode: Int): IBinder? {
        return try {
            //开放给外部，获取各自模块的binder
            mBinderPool?.queryBinder(binderCode)
        } catch (e: Exception) {
            null
        }
    }


    class BinderPoolImpl : IBinderPool.Stub() {

        @Throws(RemoteException::class)
        override fun queryBinder(binderCode: Int): IBinder? {
            //Binder池中具体生成模块各自的Binder
            return when (binderCode) {
                BINDER_COMPUTE -> ComputeImpl()
                BINDER_SECURITY_CENTER -> SecurityCenterImpl()
                else -> null
            }
        }
    }
}
