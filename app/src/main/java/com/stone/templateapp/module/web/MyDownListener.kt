package com.stone.templateapp.module.web

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.webkit.DownloadListener
import com.stone.commonutils.isValid
import org.jetbrains.anko.alert
import org.jetbrains.anko.cancelButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton

/**
 * Created By: sqq
 * Created Time: 17/7/6 上午11:42.
 */

class MyDownListener(private val activity: Activity) : DownloadListener {

    override fun onDownloadStart(url: String, userAgent: String, contentDisposition: String, mimetype: String, contentLength: Long) {
        if (!activity.isValid()) return
        //跳转至手机浏览器去下载
        activity.alert {
            message = "是否打开浏览器去下载？";isCancelable = false
            yesButton {
                val uri = Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                try {
                    activity.startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                    activity.toast("请安装浏览器后重试")
                }
            }
            cancelButton { it.dismiss() }
        }.show()
    }
}
