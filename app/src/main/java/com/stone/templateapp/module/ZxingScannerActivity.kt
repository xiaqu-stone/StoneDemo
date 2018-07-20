package com.stone.templateapp.module

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.stone.templateapp.R

class ZxingScannerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zxing_scanner)
        setTitle(R.string.title_zxing)
    }
}
