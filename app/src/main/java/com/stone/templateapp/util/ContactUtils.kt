package com.stone.templateapp.util

import android.content.Context
import android.provider.ContactsContract
import com.stone.commonutils.QUtil
import com.stone.log.Logs
import org.json.JSONObject

/**
 * Created By: sqq
 * Created Time: 2019-10-24 17:23.
 */
object ContactUtils {


}

/**
 * 获取联系人 name&phones
 */
fun Context.getContacts() {
    val projection = arrayOf(ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME)
    val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, projection, null, null, null)
    val map = mutableMapOf<String, String?>()
    if (cursor != null) {
        while (cursor.moveToNext()) {
            val id = cursor.getLong(0)
            val name = cursor.getString(1)
            Logs.i("getContacts: id:$id,name:$name")
            val phoneCursor = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id,
                    null, null
            )
            val sb = StringBuilder()
            if (phoneCursor != null) {
                while (phoneCursor.moveToNext()) {
                    Logs.d("getContacts: ${phoneCursor.getString(0)}")
                    sb.append(phoneCursor.getString(0)).append(",")
                }
            } else {
                Logs.d("getContacts: cursor 为空")

            }
            if (sb.isNotEmpty() && sb.endsWith(",")) {
                Logs.d("getContacts: sb：$sb")
                sb.delete(sb.length - 1, sb.length)
                map[name] = sb.toString()
            }
            phoneCursor?.close()
        }
    }
    cursor?.close()

    val json = JSONObject(map).toString()
    Logs.json(json)

}


/**
 * 按照手机号数量做数据输出，即同一个联系人有两个手机号，会输出两个数据
 */
fun Context.getContactPhoneColumn() {
    val cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
    if (cursor != null) {
        Logs.i("getContactPhoneColumn: ${cursor.count}")
        while (cursor.moveToNext()) {
            QUtil.recursiveCursorColumn(cursor)
        }
    }
    cursor?.close()
}

/**
 * 把联系人输出，但是不包含手机号
 */
fun Context.getContactPersonColumn() {

    val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
    if (cursor != null) {
        Logs.i("getContactPhoneColumn: ${cursor.count}")
        while (cursor.moveToNext()) {
            QUtil.recursiveCursorColumn(cursor)
        }
    }
    cursor?.close()
}


