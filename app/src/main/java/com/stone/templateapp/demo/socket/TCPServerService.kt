package com.stone.templateapp.demo.socket

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.io.*
import java.net.ServerSocket
import java.net.Socket
import java.util.*

/**
 * Created By: sqq
 * Created Time: 18/7/19 下午2:50.
 */
class TCPServerService : Service() {

    private var mIsServiceDestroyed = false

    private val mDefinedMessages = arrayOf("你好啊，洒洒水所", "今天上海听爱情", "我屮艸芔茻", "你这人真尼玛操蛋", "j今Tina踩了一坨屎")

    private val mTCPServerThread = Thread(Runnable {
        //监听本地8688 端口
        val serverSocket = ServerSocket(8688)
        //循环接收客户端的连接请求，用来支持多个客户端连接
        while (!mIsServiceDestroyed) {
            val client = serverSocket.accept()
            println("accept")
            //每接收到一个客户端的连接请求之后，开启一个线程用来处理客户端的消息
            Thread(Runnable { responseClient(client) }).start()
        }
    })

    override fun onCreate() {
        super.onCreate()
        mTCPServerThread.start()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        mIsServiceDestroyed = true
        super.onDestroy()
    }

    /**
     * 接收客户端的消息，并向客户端灰回复消息
     */
    private fun responseClient(client: Socket) {
        val reader = BufferedReader(InputStreamReader(client.getInputStream()))
        val writer = PrintWriter(BufferedWriter(OutputStreamWriter(client.getOutputStream())), true)
        writer.println("欢迎来到聊天室！")
        //循环接收当前线程对应的客户端发来消息，并回复
        while (!mIsServiceDestroyed) {
            val str: String? = reader.readLine()
            println("msg from client : $str")
            if (str == null) {
                break
            }
            val i = Random().nextInt(mDefinedMessages.size)
            writer.println(mDefinedMessages[i])
            println(mDefinedMessages[i])
        }
        //循环结束后，关闭操作
        println("client quit.")
        reader.close()
        writer.close()
        client.close()
    }
}