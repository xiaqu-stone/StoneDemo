package com.stone.templateapp.demo.binderpool

import android.os.Bundle
import android.os.RemoteException
import android.support.v7.app.AppCompatActivity
import com.stone.log.Logs
import com.stone.templateapp.R
import org.jetbrains.anko.act

class BinderPoolActivity : AppCompatActivity() {
    companion object {
        const val TAG = "BinderPoolActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_binder_pool)
        setTitle(R.string.title_binder_pool)
        //由于绑定服务时，做了阻塞操作，所以这里需要在线程中执行。
        Thread(Runnable { doWork() }).start()
    }

    /**
     * 模拟各模块的调用
     */
    private fun doWork() {
        val binderPool = BinderPool.getInstance(act)
        val securityBinder = binderPool.queryBinder(BinderPool.BINDER_SECURITY_CENTER)
        val mSecurityCenter = ISecurityCenter.Stub.asInterface(securityBinder)
        Logs.d(TAG, "visit ISecurityCenter")
        val msg = "helloWorld-安卓"
        println("content: $msg")
        try {
            val pwd = mSecurityCenter.encrypt(msg)
            println("encrypt: $pwd")
            println("decrypt: ${mSecurityCenter.decrypt(pwd)}")
        } catch (e: RemoteException) {
            e.printStackTrace()
        }

        Logs.d(TAG, "visit ICompute")
        val mComputeBinder = binderPool.queryBinder(BinderPool.BINDER_COMPUTE)
        val mCompute = ICompute.Stub.asInterface(mComputeBinder)
        try {
            println("3 + 5 = ${mCompute.add(3, 5)}")
        } catch (e: RemoteException) {
            e.printStackTrace()
        }

        Logs.d(TAG, "the work is finished")
    }
}
