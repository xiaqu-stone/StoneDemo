package com.stone.templateapp.demo.provider

import android.content.ContentValues
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.stone.log.Logs
import com.stone.templateapp.R
import com.stone.templateapp.demo.Book
import com.stone.templateapp.demo.User

class ProviderActivity : AppCompatActivity() {
    companion object {
        const val TAG = "ProviderActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_provider)
        setTitle(R.string.title_content_provider)

        executeSQL()
    }

    private fun executeSQL() {
        val values = ContentValues()
        values.put("_id", 6)
        values.put("name", "程序设计艺术")
        contentResolver.insert(BookProvider.BOOK_CONTENT_URI, values)
        val bookCursor = contentResolver.query(BookProvider.BOOK_CONTENT_URI, arrayOf("_id", "name"), null, null, null)
        while (bookCursor.moveToNext()) {
            Logs.d(TAG, "query book: ${Book(bookCursor.getInt(0), bookCursor.getString(1))}")
        }
        bookCursor.close()

        val userCursor = contentResolver.query(BookProvider.USER_CONTENT_URI, arrayOf("_id", "name", "sex"), null, null, null)
        while (userCursor.moveToNext()) {
            Logs.d(TAG, "query user : ${User(userCursor.getInt(0), userCursor.getString(1), userCursor.getInt(2))}")
        }
        userCursor.close()
    }
}
