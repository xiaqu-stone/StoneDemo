package com.stone.templateapp.module

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.stone.log.Logs
import com.stone.templateapp.R
import kotlinx.android.synthetic.main.activity_canvas_path.*

class CanvasPathActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canvas_path)

        percentSeek.mChangeListener = { seekBar, progress, fromUser ->
            Logs.d("CanvasPathActivity.onCreate() called with: seekBar = [$seekBar], progress = [$progress], fromUser = [$fromUser]")
            mTestView.progress = progress
        }

        percentSeek.post {
            //            percentSeek.mProgress = 50
        }

        mTestView.post {
            mTestView.progress = 10
        }
    }
}
