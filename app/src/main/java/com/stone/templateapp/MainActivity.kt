package com.stone.templateapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.CallLog
import com.stone.log.Logs
import com.stone.templateapp.demo.binderpool.BinderPoolActivity
import com.stone.templateapp.demo.provider.ProviderActivity
import com.stone.templateapp.demo.socket.TCPClientActivity
import com.stone.templateapp.module.AndroidPathActivity
import com.stone.templateapp.module.ZxingScannerActivity
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.io.IOException
import java.io.InputStreamReader
import java.io.LineNumberReader

class MainActivity : BaseActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnHello.setOnClickListener { startActivity<AndroidPathActivity>() }

        btnDel.setOnClickListener { delCallLog() }

        btnScan.setOnClickListener { startScan() }

        btnSocket.setOnClickListener { startActivity<TCPClientActivity>() }

        btnBinderPool.setOnClickListener { startActivity<BinderPoolActivity>() }

        btnProvider.setOnClickListener { startActivity<ProviderActivity>() }

        btnShellExec.setOnClickListener { toast(doShellExec()) }
        btnShellExec2.setOnClickListener { toast(doShellExec2()) }
    }

    private fun doShellExec(): String {

        val result = StringBuilder()
        try {
//            val exec = Runtime.getRuntime().exec("./dumpsys battery") //no
            val exec = Runtime.getRuntime().exec("/system/bin/dumpsys battery")//no
//            val exec = Runtime.getRuntime().exec("echo 我草你。。") // yes
//            val exec = Runtime.getRuntime().exec("adb shell dumpsys battery") // no
            LineNumberReader(InputStreamReader(exec.inputStream)).use {
                var read = it.readLine()
                while (read != null) {
                    result.append(read.trim())
                    read = it.readLine()
                }
            }
            Logs.i(TAG, result)
        } catch (e: IOException) {
            e.printStackTrace()
            result.append("the IOException occurred")
        }
        return result.toString()
    }

    override fun onRestart() {
        super.onRestart()
        Logs.d("MainActivity.onRestart() called with: ")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Logs.d("MainActivity.onRestoreInstanceState() called with: savedInstanceState = [$savedInstanceState]")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        Logs.d("MainActivity.onRestoreInstanceState() called with: savedInstanceState = [$savedInstanceState], persistentState = [$persistentState]")
    }

    private fun doShellExec2(): String {
        val result = StringBuilder()
        try {
            val exec = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address")
            LineNumberReader(InputStreamReader(exec.inputStream)).use {
                var read = it.readLine()
                while (read != null) {
                    result.append(read.trim())
                    read = it.readLine()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            result.append("the IOException occurred")
        }
        Logs.i(TAG, result)
        return result.toString()
    }

    private fun startScan() {
        requestPermission(object : PermissionCallback {
            override fun onSuccess() {
                Logs.i("startScanstartScanstartScanstartScan")
                startActivity<ZxingScannerActivity>()
            }
        }, Permission.CAMERA)
    }

    override fun onResume() {
        super.onResume()
//        AppUtil.isAppAlive(act, BuildConfig.APPLICATION_ID)

    }

    override fun onDestroy() {
        Logs.i("MainActivity1")
        super.onDestroy()

        Logs.i("MainActivity2")
    }


    fun delCallLog() {
        Logs.i("del call log ")
        requestPermission(object : PermissionCallback {
            @SuppressLint("MissingPermission")
            override fun onSuccess() {
                contentResolver.delete(CallLog.Calls.CONTENT_URI, CallLog.Calls.NUMBER + "=?", arrayOf("13589686666"))
            }
        }, Permission.WRITE_CALL_LOG, Permission.READ_CALL_LOG)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun requestPermission(callback: PermissionCallback, vararg permissions: String) {
        AndPermission.with(this).runtime()
                .permission(permissions)
                .onGranted {
                    Logs.i("已获取权限：${Permission.transformText(ctx, it)}")
                    callback.onSuccess()
                }
                .start()
    }

    interface PermissionCallback {
        fun onSuccess()
    }
}


