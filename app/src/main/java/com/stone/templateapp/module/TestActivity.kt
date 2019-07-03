package com.stone.templateapp.module

import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.stone.commonutils.ExifUtil
import com.stone.commonutils.QUtil
import com.stone.commonutils.getSHA1Fingerprint
import com.stone.log.Logs
import com.stone.qpermission.reqPermissions
import com.stone.recyclerwrapper.CommonAdapter
import com.stone.recyclerwrapper.base.ViewHolder
import com.stone.templateapp.R
import com.yanzhenjie.permission.Permission
import kotlinx.android.synthetic.main.activity_test.*
import java.io.File

class TestActivity : AppCompatActivity() {

    val datas = listOf("测试Set Result", "删除短信 by id", "获取指定APP的签名SHA1", "android.os.Build 输出", "照片exif输出")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        recyclerView.layoutManager = GridLayoutManager(this, 2)

        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.set(10, 5, 10, 5)
            }
        })

        val adapter = object : CommonAdapter<String>(this, R.layout.item_btn, datas) {
            override fun bindData(holder: ViewHolder, itemData: String, position: Int) {
                holder.setText(R.id.btnAction, itemData)
            }
        }

        adapter.setOnItemClickListener { view, holder, itemData, position ->
            when (position) {
                0 -> {
                    val intent = Intent()
                    intent.putExtra("result", "this is the result from TestActivity !!!")
                    setResult(10, intent)
                    finish()
                }
                1 -> reqPermissions(Permission.SEND_SMS, Permission.READ_SMS) { deleteSmsById() }
                2 -> {
                    //B3:D1:CE:9C:2C:64:03:E9:68:53:24:BC:D5:7F:67:7B:13:A5:31:74  //"com.android.mms"
                    // C201F3432EB083EB09125F21E275569ED0F32B81
                    val sha1 = getSHA1Fingerprint(packageName).toUpperCase()
                    Logs.i("onCreate: the SHA1 : $sha1")
                }
                3 -> QUtil.printOsBuild()
                4 -> {
                    reqPermissions(Permission.READ_EXTERNAL_STORAGE) {
                        val dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                        recusiveDcim(dcim)
                    }
                }
            }
        }

        recyclerView.adapter = adapter
    }

    private fun recusiveDcim(dir: File) {
        Logs.i("recusiveDcim: ${dir.path}")
        var isOutputed = false
        val listFiles = dir.listFiles()
        if (listFiles.isNullOrEmpty()) return
        for (file in listFiles) {
            if (!isOutputed && file.isFile) {
                Logs.i("recusiveDcim: =============${dir.name}, ${file.path}=============")
                ExifUtil.printExifInfo(file.absolutePath)
                isOutputed = true
            } else if (file.isDirectory && !file.name.startsWith(".")) {
                recusiveDcim(file)
            }
        }
    }

    private fun deleteSmsById() {
        val cursor = contentResolver.query(Uri.parse("content://sms"), arrayOf("_id", "address", "thread_id"), null, null, null)
                ?: return
        cursor.moveToNext()
        val id = cursor.getString(cursor.getColumnIndex("_id"))
        val address = cursor.getString(cursor.getColumnIndex("address"))
        val threadId = cursor.getString(cursor.getColumnIndex("thread_id"))
        Logs.d("onCreate: id:$id, thread_id:$threadId, address: $address")
        val count = contentResolver.delete(Uri.parse("content://sms/$id"), null, null)
        Logs.d("deleteSmsById: count $count")
        cursor.close()
    }
}
