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
 * 绘制直线
 * lineTo rLineTo
 */
class LineToView : View {
    constructor(ctx: Context) : super(ctx)

    constructor(ctx: Context, attributeSet: AttributeSet) : super(ctx, attributeSet)

    private val mPaint = Paint()
    private val mPath = Path()

    init {
        mPaint.isAntiAlias = true
        mPaint.color = Color.parseColor("red")
        mPaint.strokeWidth = 5f
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
        //默认以View的左上角作为原点（0，0）
        mPath.reset()
        mPath.lineTo(100f, 200f)
        mPath.lineTo(400f, 400f)
        //以（400，400）作为原点，偏移（300，400），则实际坐标为（700，800）
        mPath.rLineTo(300f,400f)
        mPaint.color = Color.parseColor("red")
        mPaint.style = Paint.Style.STROKE
        canvas.drawPath(mPath, mPaint)

        Logs.d("onDraw: r: $r,mWith: $mWidth")
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)

    }


}