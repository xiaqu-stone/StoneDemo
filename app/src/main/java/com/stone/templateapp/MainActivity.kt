package com.stone.templateapp

import android.app.Instrumentation
import android.graphics.Rect
import android.hardware.SensorEventListener
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.webkit.WebView
import com.github.logviewer.LogcatActivity
import com.stone.commonutils.registerShakeListener
import com.stone.commonutils.unregisterShakeListener
import com.stone.log.Logs
import com.stone.mdlib.MDAlert
import com.stone.qpermission.QPermissonUtil
import com.stone.qpermission.reqPermissions
import com.stone.recyclerwrapper.QAdapter
import com.stone.templateapp.demo.binderpool.BinderPoolActivity
import com.stone.templateapp.demo.provider.ProviderActivity
import com.stone.templateapp.demo.proxy.CglibProxyJavaFactory
import com.stone.templateapp.demo.proxy.CglibSubjectJava
import com.stone.templateapp.demo.proxy.InstrumentationProxyFactory
import com.stone.templateapp.demo.socket.TCPClientActivity
import com.stone.templateapp.module.*
import com.stone.templateapp.module.web.WebActivity
import com.yanzhenjie.permission.Permission
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.io.IOException
import java.io.InputStreamReader
import java.io.LineNumberReader

class MainActivity : BaseActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    private var mShakeListener: SensorEventListener? = null
    private val datas = arrayListOf(
            "Android Path", "Del Call Log", "扫描二维码", "Socket", "Binder Pool",
            "Content Provider", "Shell Exec", "Shell Exec2", "Dialog Activity", "TRule Activity",
            "Canvas Path", "Bezier Progress", "Node Select", "Build Info", "QHttpDemo",
            "Test Activity", "Web Demo", "DeviceInfoActivity", "Logcat", "QPermissions Kotlin",
            "QPermission Java", "TestButtonActivity", "Cglib Proxy Instrumentation","Cglib Proxy"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            val webView = WebView(applicationContext)
            Logs.i("onCreate: WebView UA: 【${webView.settings.userAgentString}】")
//            webView.customSetting()

//            webView.webChromeClient = MyChromeClient()
//            webView.webViewClient = MyWebViewClient()
//            webView.loadUrl("https://www.baidu.com")
        } catch (e: Exception) {
            e.printStackTrace()
        }

//        doAsync {

//        }

        mShakeListener = registerShakeListener {
            toast("摇一摇")
            Logs.d("onCreate: ${System.currentTimeMillis()}")
            MDAlert(this, "摇一摇").show()
        }

        recyclerView.layoutManager = GridLayoutManager(this, 3)
        val adapter = QAdapter(this, R.layout.item_main, datas) { holder, itemData, _ ->
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
                9 -> startActivity<TRuleActivity>()
                10 -> startActivity<CanvasPathActivity>()
                11 -> startActivity<BezierProgressActivity>()
                12 -> startActivity<StoneNodeSelectActivity>()
                13 -> startActivity<AndroidBuildInfoActivity>()
                14 -> startActivity<HttpDemoActivity>()
                15 -> startActivity<TestActivity>()
                16 -> startActivity<WebActivity>()
                17 -> startActivity<DeviceInfoActivity>()
                18 -> startActivity<LogcatActivity>()
                19 -> {
                    reqPermissions(Permission.WRITE_EXTERNAL_STORAGE) {
                        //                    reqPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                        toast("get permissions1")
                    }
                }
                20 -> {
                    QPermissonUtil.reqPermissons(this, QPermissonUtil.Callback {
                        toast("get permissions1")
                    }, Permission.WRITE_EXTERNAL_STORAGE)
                }
                21 -> startActivity<TestButtonActivity>()

                22 -> {
                    try {
                        val method = Class.forName("android.app.ActivityThread").getMethod("currentActivityThread")
                        method.isAccessible = true
                        val activityThread = method.invoke(null)

                        val field = activityThread.javaClass.getDeclaredField("mInstrumentation")
                        field.isAccessible = true
//                        val mInstrum = activityThread.javaClass.getMethod("getInstrumentation").invoke(null)
                        val mInstrumentation = field.get(activityThread)
                        val factory = InstrumentationProxyFactory(mInstrumentation)

                        val instrumentationProxy = factory.getProxyInstance<Instrumentation>()

                        field.set(activityThread, instrumentationProxy)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                23 -> {
                    val target = CglibSubjectJava()
                    val factory = CglibProxyJavaFactory(target)
                    println("target:${target.javaClass}")
                    val proxy = factory.getProxyInstance<CglibSubjectJava>()
                    println("proxy:${proxy.javaClass}")
                    proxy.print()
                    println("=================")
                    proxy.input("【test cglib proxy】")

                }
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
//        requestPermission(object : PermissionCallback {
//            override fun onSuccess() {
//                Logs.i("startScanstartScanstartScanstartScan")
//                startActivity<ZxingScannerActivity>()
//            }
//        }, Permission.CAMERA)
    }

    override fun onDestroy() {
        Logs.i("MainActivity1")
        super.onDestroy()
        unregisterShakeListener(mShakeListener)
        Logs.i("MainActivity2")
    }

    private fun delCallLog() {
        Logs.i("del call log ")

//        requestPermission(object : PermissionCallback {
//            @SuppressLint("MissingPermission")
//            override fun onSuccess() {
//                contentResolver.delete(CallLog.Calls.CONTENT_URI, CallLog.Calls.NUMBER + "=?", arrayOf("13589686666"))
//            }
//        }, Permission.WRITE_CALL_LOG, Permission.READ_CALL_LOG)
    }

    private fun requestPermission(callback: PermissionCallback, vararg permissions: String) {
//        AndPermission.with(this).runtime()
//                .permission(permissions)
//                .onGranted {
//                    Logs.i("已获取权限：${Permission.transformText(this, it)}")
//                    callback.onSuccess()
//                }
//                .start()
    }

    interface PermissionCallback {
        fun onSuccess()
    }
}


