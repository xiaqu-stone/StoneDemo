package com.stone.templateapp.demo.https

import okhttp3.OkHttpClient
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext

/**
 * Created By: sqq
 * Created Time: 2019-05-16 14:44.
 */
class HttpDemo {


    fun httpUrlConn() {

        val url = URL("https://www.baidu.com")
        val conn = url.openConnection()
        if (conn is HttpsURLConnection) {

            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, arrayOf(SslUtils.UnsafeTrustManager()), null)
            conn.sslSocketFactory = sslContext.socketFactory
            conn.hostnameVerifier = SslUtils.UnsafeHostnameVerifier()
            val ins = conn.inputStream
            // 通过流写入数据
        }
    }

    fun okHttpUse() {
        OkHttpClient.Builder().sslSocketFactory(SslUtils.getSslContext().socketFactory, SslUtils.UnsafeTrustManager())
                .hostnameVerifier(SslUtils.UnsafeHostnameVerifier())
    }
}