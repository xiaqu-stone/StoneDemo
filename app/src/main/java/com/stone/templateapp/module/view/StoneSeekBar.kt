package com.stone.templateapp.module.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.SeekBar
import com.stone.commonutils.ctx
import com.stone.templateapp.R
import kotlinx.android.synthetic.main.view_stone_seek_bar.view.*

/**
 * Created By: sqq
 * Created Time: 2019/1/18 17:51.
 *
 * 自定义组合控件——拖动条
 */
class StoneSeekBar : FrameLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttrs(context, attrs, defStyleAttr)
    }

    var mChangeListener: ((seekBar: SeekBar?, progress: Int, fromUser: Boolean) -> Unit)? = null

    var mProgress = 50
        set(value) {
            field = when {
                value > 100 -> 100
                value < 0 -> 0
                else -> value
            }
            tvProgress?.text = value.toString()
            seekBar?.progress = value
            mChangeListener?.invoke(seekBar, value, false)
        }
    var mSeekBarTitle = "title"

    private fun initAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.StoneSeekBar, defStyleAttr, 0)
        mProgress = ta.getInteger(R.styleable.StoneSeekBar_stone_seek_progress, 50)
        mSeekBarTitle = ta.getString(R.styleable.StoneSeekBar_stone_seek_title) ?: "title"
        ta.recycle()
        initView()
    }

    private fun initView() {
        LayoutInflater.from(ctx).inflate(R.layout.view_stone_seek_bar, this)
        tvProgress.text = mProgress.toString()
        tvTitle.text = mSeekBarTitle
        seekBar.progress = mProgress

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mChangeListener?.invoke(seekBar, progress, fromUser)
                tvProgress.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }


}