package com.stone.templateapp.demo.socket

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import com.stone.templateapp.R
import kotlinx.android.synthetic.main.activity_tcpclient.*
import org.jetbrains.anko.act
import org.jetbrains.anko.doAsync
import java.io.*
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.*

class TCPClientActivity : AppCompatActivity() {
    companion object {
        private const val MSG_RECEIVE_NEW_MSG = 1
        private const val MSG_SOCKET_CONNECTED = 2
    }

    private var mClientSocket: Socket? = null
    private var mPrintWriter: PrintWriter? = null

    @SuppressLint("SetTextI18n")
    private val mHandler = Handler(Handler.Callback {
        when (it.what) {
            MSG_RECEIVE_NEW_MSG -> tvMsgContainer.text = tvMsgContainer.text.toString() + it.obj
            MSG_SOCKET_CONNECTED -> btnSend.isEnabled = true
        }
        true
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tcpclient)

        setTitle(R.string.title_socket_ipc)

        btnSend.setOnClickListener { clickBtnSend() }

        startService(Intent(act, TCPServerService::class.java))

        Thread(Runnable { connectTCPServer() }).start()
//        btnSend.postDelayed({  }, 3000)//延迟连接，等待服务端启动完成
    }

    private fun connectTCPServer() {

        var socket: Socket? = null
        while (socket == null) {
            //通过循环来处理服务端启动延迟的问题，当然这里的循环也是基于处理失败重连的机制上的
            //如果不做try catch 处理，当连接失败时会抛错
            try {
                socket = Socket("localhost", 8688)
                mClientSocket = socket
                mPrintWriter = PrintWriter(BufferedWriter(OutputStreamWriter(socket.getOutputStream())), true)
                mHandler.sendEmptyMessage(MSG_SOCKET_CONNECTED)
                println("connect server success")
            } catch (e: Exception) {
                SystemClock.sleep(1000)
                println("connect server failed, retry...")
            }
        }

        val br = BufferedReader(InputStreamReader(socket.getInputStream()))
        while (!isFinishing) {
            var msg: String? = null
            try {
                msg = br.readLine()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (msg != null) {
                val time = formatDateTime(System.currentTimeMillis())
                val showedMsg = "server $time: $msg\n"
                mHandler.obtainMessage(MSG_RECEIVE_NEW_MSG, showedMsg).sendToTarget()
            }
        }

        println("quit...")
        mPrintWriter?.close()
        br.close()
        socket.close()
    }

    private fun formatDateTime(time: Long): String {
        return SimpleDateFormat("（HH:mm:ss）", Locale.CHINA).format(Date(time))
    }

    @SuppressLint("SetTextI18n")
    private fun clickBtnSend() {
        val msg: String? = inputMsg.text.toString()
        if (!msg.isNullOrEmpty()) {
            doAsync { mPrintWriter?.println(msg) }
            inputMsg.setText("")
            val time = formatDateTime(System.currentTimeMillis())
            val showedMsg = "self $time : $msg\n"
            tvMsgContainer.text = tvMsgContainer.text.toString() + showedMsg
        }
    }

    override fun onDestroy() {
        mClientSocket?.shutdownInput()
        mClientSocket?.close()
        mHandler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}
