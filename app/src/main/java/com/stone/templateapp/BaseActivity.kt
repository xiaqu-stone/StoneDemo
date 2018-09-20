package com.stone.templateapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.stone.log.Logs

@SuppressLint("Registered")
open
/**
 * Created By: sqq
 * Created Time: 9/20/18 4:15 PM.
 */
class BaseActivity : AppCompatActivity() {

    companion object {

        private const val TAG = "BaseActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logs.v(TAG, "onCreate: this = $this")//当前Activity与 ContextWrapper
        Logs.v(TAG, "onCreate: applicationContext = $applicationContext")// 当前ApplicationContext
        Logs.v(TAG, "onCreate: application = $application")//当前Application
        Logs.v(TAG, "onCreate: App.getApp() = ${App.getApp()}")//自己封装获取的当前 Application
        Logs.v(TAG, "onCreate: baseContext = $baseContext")//当前 ContextImpl
        Logs.v(TAG, "onCreate: application.baseContext = ${application.baseContext}")//当前 APP 的 ContextImpl
    }
}