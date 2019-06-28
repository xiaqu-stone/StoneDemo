package com.stone.templateapp.demo.accessibility

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.stone.templateapp.R

class PacketEmptyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_packet_empty)


        Handler().postDelayed({ finish() }, 500)
    }
}
