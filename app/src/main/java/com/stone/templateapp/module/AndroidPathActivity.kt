package com.stone.templateapp.module

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import com.stone.log.Logs
import com.stone.templateapp.BaseActivity
import com.stone.templateapp.R
import kotlinx.android.synthetic.main.activity_android_path.*

/**
 * Android中路径
 */
class AndroidPathActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_android_path)

        setTitle(R.string.title_android_path)

        initView()


//        button.setOnClickListener {
//
//        }
    }

    override fun onStart() {
        super.onStart()
//        startActivity<TCPClientActivity>()
//        finish()
    }

    override fun onRestart() {
        super.onRestart()
        Logs.d("AndroidPathActivity.onRestart() called with: ")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Logs.d("MainActivity.onRestoreInstanceState() called with: savedInstanceState = [$savedInstanceState]")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        Logs.d("MainActivity.onRestoreInstanceState() called with: savedInstanceState = [$savedInstanceState], persistentState = [$persistentState]")
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        Logs.d("AndroidPathActivity.onSaveInstanceState() called with: outState = [$outState], outPersistentState = [$outPersistentState]")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Logs.d("AndroidPathActivity.onSaveInstanceState() called with: outState = [$outState]")
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    private fun initView() {

        textView.text = "filesDir.absolutePath:\n${filesDir.absolutePath}\n\n" +
                "getExternalFilesDir:\n${getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).absolutePath}\n\n" +
                "cacheDir.absolutePath:\n${cacheDir.absolutePath}\n\n"

    }

    override fun onDestroy() {
        super.onDestroy()
        Logs.d("AndroidPathActivity.onDestroy() called with: ")
    }

}
