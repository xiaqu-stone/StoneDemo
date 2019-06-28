package com.stone.templateapp.demo.accessibility

import android.accessibilityservice.AccessibilityService
import android.annotation.TargetApi
import android.app.Notification
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.stone.log.Logs

/**
 * Created By: sqq
 * Created Time: 2019-06-21 11:06.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
class MyAccessibility : AccessibilityService() {
    override fun onInterrupt() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event ?: return
        val type = event.eventType
        Logs.d("onAccessibilityEvent: $type,,,,,,$event")
        when (type) {
            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                Logs.d("onAccessibilityEvent: TYPE_VIEW_CLICKED")
            }
            AccessibilityEvent.TYPE_VIEW_LONG_CLICKED -> {
                Logs.d("onAccessibilityEvent: TYPE_VIEW_LONG_CLICKED")
            }
//            AccessibilityEvent.CONTENT_CHANGE_TYPE_SUBTREE -> {}
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                Logs.d("onAccessibilityEvent: TYPE_WINDOW_STATE_CHANGED")

                val className = event.className.toString()
                Logs.d("onAccessibilityEvent: $className")
                if (className == "com.tencent.mm.ui.LauncherUI") {
                    getPacket()
                } else if (className == "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI") {
                    openPacket()
                } else {
                    Logs.i("onAccessibilityEvent: $className")
                }


            }
            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> {
                Logs.d("onAccessibilityEvent: TYPE_NOTIFICATION_STATE_CHANGED")

                val list = event.text
                if (list.isEmpty()) return
                list.forEach {
                    if (it.contains("[微信红包]")) {
                        val data = event.parcelableData
                        if (data != null && data is Notification) {
                            try {
                                data.contentIntent.send()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun openPacket() {
        Logs.d("openPacket: ")
        val info = rootInActiveWindow

        if (info != null) {
            recyclerOpenNode(info)
        }
    }

    fun recyclerOpenNode(info: AccessibilityNodeInfo) {
        if (info.childCount == 0) {
            Logs.d("openPacket: $info")
            if (info.isClickable) {
                // TODO: 2019-06-21
                Logs.i("recyclerOpenNode: click = true;;;; $info")
            }
        } else {
            Logs.d("recyclerOpenNode: $info")
            for (i in 0 until info.childCount) {
                val child = info.getChild(i) ?: continue
                recyclerOpenNode(child)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun getPacket() {
        Logs.d("getPacket: ")
        val rootNode = rootInActiveWindow ?: return

        val list = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ap9")
        if (list.isNullOrEmpty()) {
            Logs.i("getPacket: List 为kong")
//            recycleNode(rootNode)
        } else {
//            list.forEach {
//                Logs.d("getPacket: $it")
//
//            }
            Logs.i("getPacket: List 不为kong")
            list.forEach {
                val descList = it.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/aqk")
                if (descList.isNullOrEmpty()) {
                    Logs.i("getPacket: 有红包，马上去抢！！！")
                    it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                }else{
                    val text = descList[0].text
//                    Logs.d("getPacket: desc: $text")
                    if (text.isNullOrEmpty()) {
                        Logs.i("getPacket: 有红包，马上去抢！！！")
                        it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    } else {
                        if (text.contains("已领取") || text.contains("过期") || text.contains("领完")) {
                            Logs.i("getPacket: 此红包$text")
                        }
                    }
                }
            }
//            list.last().performAction(AccessibilityNodeInfo.ACTION_CLICK)
//            startActivity<PacketEmptyActivity>()
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun recycleNode(info: AccessibilityNodeInfo) {
        if (info.childCount == 0) {
            if (!info.text.isNullOrEmpty()) {
                Logs.d("recycleNode: info.text: ${info.text}")
                if (info.text.contains("微信红包")) {
                    if (info.isClickable) info.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    else {
                        var parent = info.parent
                        while (parent != null) {
                            if (parent.isClickable) {
                                Logs.i("recycleNode: 找到红包")
//                                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                                break
                            } else parent = parent.parent
                        }
                    }
                }
            }
        } else {
            for (i in 0 until info.childCount) {
                val child = info.getChild(i)
                if (child != null) {
                    recycleNode(child)
                }
            }
        }
    }
}