package com.stone.templateapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.LinearLayout
import com.stone.commonutils.dp2px
import com.stone.log.Logs
import com.stone.qpermission.reqPermissions
import com.stone.templateapp.demo.dynamicproxy.DynamicProxy
import com.stone.templateapp.demo.dynamicproxy.RealSubject
import com.stone.templateapp.demo.dynamicproxy.Subject
import com.stone.templateapp.util.*
import com.yanzhenjie.permission.Permission
import kotlinx.android.synthetic.main.activity_test_button.*
import org.jetbrains.anko.doAsync

class TestButtonActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_button)


        // TODO: 2019-10-24  WRITE_CONTACTS 写入通讯录，Android 7.1 Red mi 4X, 写入权限报错
        addNewButton("获取通讯录——Phone") {
            reqPermissions(Permission.READ_CONTACTS) {
                Logs.d("onCreate: 拿到权限")
                doAsync { getContactPhoneColumn() }
            }
        }

        addNewButton("获取通讯录——Person") {
            reqPermissions(Permission.READ_CONTACTS) {
                doAsync { getContactPersonColumn() }
            }
        }

        addNewButton("获取通讯录——RawContact") {
            reqPermissions(Permission.READ_CONTACTS) {
                doAsync { getContactRawColumn() }
            }
        }


        addNewButton("获取联系人") {
            reqPermissions(Permission.READ_CONTACTS) {
                doAsync { getContacts() }
            }
        }


        addNewButton("插入联系人") {
            reqPermissions(Permission.WRITE_CONTACTS) {
                doAsync { insertContact("周八", "18500001111", "test@163.com") }
            }
        }

        addNewButton("批量插入联系人") {
            reqPermissions(Permission.WRITE_CONTACTS) {
                doAsync {
                    insertContacts("快牛1" to "123456", "快牛2" to "111111", "快牛3" to "22222")
                }
            }
        }

        addNewButton("发送邮件") {
            doAsync {
                val param = MailSender.MailParam()
                param.username = "sunqinqin@kuainiugroup.com"
                param.password = "Qsq123456qsQ"
                param.sendFromAddress = "sunqinqin@kuainiugroup.com"
                param.title = "这是标题"
                param.content = "这是测试邮件内容"
                param.sendToAddress = listOf("stonexiaqu@163.com")
                MailSender(param).sendTextMail()
            }

        }

        addNewButton("修改PIN DIAL") {
            val oldPin = "1233"
            val newPin = "1234"
            val ussdCode = "**04*$oldPin*$newPin*$newPin#"
//            val uri = Uri.parse("tel:$ussdCode")
            val uri = Uri.fromParts("tel", ussdCode, "#")
            Logs.i("onCreate: $uri")
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = uri
            startActivity(intent)
        }


        addNewButton("修改PIN CALL") {
            val oldPin = "1233"
            val newPin = "1234"
            val ussdCode = "**04*$oldPin*$newPin*$newPin#"
//            val uri = Uri.parse("tel:$ussdCode")
            val uri = Uri.fromParts("tel", ussdCode, "#")
            Logs.i("onCreate: $uri")
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = uri
            reqPermissions(Permission.CALL_PHONE) {
                startActivity(intent)
            }
        }

        addNewButton("动态代理") {
            val target = RealSubject()
            target.execute()
            DynamicProxy().newProxy<Subject>(target).execute()
        }
    }


    private fun addNewButton(text: String, listener: () -> Unit) {
        val button = Button(this)
        button.text = text
        button.isAllCaps = false
        button.setOnClickListener { listener() }
        container.addView(button, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp2px(40f)))
    }
}
