package com.stone.templateapp.util

import android.content.ContentProviderOperation
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.provider.ContactsContract
import com.stone.commonutils.QUtil
import com.stone.log.Logs
import org.jetbrains.anko.longToast
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast
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
//                    Logs.d("getContacts: ${phoneCursor.getString(0)}")
                    sb.append(phoneCursor.getString(0)).append(",")
                }
            } else {
                Logs.d("getContacts: cursor 为空")
            }
            if (sb.isNotEmpty() && sb.endsWith(",")) {
//                Logs.d("getContacts: sb：$sb")
                sb.delete(sb.length - 1, sb.length)
                map[name] = sb.toString()
            }
            phoneCursor?.close()
        }
    }
    cursor?.close()

    val json = JSONObject(map).toString()
    Logs.json(json)
    runOnUiThread { longToast(json) }
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

/**
 *
 */
fun Context.getContactRawColumn() {
    val cursor = contentResolver.query(ContactsContract.RawContacts.CONTENT_URI, null, null, null, null)
    if (cursor != null) {
        Logs.i("getContactPhoneColumn: ${cursor.count}")
        while (cursor.moveToNext()) {
            QUtil.recursiveCursorColumn(cursor)
        }
    }
    cursor?.close()
}

/**
 * 插入一条记录
 */
fun Context.insertContact(name: String, phone: String, email: String) {

    val values = ContentValues()
    val uri = contentResolver.insert(ContactsContract.RawContacts.CONTENT_URI, values)
    val contactId = ContentUris.parseId(uri)
    values.clear()
    values.put(ContactsContract.Data.RAW_CONTACT_ID, contactId)
//    //添加内容类型（MIMETYPE）
//        values.put(StructuredName.GIVEN_NAME, "凯风自南");//添加名字，添加到first name位置
    values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
    values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name)
//    android.provider.ContactsContract.Data.CONTENT_URI, values);
//        //往data表入电话数据
    contentResolver.insert(ContactsContract.Data.CONTENT_URI, values)

    values.clear()
    values.put(ContactsContract.Data.RAW_CONTACT_ID, contactId)
    values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
    values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone)
    values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
    contentResolver.insert(ContactsContract.Data.CONTENT_URI, values)

    values.clear()
    values.put(ContactsContract.Data.RAW_CONTACT_ID, contactId)
    values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
    values.put(ContactsContract.CommonDataKinds.Email.DATA, email)
    values.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
    contentResolver.insert(ContactsContract.Data.CONTENT_URI, values)

    runOnUiThread { toast("添加成功") }
}

/**
 * 批量添加联系人
 * @param contacts pair: name to number
 */
fun Context.insertContacts(vararg contacts: Pair<String, String>) {
    val ops = arrayListOf<ContentProviderOperation>()
    contacts.forEach {
        val offsetContactId = ops.size
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withYieldAllowed(true)
                .build())
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, offsetContactId)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, it.first)
                .build())
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, offsetContactId)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, it.second)
                .build())
    }
    val results = contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)

    // TODO: 2019-10-25 log 输出
    results.forEach { Logs.i("insertContacts: ${it.uri}") }

    runOnUiThread { toast("批量插入成功") }
}


fun Context.deleteContact(){
//    contentResolver.delete(ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI,))
}


