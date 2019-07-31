package com.stone.templateapp.module

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.stone.recyclerwrapper.QAdapter
import com.stone.templateapp.R
import kotlinx.android.synthetic.main.activity_http_demo.*
import org.jetbrains.anko.act
import org.jetbrains.anko.ctx

class HttpDemoActivity : AppCompatActivity() {

    private var datas = arrayListOf("upload map", "upload body", "upload parts")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_http_demo)

        recyclerView.layoutManager = LinearLayoutManager(act, 1, false)

        val adapter = QAdapter(ctx, R.layout.item_main, datas) { holder, itemData, pos ->
            holder.setText(R.id.button, itemData)
        }

        recyclerView.adapter = adapter

        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.top = 15
            }
        })

//        adapter.setOnItemClickListener { view, holder, itemData, position ->
//            Logs.d("HttpDemoActivity.onCreate() called with: view = [$view], holder = [$holder], itemData = [$itemData], position = [$position]")
//            reqPermissions(Permission.WRITE_EXTERNAL_STORAGE) {
//                when (position) {
//
//                    0 -> {
//                        val file = File(Environment.getExternalStorageDirectory(), "tencent/MicroMsg/WeiXin/1233.jpg")
//                        QClient.url("$HOST/pb/file/upload").headers(getHeaders())
//                                .progress { bytesWritten, contentLength, done ->
//                                    Logs.d("HttpDemoActivity.onCreate() called with: bytesWritten = [$bytesWritten], contentLength = [$contentLength], done = [$done]")
//                                }
//                                .partMap(mapOf(
//                                        "desc" to RequestBody.create(MultipartBody.FORM, "11"),
//                                        "desc1" to RequestBody.create(MultipartBody.FORM, "1"),
//                                        "desc2" to RequestBody.create(MultipartBody.FORM, "2"),
//                                        "file\";filename=\"${file.name}" to RequestBody.create(MultipartBody.FORM, file)
//                                )).uploadMap(QObserver { toast("upload map success") })
//                    }
//                    1 -> {
//                        val file = File(Environment.getExternalStorageDirectory(), "tencent/MicroMsg/WeiXin/1233.jpg")
//                        val body = MultipartBody.Builder()
//                                .setType(MultipartBody.FORM)//必须加上 form-data 的MIME
//                                .addFormDataPart("desc", "1")
//                                .addFormDataPart("file", file.name, RequestBody.create(MultipartBody.FORM, file))
//                                .build()
//                        QClient.url("$HOST/pb/file/upload").headers(getHeaders())
//                                .progress { bytesWritten, contentLength, done ->
//                                    Logs.d("HttpDemoActivity.onCreate() called with: bytesWritten = [$bytesWritten], contentLength = [$contentLength], done = [$done]")
//                                }
//                                .body(body).uploadBody(QObserver { toast("upload body success") })
//                    }
//                    2 -> {
//                        val file = File(Environment.getExternalStorageDirectory(), "tencent/MicroMsg/WeiXin/1233.jpg")
//                        val file1 = File(Environment.getExternalStorageDirectory(), "tencent/MicroMsg/WeiXin/1234.mp4")
//                        // TODO: 2019/4/16 关于上传的进度处理
//                        val requestBody = RequestBody.create(MultipartBody.FORM, file)
//                        val parts = MultipartBody.Builder()
//                                .setType(MultipartBody.FORM)
//                                .addFormDataPart("desc", "1")
//                                .addFormDataPart("file", file.name, requestBody)
//                                .addFormDataPart("file1", file1.name, RequestBody.create(MultipartBody.FORM, file1))
//                                .build().parts()
//
//                        QClient.url("$HOST/pb/file/upload").headers(getHeaders())
//                                .filePart(parts.toTypedArray())
//                                .progress { bytesWritten, contentLength, done ->
//                                    Logs.d("HttpDemoActivity.onCreate() called with: bytesWritten = [$bytesWritten], contentLength = [$contentLength], done = [$done]")
//                                }
//                                .upload(QObserver<Result<String>> {
//                                    toast("upload parts success")
//                                }.finish { })
//                    }
//                }
//            }

//        }
    }

    fun getHeaders(): LinkedHashMap<String, String> {
        val headers = LinkedHashMap<String, String>()
        headers["token"] = "f15f25bcbc5f0fa4480ec968daa5b350316f4a29d91defba1f973616a259ff2d"
//        headers["token"] = "token_aaaaa"
        headers["deviceType"] = "app_android"
        headers["uuid"] = "864290040742589"
        headers["appId"] = "nkz_1.1.1_3"
        headers["weexVersion"] = "100"
        return headers
    }

    companion object {
        const val HOST = "http://kong-api-test.kuainiujinke.com/nkztesting"
    }
}
