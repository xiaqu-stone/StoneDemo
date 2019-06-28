package com.stone.templateapp.module

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.support.v4.content.ContextCompat
import com.stone.commonutils.ImageFilePath
import com.stone.log.Logs
import com.stone.templateapp.BaseActivity
import com.stone.templateapp.R
import kotlinx.android.synthetic.main.activity_android_path.*
import org.jetbrains.anko.act
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.yesButton

/**
 * Android中路径
 */
class AndroidPathActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_android_path)

        setTitle(R.string.title_android_path)

        initView()

        button.setOnClickListener {
            alert {
                title = "测试生命周期";message = "测试Activity生命周期"
                yesButton { }
            }.show()
//            finish()
        }
        btnForResult.setOnClickListener {
            startActivityForResult<TestActivity>(2)
//            selectPicFromGallery(1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Logs.d("AndroidPathActivity.onActivityResult() called with: requestCode = [$requestCode], resultCode = [$resultCode], data = [$data]")
        when (requestCode) {
            1 -> {
                val path = ImageFilePath.getPath(this, data?.data)
                Logs.i("onActivityResult: Image Path is $path")
            }
            2 -> {
                Logs.i("onActivityResult: ${data?.getStringExtra("result")}")
            }
        }
    }

    override fun onStart() {
        super.onStart()

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


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SetTextI18n")
    private fun initView() {
        textView.text = "filesDir:\n${filesDir.absolutePath}\n\n" +
                "getExternalFilesDir:\n${getExternalFilesDir(null)?.absolutePath}\n\n" +
                "getExternalFilesDir(Documents -19):\n${getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.absolutePath}\n\n" +
                "ContextCompat.getExternalFilesDirs[0]:\n${ContextCompat.getExternalFilesDirs(act, null)[0].absolutePath}\n\n" +
                "cacheDir:\n${cacheDir.absolutePath}\n\n" +
                "Environment.getExternalStorageDirectory():\n${Environment.getExternalStorageDirectory().absolutePath}\n\n" +
                "externalCacheDirs-19:\n${externalCacheDirs[0].absolutePath}\n\n" +
                "externalMediaDirs-21\n${externalMediaDirs[0].absolutePath}\n\n"

        Logs.i("initView: ${textView.text}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Logs.d("AndroidPathActivity.onDestroy() called with: ")
    }

}
