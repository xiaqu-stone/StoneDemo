package com.stone.templateapp.module

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.stone.templateapp.R
import kotlinx.android.synthetic.main.activity_dialog.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton

class DialogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)
        button2.setOnClickListener {
            toast("Dialog Activity")
            alert {
                title = "测试生命周期";message = "测试Activity生命周期"
                yesButton { }
            }.show()
        }
    }
}
