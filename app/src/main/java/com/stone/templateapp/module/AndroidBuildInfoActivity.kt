package com.stone.templateapp.module

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.stone.recyclerwrapper.QAdapter
import com.stone.templateapp.R
import kotlinx.android.synthetic.main.activity_android_build_info.*
import org.jetbrains.anko.act

class AndroidBuildInfoActivity : AppCompatActivity() {
    private val dataList = listOf<Pair<String, String>>(
            "MODEL" to Build.MODEL,
            "BOARD" to Build.BOARD,
            "BOOTLOADER" to Build.BOOTLOADER,
            "BRAND" to Build.BRAND,
            "DEVICE" to Build.DEVICE,
            "DISPLAY" to Build.DISPLAY,
            "FINGERPRINT" to Build.FINGERPRINT,
            "HARDWARE" to Build.HARDWARE,
            "HOST" to Build.HOST,
            "ID" to Build.ID,
            "MANUFACTURER" to Build.MANUFACTURER,
            "PRODUCT" to Build.PRODUCT,
            "TAGS" to Build.TAGS,
            "TYPE" to Build.TYPE,
            "UNKNOWN" to Build.UNKNOWN,
            "USER" to Build.USER,
            "VERSION.RELEASE" to Build.VERSION.RELEASE,
            "VERSION.BASE_OS" to Build.VERSION.BASE_OS,
            "VERSION.CODENAME" to Build.VERSION.CODENAME,
            "VERSION.INCREMENTAL" to Build.VERSION.INCREMENTAL,
            "VERSION.SECURITY_PATCH" to Build.VERSION.SECURITY_PATCH,
            "VERSION.PREVIEW_SDK_INT" to Build.VERSION.PREVIEW_SDK_INT.toString(),
            "VERSION.SDK_INT" to Build.VERSION.SDK_INT.toString()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_android_build_info)

        recyclerView.layoutManager = LinearLayoutManager(act)
        val adapter = QAdapter(act, R.layout.item_build_info, dataList) { holder, itemData, pos ->
            holder.setText(R.id.tvTitle, itemData.first)
            holder.setText(R.id.tvContent, itemData.second)
        }
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.top = 10
            }
        })
    }
}
