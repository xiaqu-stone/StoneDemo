package com.stone.templateapp

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.stone.commonutils.ActManager
import com.stone.commonutils.getProcessNameQ
import com.stone.log.Logs


class App : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
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
//        launcherLogcatViewer()

//        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
//            override fun onActivityPaused(activity: Activity?) {
//                Logs.d("App.onActivityPaused() called with: activity = [$activity]")
//            }
//
//            override fun onActivityResumed(activity: Activity?) {
//                Logs.d("App.onActivityResumed() called with: activity = [$activity]")
//            }
//
//            override fun onActivityStarted(activity: Activity?) {
//                Logs.d("App.onActivityStarted() called with: activity = [$activity]")
//            }
//
//            override fun onActivityDestroyed(activity: Activity?) {
//                Logs.d("App.onActivityDestroyed() called with: activity = [$activity]")
//            }
//
//            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
//                Logs.d("App.onActivitySaveInstanceState() called with: activity = [$activity], outState = [$outState]")
//            }
//
//            override fun onActivityStopped(activity: Activity?) {
//                Logs.d("App.onActivityStopped() called with: activity = [$activity]")
//            }
//
//            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
//                Logs.d("App.onActivityCreated() called with: activity = [$activity], savedInstanceState = [$savedInstanceState]")
//            }
//        })
    }


    companion object {
        private lateinit var app: App
        private const val TAG = "App"

        fun getApp(): App {
            return app
        }

    }
}
