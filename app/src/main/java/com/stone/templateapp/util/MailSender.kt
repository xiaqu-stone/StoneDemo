package com.stone.templateapp.util

import com.stone.log.Logs
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

/**
 * Created By: sqq
 * Created Time: 2019-10-25 17:58.
 */
class MailSender {

    class MailParam {

        var username: String = ""
        var password: String = ""
        var sendFromAddress: String = ""
        var sendToAddress = listOf<String>()
        var sendCCAddress = listOf<String>()
        var sendBCCAddress = listOf<String>()

        var title = ""
        var content = ""
    }

    constructor(param: MailParam) {
        this.params = param
    }

    constructor()

    var params: MailParam? = null

    fun buildParams(param: MailParam): MailSender {
        this.params = param
        return this
    }

    fun sendTextMail() {
        val param: MailParam = if (params == null) {
            Logs.w("sendTextMail: 未初始化邮件参数")
            return
        } else {
            params!!
        }
        Logs.d("sendTextMail: start send email")
        val authenticator = MyAuthenticator(param.username, param.password)
        val config = getMailServerConfig()

        val session = Session.getDefaultInstance(config, authenticator)
        val message = MimeMessage(session)

        message.setFrom(InternetAddress(param.sendFromAddress))
        if (!param.sendToAddress.isNullOrEmpty()) {
            val toList = mutableListOf<InternetAddress>()
            param.sendToAddress.forEach { toList.add(InternetAddress(it)) }
            message.setRecipients(Message.RecipientType.TO, toList.toTypedArray())
        } else {
            Logs.w("sendTextMail: 缺少收件人")
            return
        }

        if (!param.sendCCAddress.isNullOrEmpty()) {
            val toList = mutableListOf<InternetAddress>()
            param.sendCCAddress.forEach { toList.add(InternetAddress(it)) }
            message.setRecipients(Message.RecipientType.CC, toList.toTypedArray())
        }
        if (!param.sendBCCAddress.isNullOrEmpty()) {
            val toList = mutableListOf<InternetAddress>()
            param.sendBCCAddress.forEach { toList.add(InternetAddress(it)) }
            message.setRecipients(Message.RecipientType.BCC, toList.toTypedArray())
        }

        message.subject = param.title

        message.setText(param.content)

        message.sentDate = Date()

        //详细输出log
        session.debug = true
        val transport = session.getTransport("smtp")
        // TODO: 2019-10-25 客户端专用密码
        transport.connect(param.sendFromAddress, "CtPWw7D8Mgz7sayW")
        transport.sendMessage(message, message.allRecipients)
//        Transport.send(message)
        transport.close()

        Logs.d("sendTextMail: 发送完成")
    }


    fun sendHtmlMail() {
        val authenticator = MyAuthenticator("账号", "密码")
        val config = getMailServerConfig()

        val session = Session.getDefaultInstance(config, authenticator)
        val message = MimeMessage(session)

        message.setFrom(InternetAddress("发件人"))
        message.setRecipients(Message.RecipientType.TO, arrayOf(InternetAddress("收件人地址")))
        message.setRecipients(Message.RecipientType.CC, arrayOf(InternetAddress("抄送人地址")))
        message.setRecipients(Message.RecipientType.BCC, arrayOf(InternetAddress("密送人地址")))

        message.subject = "标题"

        message.sentDate = Date()

        val multipart = MimeMultipart()

        val bodyPart = MimeBodyPart()
        bodyPart.setContent("这是HTML邮件内容", "text/html;charset=utf-8")

        multipart.addBodyPart(bodyPart)

        message.setContent(multipart)


        Transport.send(message)
    }

    /**
     * 这里以腾讯企业邮箱为例
     */
    fun getMailServerConfig(): Properties {
        val p = Properties()
        p["mail.smtp.host"] = "smtp.exmail.qq.com"
        p["mail.smtp.port"] = "465"
        p["mail.smtp.auth"] = "true"
        p["mail.smtp.ssl.enable"] = "true"
        return p
    }


    class MyAuthenticator(private val account: String, private val password: String) : Authenticator() {

        override fun getPasswordAuthentication(): PasswordAuthentication {

            return PasswordAuthentication(account, password)
        }
    }
}