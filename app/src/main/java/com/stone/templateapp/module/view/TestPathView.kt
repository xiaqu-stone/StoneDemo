package com.stone.templateapp.module.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.stone.log.Logs

/**
 * Created By: sqq
 * Created Time: 2019/1/7 18:39.
 *
 * 关于 Paint.Style 的不同实测
 */
class TestPathView : View {
    constructor(ctx: Context) : super(ctx)

    constructor(ctx: Context, attributeSet: AttributeSet) : super(ctx, attributeSet)

    private val mPaint = Paint()
    private val mPath = Path()

    init {
        mPaint.isAntiAlias = true
        mPaint.color = Color.parseColor("red")
        mPaint.strokeWidth = 20f
        mPaint.style = Paint.Style.FILL
    }

    //View的大小
    private var mViewWidth: Int = 0
    private var mViewHeight: Int = 0
    //可绘制区域大小（去除Padding之后）
    private var mWidth: Int = 0
    private var mHeight: Int = 0

    private var r: Float = 0f
//    private lateinit var rectF: RectF

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mViewWidth = w
        mViewHeight = h
        mWidth = mViewWidth - paddingLeft - paddingRight
        mHeight = mViewHeight - paddingTop - paddingBottom

        r = Math.min(mWidth, mHeight) * 0.95f
        invalidate()
//        rectF = RectF(-r, -r, r, r)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //第一个
        mPath.moveTo(50f, 50f)
        mPath.lineTo(r / 4 - 50, r / 4 - 50)
        mPath.lineTo(r / 4 - 50, 50f)
        mPath.close()
        mPaint.color = Color.parseColor("red")
        mPaint.style = Paint.Style.STROKE
        canvas.drawPath(mPath, mPaint)

        mPath.rewind()
        mPath.moveTo(r / 4 + 50f, 50f)
        mPath.lineTo(2 * r / 4 - 50, r / 4 - 50)
        mPath.lineTo(2 * r / 4 - 50, 50f)
        mPath.close()
        canvas.drawPath(mPath, mPaint)//先绘制线条
        mPaint.color = Color.parseColor("grey")
        mPaint.style = Paint.Style.FILL
        canvas.drawPath(mPath, mPaint)//再绘制填充

        mPath.rewind()
        mPath.moveTo(2 * r / 4 + 50f, 50f)
        mPath.lineTo(3 * r / 4 - 50, r / 4 - 50)
        mPath.lineTo(3 * r / 4 - 50, 50f)
        mPath.close()
        mPaint.color = Color.parseColor("grey")
        mPaint.style = Paint.Style.FILL_AND_STROKE
        canvas.drawPath(mPath, mPaint)
        mPaint.color = Color.parseColor("red")
        mPaint.style = Paint.Style.STROKE
        canvas.drawPath(mPath, mPaint)

        mPath.rewind()
        mPath.moveTo(3 * r / 4 + 50f, 50f)
        mPath.lineTo(r - 50, r / 4 - 50)
        mPath.lineTo(r - 50, 50f)
        mPath.close()
        mPaint.color = Color.parseColor("red")
        mPaint.style = Paint.Style.STROKE
        canvas.drawPath(mPath, mPaint)
        mPaint.color = Color.parseColor("grey")
        mPaint.style = Paint.Style.FILL_AND_STROKE
        canvas.drawPath(mPath, mPaint)

        Logs.d("onDraw: r: $r,mWith: $mWidth")
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)

    }


}