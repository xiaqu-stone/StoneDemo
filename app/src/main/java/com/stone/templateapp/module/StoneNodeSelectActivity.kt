package com.stone.templateapp.module

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.stone.log.Logs
import com.stone.templateapp.R
import kotlinx.android.synthetic.main.activity_stone_node_select.*
import org.jetbrains.anko.toast

class StoneNodeSelectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stone_node_select)

        mTestView.mDataList = arrayListOf("7天", "14天", "30天", "60天")
        mTestView.mChangeListener = { position, msg ->
            Logs.d("CanvasPathActivity.onCreate() called with: $position,$msg")
            toast(msg)
        }
    }
}
