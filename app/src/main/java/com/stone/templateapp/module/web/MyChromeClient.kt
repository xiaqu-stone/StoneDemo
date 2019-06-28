package com.stone.templateapp.module.web

import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import com.stone.log.Logs
import com.stone.templateapp.extensions.setTitle

/**
 * Created By: sqq
 * Created Time: 17/12/7 下午6:43.
 */

class MyChromeClient(
        private val mProgress: View? = null
) : WebChromeClient() {
    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
        view?.setTitle(title)
    }

    override fun onProgressChanged(view: WebView, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        Logs.d(TAG, newProgress)
//        if (view.visibility != View.VISIBLE && newProgress == 100) {
//            view.visibility = View.VISIBLE
//        }
        if (mProgress != null) {
            when (mProgress) {
                is ProgressBar -> {
                    mProgress.progress = newProgress
                }
                else -> {
//                    do nothing
                }
            }
        }
    }

    override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
        Logs.d(TAG, "MyChromeClient.onJsAlert() called with: view = [$view], url = [$url], message = [$message], result = [$result]")
        return super.onJsAlert(view, url, message, result)
    }

    override fun onJsConfirm(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
        Logs.d(TAG, "MyChromeClient.onJsConfirm() called with: view = [$view], url = [$url], message = [$message], result = [$result]")
        return super.onJsConfirm(view, url, message, result)
    }

    override fun onJsPrompt(view: WebView?, url: String?, message: String?, defaultValue: String?, result: JsPromptResult?): Boolean {
        Logs.d(TAG, "MyChromeClient.onJsPrompt() called with: view = [$view], url = [$url], message = [$message], defaultValue = [$defaultValue], result = [$result]")
        return super.onJsPrompt(view, url, message, defaultValue, result)
    }

    override fun onConsoleMessage(message: String?, lineNumber: Int, sourceID: String?) {
        Logs.d(TAG, "MyChromeClient.onConsoleMessage() called with: message = [$message], lineNumber = [$lineNumber], sourceID = [$sourceID]")
        super.onConsoleMessage(message, lineNumber, sourceID)
    }

    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
        Logs.d(TAG, "MyChromeClient.onConsoleMessage() called with: consoleMessage = [$consoleMessage]")
        return super.onConsoleMessage(consoleMessage)
    }

    companion object {
        const val TAG = "MyChromeClient"
    }
}
