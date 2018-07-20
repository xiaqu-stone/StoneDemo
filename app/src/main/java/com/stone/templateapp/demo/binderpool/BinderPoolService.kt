package com.stone.templateapp.demo.binderpool

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * Created By: sqq
 * Created Time: 18/7/19 下午7:41.
 */
class BinderPoolService : Service() {
    //Binder连接池 服务，统一向各个模块提供跨进程通信的Binder
    private val mBinderPool = BinderPool.BinderPoolImpl()

    override fun onBind(intent: Intent?): IBinder {
        println("on bind")
        return mBinderPool
    }
}