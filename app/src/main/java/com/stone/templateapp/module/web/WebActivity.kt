package com.stone.templateapp.module.web

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.stone.templateapp.R
import com.stone.templateapp.extensions.customSetting
import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        webView.customSetting()

        webView.webChromeClient = MyChromeClient()
        webView.webViewClient = MyWebViewClient()

        webView.loadUrl("http://localhost:8080")
    }
}
