package com.stone.templateapp.demo.provider

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created By: sqq
 * Created Time: 18/7/20 下午4:06.
 */
class DbOpenHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "book_provider.db"
        const val BOOK_TABLE_NAME = "book"
        const val USER_TABLE_NAME = "user"

        //图书和用户信息表
        const val CREATE_BOOK_TABLE = "CREATE TABLE IF NOT EXISTS $BOOK_TABLE_NAME(_id INTEGER PRIMARY KEY,name TEXT)"
        const val CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS $USER_TABLE_NAME(_id INTEGER PRIMARY KEY,name TEXT,sex INT)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_BOOK_TABLE)
        db.execSQL(CREATE_USER_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}