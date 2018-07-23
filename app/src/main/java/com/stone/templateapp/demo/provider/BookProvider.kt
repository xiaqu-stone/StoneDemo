package com.stone.templateapp.demo.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import com.stone.log.Logs
import com.stone.templateapp.BuildConfig

/**
 * Created By: sqq
 * Created Time: 18/7/19 下午12:07.
 */
class BookProvider : ContentProvider() {
    companion object {
        const val TAG = "BookProvider"
        const val AUTHORITY = "${BuildConfig.APPLICATION_ID}.book_provider"

        val USER_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/user")
        val BOOK_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/book")

        const val BOOK_URI_CODE = 0
        const val USER_URI_CODE = 1
    }

    private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    private lateinit var mDb: SQLiteDatabase

    init {
        //创建URI关联，可以进入SDK中查看注释使用示例
        //这里将 book 和 user 两个表分别与URICode进行了关联，分别为 0 和 1
        sUriMatcher.addURI(AUTHORITY, "book", BOOK_URI_CODE)
        sUriMatcher.addURI(AUTHORITY, "user", USER_URI_CODE)
    }

    private fun getTableName(uri: Uri): String {
        return when (sUriMatcher.match(uri)) {
            BOOK_URI_CODE -> DbOpenHelper.BOOK_TABLE_NAME
            USER_URI_CODE -> DbOpenHelper.USER_TABLE_NAME
            else -> ""
        }
    }


    override fun onCreate(): Boolean {
        Logs.d(TAG, "onCreate, current thread is : ${Thread.currentThread().name}")
        initProviderData()
        return true
    }

    /**
     * 初始化数据库，执行数据库操作属于耗时操作
     */
    private fun initProviderData() {
        println("provider init data")
        mDb = DbOpenHelper(context).writableDatabase
        mDb.execSQL("delete from ${DbOpenHelper.BOOK_TABLE_NAME}")
        mDb.execSQL("delete from ${DbOpenHelper.USER_TABLE_NAME}")
        mDb.execSQL("insert into book values(3,'Android');")
        mDb.execSQL("insert into book values(4,'iOS');")
        mDb.execSQL("insert into book values(5,'Html5');")
        mDb.execSQL("insert into user values(1,'jake',1);")
        mDb.execSQL("insert into user values(2,'jasmine',0);")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        Logs.d(TAG, "insert, current thread is : ${Thread.currentThread().name}")
        val tableName = getTableName(uri)
        if (tableName.isEmpty()) {
            throw IllegalArgumentException("Unsupported URI: $uri")
        }
        mDb.insert(tableName, null, values)
        context.contentResolver.notifyChange(uri, null)
        return uri
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        Logs.d(TAG, "query, current thread is : ${Thread.currentThread().name}")
        val tableName = getTableName(uri)
        if (tableName.isEmpty()) {
            throw IllegalArgumentException("Unsupported URI : $uri")
        }
        return mDb.query(tableName, projection, selection, selectionArgs, null, null, sortOrder, null)
    }


    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        val row = mDb.update(getTableName(uri), values, selection, selectionArgs)
        if (row > 0) {
            context.contentResolver.notifyChange(uri, null)
        }
        return row
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val count = mDb.delete(getTableName(uri), selection, selectionArgs)
        if (count > 0) {
            context.contentResolver.notifyChange(uri, null)
        }
        return count
    }

    override fun getType(uri: Uri?): String? {
        Logs.d(TAG, "getType, current thread is : ${Thread.currentThread().name}")
        return null
    }
}