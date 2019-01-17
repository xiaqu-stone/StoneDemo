package com.stone.templateapp.module

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.stone.templateapp.R
import kotlinx.android.synthetic.main.activity_bezier_progress.*

class BezierProgressActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bezier_progress)
        mTestView.percent = 0
        val runnable = object : Runnable {
            override fun run() {
                mTestView.percent += 5
                if (mTestView.percent < 100) {
                    mTestView.postDelayed(this, 2000)
                }
            }
        }
        mTestView.postDelayed(runnable, 2000)
//        mTestView.post {
//            mTestView.startAnimation()
//        }
    }
}
