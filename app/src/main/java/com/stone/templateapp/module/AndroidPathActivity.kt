package com.stone.templateapp.module

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import com.stone.templateapp.R
import kotlinx.android.synthetic.main.activity_android_path.*

/**
 * Android中路径
 */
class AndroidPathActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_android_path)

        setTitle(R.string.title_android_path)

        initView()
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    private fun initView() {

        textView.text = "filesDir.absolutePath:\n${filesDir.absolutePath}\n\n" +
                "getExternalFilesDir:\n${getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).absolutePath}\n\n" +
                "cacheDir.absolutePath:\n${cacheDir.absolutePath}\n\n"

    }


}
