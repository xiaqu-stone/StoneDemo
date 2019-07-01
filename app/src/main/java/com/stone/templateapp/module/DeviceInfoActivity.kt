package com.stone.templateapp.module

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.stone.commonutils.QUtil
import com.stone.recyclerwrapper.CommonAdapter
import com.stone.recyclerwrapper.base.ViewHolder
import com.stone.templateapp.R
import kotlinx.android.synthetic.main.activity_device_info.*

class DeviceInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_info)


        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = object : CommonAdapter<Pair<String, String>>(this, R.layout.item_device_info, QUtil.printOsBuild().toList()) {
            override fun bindData(holder: ViewHolder, itemData: Pair<String, String>, position: Int) {
                holder.setText(R.id.tvTitle, itemData.first)
                holder.setText(R.id.tvValue, itemData.second)
            }
        }

        recyclerView.adapter = adapter
    }
}
