package com.stone.templateapp.module.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.stone.log.Logs

/**
 * Created By: sqq
 * Created Time: 2019/1/7 18:39.
 *
 * 画布旋转实测
 * canvas.rotate(180f)
 * canvas.rotate(180f,-150f,0f)
 */
class CanvasRotateView : View {
    constructor(ctx: Context) : super(ctx)

    constructor(ctx: Context, attributeSet: AttributeSet) : super(ctx, attributeSet)

    private val mPaint = Paint()
    private val mPointPaint = Paint()
    private val mPath = Path()

    init {
        mPaint.isAntiAlias = true
        mPaint.color = Color.parseColor("red")
        mPaint.strokeWidth = 5f
        mPaint.style = Paint.Style.FILL

        mPointPaint.isAntiAlias = true
        mPointPaint.color = Color.parseColor("black")
        mPointPaint.strokeWidth = 35f
        mPointPaint.style = Paint.Style.FILL_AND_STROKE


    }

    //View的大小
    private var mViewWidth: Int = 0
    private var mViewHeight: Int = 0
    //可绘制区域大小（去除Padding之后）
    private var mWidth: Int = 0
    private var mHeight: Int = 0

    private var r: Float = 0f
    private lateinit var rectF: RectF

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mViewWidth = w
        mViewHeight = h
        mWidth = mViewWidth - paddingLeft - paddingRight
        mHeight = mViewHeight - paddingTop - paddingBottom

        r = Math.min(mWidth, mHeight) * 0.95f
        invalidate()
        rectF = RectF(-r, -r, r, r)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //保存之前Canvas的状态，save之后可以调用Canvas的平移、旋转等操作
        canvas.save()
        //默认以View的左上角作为原点（0，0）
        mPaint.color = Color.parseColor("red")
        canvas.translate(measuredWidth / 2f, measuredHeight / 2f)
        rectF.set(-300f, -300f, 0f, 0f)
        canvas.drawRect(rectF, mPaint)
        mPointPaint.color = Color.parseColor("black")
        canvas.drawPoints(floatArrayOf(0f, 0f), mPointPaint)
        mPointPaint.color = Color.parseColor("yellow")
        canvas.drawPoint(-150f, 0f, mPointPaint)

        canvas.rotate(90f)
        mPaint.color = Color.parseColor("green")
        canvas.drawRect(rectF, mPaint)
        mPointPaint.color = Color.parseColor("blue")
        canvas.drawPoints(floatArrayOf(0f, 0f), mPointPaint)
        //与 save() 成对出现，恢复之前保存的canvas状态，防止上述save之后的canvas操作对后续的绘制产生影响
        canvas.restore()//

        Logs.d("onDraw: r: $r,mWith: $mWidth")
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)

    }


}