package com.stone.templateapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.CallLog
import android.support.v7.app.AppCompatActivity
import com.stone.log.Logs
import com.stone.templateapp.demo.binderpool.BinderPoolActivity
import com.stone.templateapp.demo.socket.TCPClientActivity
import com.stone.templateapp.module.AndroidPathActivity
import com.stone.templateapp.module.ZxingScannerActivity
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnHello.setOnClickListener { startActivity<AndroidPathActivity>() }

        btnDel.setOnClickListener { delCallLog() }

        btnScan.setOnClickListener { startScan() }

        btnSocket.setOnClickListener { startActivity<TCPClientActivity>() }

        btnBinderPool.setOnClickListener { startActivity<BinderPoolActivity>() }
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
