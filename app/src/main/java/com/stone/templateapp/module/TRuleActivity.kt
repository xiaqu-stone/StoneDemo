package com.stone.templateapp.module

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.stone.log.Logs
import com.stone.templateapp.R
import kotlinx.android.synthetic.main.activity_trule.*

class TRuleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trule)

        tRule1.setOnRulerChangeListener { obj, pos ->
            Logs.d("TRuleActivity.onCreate() called with: obj = [$obj], pos = [$pos]")
        }
    }
}
