package com.stone.templateapp

import android.app.Application
import android.content.Context
import com.stone.commonutils.ActManager
import com.stone.commonutils.getProcessNameQ
import com.stone.log.Logs


class App : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        Logs.init(Logs.VERBOSE, "StoneDemo")
        app = this
    }

    override fun onCreate() {
        super.onCreate()
//        Logs.i("onCreate: SDK API : ${Application.getProcessName()}")// added in API level 28
        if (getProcessNameQ() == packageName) {
            initMainProcess()
        }
    }

    private fun initMainProcess() {
        ActManager.registerActivityLifecycleCallbacks(this)
    }


    companion object {
        private lateinit var app: App
        private const val TAG = "App"

        fun getApp(): App {
            return app
        }

    }
}
