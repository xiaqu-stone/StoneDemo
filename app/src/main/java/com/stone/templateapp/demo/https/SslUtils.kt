package com.stone.templateapp.demo.https

import com.stone.log.Logs
import com.stone.templateapp.App
import okio.Buffer
import org.apache.http.conn.ssl.SSLSocketFactory
import java.io.BufferedInputStream
import java.io.InputStream
import java.security.KeyStore
import java.security.PublicKey
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.*

/**
 * Created By: sqq
 * Created Time: 2019-05-15 19:48.
 *
 * HTTPS 客户端校验服务器证书的逻辑。
 *
 * 目前还未整理 校验客户端证书的逻辑 KeyManager
 *
 */
object SslUtils {

    fun getSslContext(): SSLContext {
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf(UnsafeTrustManager()), null)
        return sslContext
    }

    /**
     * SSL证书的校验
     *
     * @param inputStream 预埋入APP中的服务器证书的流
     */
    fun getTrustManager(inputStream: InputStream): Array<out TrustManager>? {
        //以 X.509 格式获取证书
        val cf = CertificateFactory.getInstance("X.509")
        val cert = cf.generateCertificate(inputStream)
        Logs.d("getTrustManager: 证书公钥：${cert.publicKey}")

        //生成一个包含服务端证书的KeyStore
        val keyStoreType = KeyStore.getDefaultType()
        Logs.d("getTrustManager: keyStoreType: $keyStoreType")
        val keyStore = KeyStore.getInstance(keyStoreType)
        keyStore.load(null)
        keyStore.setCertificateEntry("cert", cert)

        //用包含服务端证书的Keystore生成一个TrustManager
        try {
            inputStream.close()
        } catch (e: Exception) {
        }
        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(keyStore)
        return trustManagerFactory.trustManagers
    }

    /**
     * 通过公钥的字符串生成证书
     *
     * 备注：当不在APP预埋证书文件时，可以拿到 公钥 信息，硬编码进代码中。
     *
     * 证书公钥的输出：
     *
     * keytool -printcert -rfc -file <证书 file path>
     *
     * 以 ---------BEIGIN CERTIFICATE----------- 开始
     * 以 ---------END CERTIFICATE-------------- 结束
     */
    fun getCertificate(publicKey: String): Certificate? {
        val certStream = Buffer().writeUtf8(publicKey).inputStream()
        //X509Certificate
        return CertificateFactory.getInstance("X.509").generateCertificate(certStream)
    }

    /**
     *
     */
    fun getTrustManagerPwd(inputStream: InputStream, password: String): Array<out TrustManager>? {
        val trustStore = KeyStore.getInstance("BKS")
        trustStore.load(inputStream, password.toCharArray())
        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(trustStore)
        return trustManagerFactory.trustManagers
    }


    /**
     * 弱校验证书（没有真正校验证书），不安全的方式。
     */
    class UnsafeTrustManager : X509TrustManager {
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            //用以校验客户端证书
        }

        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            //用以校验服务端证书
            SSLSocketFactory.STRICT_HOSTNAME_VERIFIER

        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }
    }
    //

    /**
     * 未校验服务器证书的域名是否相符
     *
     * @see SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER Deprecated
     */
    class UnsafeHostnameVerifier : HostnameVerifier {
        override fun verify(hostname: String?, session: SSLSession?): Boolean {
            //未校验服务器证书的域名是否相符
            return true
        }
    }

    /**
     * 对服务器证书域名进行强校验
     */
    class StrictHostnameVerifier : HostnameVerifier {
        override fun verify(hostname: String?, session: SSLSession?): Boolean {
            val hv = HttpsURLConnection.getDefaultHostnameVerifier()
            return hv.verify("*.stone.com", session)
        }
    }

    /**
     *
     * 真正实现 TrustManager#checkServerTrusted 的正确写法，真正对服务器的证书做了强校验
     *
     * 首先校验服务器证书的有效性，然后与本地预埋的证书作对比。
     *
     * 自定义的 TrustManager
     */
    class StrictTrustManager : X509TrustManager {
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            //校验客户端证书
        }

        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            //校验服务器证书
            if (chain.isNullOrEmpty()) throw IllegalArgumentException("Check Server x509Certificate is null or Empty")

            chain.forEach {
                //检查服务端签名是否有问题
                it.checkValidity()
                try {
                    //和APP预埋的证书做对比
                    it.verify(getPublicKey("    cert"))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }

    }

    /**
     * 根据文件名获取在assets目前下的证书的公钥（PublicKey）
     */
    fun getPublicKey(filename: String): PublicKey? {
        val certInput = BufferedInputStream(App.getApp().assets.open(filename))
        val certificate = CertificateFactory.getInstance("X.509").generateCertificate(certInput)
        return certificate.publicKey
    }

}