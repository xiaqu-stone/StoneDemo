package com.stone.templateapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.LinearLayout
import com.stone.commonutils.dp2px
import com.stone.log.Logs
import com.stone.qpermission.reqPermissions
import com.stone.templateapp.util.getContactPersonColumn
import com.stone.templateapp.util.getContactPhoneColumn
import com.stone.templateapp.util.getContacts
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


        addNewButton("获取联系人") {
            reqPermissions(Permission.READ_CONTACTS) {
                doAsync { getContacts() }
            }
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
