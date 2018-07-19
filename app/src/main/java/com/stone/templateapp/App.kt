package com.stone.templateapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.stone.log.Logs
import com.stone.templateapp.util.AppUtils


class App : Application(), Application.ActivityLifecycleCallbacks {

    private var curActivity: Activity? = null

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        app = this
    }

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
    }

    fun getCurAty(): Activity {
        return curActivity!!
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Logs.d("onActivityCreated, the activity is ${activity::class.java.canonicalName} @instance is $activity")
        //打开新的 使得在resume之前即可使用
        curActivity = activity
    }

    override fun onActivityStarted(activity: Activity) {
        Logs.v("onActivityStarted, the activity is ${activity::class.java.canonicalName} @instance is $activity")
    }

    override fun onActivityResumed(activity: Activity) {
        Logs.v("onActivityResumed, the activity is ${activity::class.java.canonicalName} @instance is ${activity.toString()}")
        //回退 栈中resume
        curActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {
        Logs.v("onActivityPaused, the activity is ${activity::class.java.canonicalName} @instance is $activity")
    }

    override fun onActivityStopped(activity: Activity) {
        Logs.v("onActivityStopped, the activity is ${activity::class.java.canonicalName} @instance is $activity")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
        Logs.v("onActivitySaveInstanceState, the activity is ${activity::class.java.canonicalName} @instance is $activity")
    }

    override fun onActivityDestroyed(activity: Activity) {
        Logs.d("onActivityDestroyed, the activity is ${activity::class.java.canonicalName} @instance is $activity")
        //判断当前任务栈是否为空，为空则将curActivity置null
        if (!AppUtils.isAppAlive(this, BuildConfig.APPLICATION_ID)) {
            curActivity = null
            AppUtils.exitProcess()//任务栈为0时，进程为及时关闭，手动关闭进程
        }

        //回退时，先回调下一个Activity的resume，然后回调当前Activity的destroy
    }

    override fun onTerminate() {
        super.onTerminate()
        Logs.i("onTerminate")
    }

    companion object {
        //todo 处理静态持有的问题，静态持有Activity会 break instant run
        @SuppressLint("StaticFieldLeak")
        private lateinit var app: App
        private const val TAG = "App"

        fun getApp(): App {
            return app
        }

    }
}
