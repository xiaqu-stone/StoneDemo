package com.stone.templateapp

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.CallLog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.stone.log.Logs
import com.stone.recyclerwrapper.QAdapter
import com.stone.templateapp.demo.binderpool.BinderPoolActivity
import com.stone.templateapp.demo.provider.ProviderActivity
import com.stone.templateapp.demo.socket.TCPClientActivity
import com.stone.templateapp.module.AndroidPathActivity
import com.stone.templateapp.module.DialogActivity
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

    private val datas = arrayListOf("Android Path", "Del Call Log", "扫描二维码", "Socket", "Binder Pool", "Content Provider", "Shell Exec", "Shell Exec2",
            "Dialog Activity")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = GridLayoutManager(ctx, 3)
        val adapter = QAdapter(ctx, R.layout.item_main, datas) { holder, itemData, pos ->
            holder.setText(R.id.button, itemData)
        }
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                val pos = parent.getChildAdapterPosition(view)
                if (pos % 3 != 2) {
                    outRect.right = 15
                }
                outRect.bottom = 15
            }
        })
        adapter.setOnItemClickListener { view, holder, itemData, position ->
            Logs.d("MainActivity.onCreate() called with: view = [$view], holder = [$holder], itemData = [$itemData], position = [$position]")
            when (position) {
                0 -> startActivity<AndroidPathActivity>()
                1 -> delCallLog()
                2 -> startScan()
                3 -> startActivity<TCPClientActivity>()
                4 -> startActivity<BinderPoolActivity>()
                5 -> startActivity<ProviderActivity>()
                6 -> toast(doShellExec())
                7 -> toast(doShellExec2())
                8 -> startActivity<DialogActivity>()
            }
        }

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


