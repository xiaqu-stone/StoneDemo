package com.stone.templateapp.module

import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.util.JsonWriter
import android.view.View
import com.stone.commonutils.*
import com.stone.log.Logs
import com.stone.qpermission.reqPermissions
import com.stone.recyclerwrapper.CommonAdapter
import com.stone.recyclerwrapper.base.ViewHolder
import com.stone.templateapp.R
import com.yanzhenjie.permission.Permission
import kotlinx.android.synthetic.main.activity_test.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.security.PublicKey

class TestActivity : AppCompatActivity() {

    val datas = listOf(
            "测试Set Result", "删除短信 by id", "获取指定APP的签名SHA1", "android.os.Build 输出", "照片exif输出",
            "测试RSA加解密大文件数据", "AES 加解密", "RSA 对文件加密", "RSA 对文件解密", "AES 文件加解密", "测试外部获取Coll DeviceKey"
    )
    val keyLength = 2048
    val keyPair = RsaEncrypt.generateRSAKeyPair(keyLength)

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
                        recusiveDcim(File(dcim, "Camera"))
                    }
                }

                5 -> QUtil.logDuration { testRsa("RSA") }
                6 -> QUtil.logDuration { testRsa("AES") }
                7 -> {
//                    keyPair ?: return@setOnItemClickListener
                    reqPermissions(Permission.WRITE_EXTERNAL_STORAGE) {
                        val publicKey = RsaEncrypt.loadPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3ZgdNqhwmnkL7XJkhDf1Q0QbdXtMaAnQZCYHiD+VTFCuvHzrFzdNPAEmn7fmDm0QVqVig4Ll/SGpA+E1x/MYsrwVct86xjVV4/Me+wtVGi3ob8eO8l/pk40fkHzpwtOyZlu889QVp9uTmOXGRcEo/GWpOfaULC0VWZ2zxHh1uuauIgdgoNz9VCSphJkd3SSG/wWuAPK3NHdeIUtIz3I/m1R/F8OUItHNR/XRVV/uNeMgCns916aY7Q37vSmZmrcPF/vsidbgRdtByz653+DqXcvvS1mOQlxLcxFDwWhvIlbcAkXOJUUVFyTPLnFaxnK0SDzA/lzTuHKlJlPP+qz7fQIDAQAB")
                        QUtil.logDuration { encryptFile(publicKey) }
                    }
                }

                8 -> {
//                    keyPair ?: return@setOnItemClickListener
                    val dir = File(Environment.getExternalStorageDirectory(), "AAStoneDemo")
                    val file = File(dir, "encrypt_test.txt")
                    val privateKey = RsaEncrypt.loadPrivateKey("MIIEpAIBAAKCAQEA3ZgdNqhwmnkL7XJkhDf1Q0QbdXtMaAnQZCYHiD+VTFCuvHzrFzdNPAEmn7fmDm0QVqVig4Ll/SGpA+E1x/MYsrwVct86xjVV4/Me+wtVGi3ob8eO8l/pk40fkHzpwtOyZlu889QVp9uTmOXGRcEo/GWpOfaULC0VWZ2zxHh1uuauIgdgoNz9VCSphJkd3SSG/wWuAPK3NHdeIUtIz3I/m1R/F8OUItHNR/XRVV/uNeMgCns916aY7Q37vSmZmrcPF/vsidbgRdtByz653+DqXcvvS1mOQlxLcxFDwWhvIlbcAkXOJUUVFyTPLnFaxnK0SDzA/lzTuHKlJlPP+qz7fQIDAQABAoIBAQDEcrpTXniVOYKteqBJ5yH0BEkjma9e/HRWlLBQxa2h/lUqnMP9zPUXoR2QswOs/lthtTTkygMCqfigi+OV45mdush5EhWU6mu+riZlAOLtVo2dqxi7HvR/7rhw64yBQBQEUofs0bRxH2R2RB3GcTvYpnej/0nU8BLhY8GfB/u3OGwPZQMA6QlUIInmVdyGrlnalISKCILO1cag1ccXpw7iE0EmsgA44wvxC2EPpMZNTW8KMMSx2jdcyhmiDtXISgK79VZBU4ACm6vpwrZNBaAxUKP+kWSpF09vPjkdLAC8G9L4hLpE3ylxO3aXNn9xbe9oa7iRCKGXkpgNPxvsrwQBAoGBAO++mqoN5N+Zvv3SC9ekDhT1Ge79tTzCI/4L6ab3he2V6IYXIZ/dWb141MwWCDOe+9NCoJtcsV/oiY9bLM/G9HpAZqZyBWWGkIteni7s8mva4wgiYsNS1bmq4SxDIsoL0a/xN3b66vcfT8ftF/DiEuVQqZbWQ82GvWzmB2CgU+7BAoGBAOyedoUFuVgqsa1t/hP0aKmokZloYlh8i6USs1vQWkUvcozUoJWPiBQ7hj976TaAC5sAShlHBb/aBHlxKQuAQThEoJjG3CL3bZ2+Se8uqY7sua4uvSU77eI10wgFd4O9y2k0m0YLcxBFmzhGj7i13747FOV4MnZ7FDyR56/BOne9AoGBAIaFd7k1pJfGaAF564UrG2+s+8k6+IfIE3QH8Snrt21RcTBUKFzQUftbomOFdUKp0Xl/6ChkwW4kR5N1wFYxt5olKrP4RIjsEYzyIWYJNBlKr5sv2CTrBNtyQ3iXjxy5twW3Hr0XG+Jf4l827PkPkTS0pwPo5BCdqwpApCEWtSHBAoGADZjVirNXgT+C0kp9zTvAP++V3lDnRdpyU9wNPntslIkzdfBKAg4rba41+rPuJNma0R9lIqq4I9wtYaUlDKokFU2sRNs4jzHUtLnYmcv+pl3oFaBN8jPNabBaHh3iTkm7xKHnQlEqHYdCcitr2ttorg/LFZygmbpc4VYpECp0Z6kCgYBSsH4f/epmOPscDnBuAD9OWz3ghxkUWyGvqiRYd4aCL9W7WtBfzuStL0MwVAHBa6r7ZgH981iZAWofun8G/j1tP3ARkWySZ6e7Pfzb3Zi9DF0vKu974g/sBky6yOgtLkPLxMchBaw0Oz99iIpUIwFCxNFEtY15qP3eSv/SE2rsOA==")
                    QUtil.logDuration { RsaEncrypt.decryptBigData(file, File(dir, "decrypt_test.txt"), privateKey, keyLength) }
                }
                9 -> {
                    reqPermissions(Permission.WRITE_EXTERNAL_STORAGE) { testAesFile() }
                }
                10 -> QUtil.logDuration { getDeviceFingerprintFromStorage() }
            }
        }

        recyclerView.adapter = adapter
    }

    private fun getDeviceFingerprintFromStorage(): String {
        try {
            val dir = File(Environment.getExternalStorageDirectory(), ".coll")
            val deviceF = File(dir, ".device")

            if (deviceF.exists()) {
                Logs.d("getDeviceFingerprintFromStorage: ${deviceF.absolutePath}")
                FileInputStream(deviceF).use {
                    val deviceId = String(it.readBytes())
                    Logs.i("通过外部存储获取到DeviceKey:$deviceId")
                    return deviceId
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    private fun testAesFile() {
        val parent = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        if (!parent.exists()) parent.mkdirs()
        val originFile = File(parent, "testAes.json")
//        if (!originFile.exists()) originFile.createNewFile()
        if (!originFile.exists() || originFile.length() < 100) {

            val writer = JsonWriter(OutputStreamWriter(FileOutputStream(originFile)))
            writer.beginArray()
            for (i in 0 until 1000) {
                writer.beginObject()
                writer.name("id").value(i)
                writer.name("random").value(QUtil.getRandomString(16))
                writer.name("time").value(System.nanoTime())
                writer.endObject()
            }
            writer.endArray()
            writer.flush()
            writer.close()
        }
        val key = QUtil.getRandomString(16)
        Logs.i("testAes: $key")
        val encryptFile = File(parent, "aes_encrypt_data.json")
        AesEncrpt.aesEncrypt(key, originFile, encryptFile)
        Logs.d("testAes: AES加密完成")
        AesEncrpt.aesDecrypt(key, encryptFile, File(parent, "aes_decrypt_data.json"))
        Logs.d("testAes: AES 解密完成")
    }

    private fun encryptFile(keyPair: PublicKey) {
        val dir = File(Environment.getExternalStorageDirectory(), "AAStoneDemo")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val file = File(dir, "test.txt")
        if (!file.exists()) {
            file.createNewFile()
            val sb = StringBuilder()
            for (i in 0..1000) {
                sb.append(i).append("钦").append(",")
            }
            FileOutputStream(file).use { it.write(sb.toString().toByteArray()) }
            Logs.i("encryptFile: 创建文件成功 test.txt")
        }
        RsaEncrypt.encryptBigData(file, File(dir, "encrypt_test.txt"), keyPair, keyLength)
    }


    private fun testRsa(type: String) {
        /**
         * -----BEGIN PUBLIC KEY-----
        MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3ZgdNqhwmnkL7XJkhDf1
        Q0QbdXtMaAnQZCYHiD+VTFCuvHzrFzdNPAEmn7fmDm0QVqVig4Ll/SGpA+E1x/MY
        srwVct86xjVV4/Me+wtVGi3ob8eO8l/pk40fkHzpwtOyZlu889QVp9uTmOXGRcEo
        /GWpOfaULC0VWZ2zxHh1uuauIgdgoNz9VCSphJkd3SSG/wWuAPK3NHdeIUtIz3I/
        m1R/F8OUItHNR/XRVV/uNeMgCns916aY7Q37vSmZmrcPF/vsidbgRdtByz653+Dq
        XcvvS1mOQlxLcxFDwWhvIlbcAkXOJUUVFyTPLnFaxnK0SDzA/lzTuHKlJlPP+qz7
        fQIDAQAB
        -----END PUBLIC KEY-----
         */
//        val rsaKeyPair = RsaEncrpt.generateRSAKeyPair()


        val sb = StringBuilder()
        for (i in 0 until 200) {
            sb.append("大")
        }
        Logs.d("onCreate: the sb length: ${sb.length}")
        val bytes = sb.toString().toByteArray()
        Logs.d("onCreate: the bytes length ${bytes.size}")
        if (type == "AES") {
            val key = QUtil.getRandomString(16)
            val encrypt = AesEncrpt.aesEncrypt(key, sb.toString())
            Logs.d("testAes encrypt: $encrypt")
            val decrypt = AesEncrpt.aesDecrypt(key, encrypt)
            Logs.d("testAes decrypt: $decrypt")
        } else {
            val keyLength = 2048
            val keyPair = RsaEncrypt.generateRSAKeyPair(keyLength) ?: return
            val encryptBigData = RsaEncrypt.encryptBigData(bytes, keyPair.public, keyLength)
                    ?: return
            val enBase64 = encryptBigData.encodeBase64String()
            Logs.d("testRsa: encrypt size:${encryptBigData.size},length:${enBase64.length}, $enBase64")

//            val encode = Base64.encode(encryptBigData, Base64.NO_WRAP)

//            Logs.i("testRsa: encrypt size:${encode.size}, ${String(encode)}")
//            val testData = enBase64.decodeBase64().toByteArray() //testRsa: 1392
//            val testData = enBase64.toByteArray().decodeBase64String().toByteArray() //testRsa: 1371
//            val testData = enBase64.toByteArray().decodeBase64String().toByteArray() //231

//            val testData = Base64.decode(encode, Base64.NO_WRAP) //normal
            val testData = Base64.decode(enBase64, Base64.NO_WRAP)
            Logs.d("testRsa: ${testData.size}")
            val decryptBigData = RsaEncrypt.decryptBigData(testData, keyPair.private, keyLength)
                    ?: return
            val result = String(decryptBigData)
            Logs.d("testRsa: encrypt size:${decryptBigData.size},length:${result.length}, $result")
        }

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
//                isOutputed = true
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
