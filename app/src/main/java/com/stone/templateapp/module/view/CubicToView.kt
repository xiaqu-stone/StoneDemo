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
 * 三阶贝塞尔曲线
 *
 * cubicTo
 */
class CubicToView : View {
    constructor(ctx: Context) : super(ctx)

    constructor(ctx: Context, attributeSet: AttributeSet) : super(ctx, attributeSet)

    private val mPaint = Paint()
    private val mPointPaint = Paint()
    private val mPath = Path()
    private val mCoordinatePaint = Paint()

    init {
        mPaint.isAntiAlias = true
        mPaint.color = Color.parseColor("red")
        mPaint.strokeWidth = 15f
        mPaint.style = Paint.Style.FILL
        mPaint.isDither = true

        mCoordinatePaint.isAntiAlias = true
        mCoordinatePaint.color = Color.parseColor("red")
        mCoordinatePaint.strokeWidth = 5f
        mCoordinatePaint.style = Paint.Style.STROKE
        mCoordinatePaint.isDither = true

        mPointPaint.isAntiAlias = true
        mPointPaint.color = Color.parseColor("black")
        mPointPaint.strokeWidth = 35f
        mPointPaint.style = Paint.Style.FILL_AND_STROKE
        mPointPaint.isDither = true

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

    /**
     * 绘制坐标系的箭头
     */
    private fun drawTriangle(canvas: Canvas) {
        mPath.reset()
        mPath.moveTo(measuredWidth / 2f, 0f)
        mPath.lineTo(measuredWidth / 2f - 50f, 25f)
        mPath.lineTo(measuredWidth / 2f - 50f, -25f)
        mPath.close()
        mCoordinatePaint.style = Paint.Style.FILL
        canvas.drawPath(mPath, mCoordinatePaint)
        mPath.rewind()
        mPath.moveTo(0f, measuredHeight / 2f)
        mPath.lineTo(25f, measuredHeight / 2f - 50f)
        mPath.lineTo(-25f, measuredHeight / 2f - 50f)
        mPath.close()
        canvas.drawPath(mPath, mCoordinatePaint)
    }


    /**
     * 绘制坐标系的轴线，以屏幕中心作为坐标原点
     */
    private fun drawCoordinateSystem(canvas: Canvas) {
        canvas.save()
        canvas.translate(measuredWidth / 2f, measuredHeight / 2f)
        canvas.drawPoint(0f, 0f, mPointPaint)
        mCoordinatePaint.style = Paint.Style.STROKE
        canvas.drawLine(-measuredWidth / 2f, 0f, measuredWidth / 2f, 0f, mCoordinatePaint)
        canvas.drawLine(0f, -measuredHeight / 2f, 0f, measuredHeight / 2f, mCoordinatePaint)
        drawTriangle(canvas)
        canvas.restore()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawCoordinateSystem(canvas)
        //保存之前Canvas的状态，save之后可以调用Canvas的平移、旋转等操作
        canvas.save()
        canvas.translate(measuredWidth / 2f, measuredHeight / 2f)
        mPaint.color = Color.parseColor("black")
        mPaint.style = Paint.Style.STROKE
        mPath.rewind()
        mPath.moveTo(-500f, 0f)
        mPath.cubicTo(-250f, 0f, -250f, 0f, 500f, 0f)
        canvas.drawPath(mPath, mPaint)

        mPath.rewind()
        mPath.moveTo(-500f, 0f)
        mPath.cubicTo(-250f, 500f, 250f, -500f, 500f, 0f)
        mPaint.color = Color.parseColor("green")
        canvas.drawPath(mPath, mPaint)
        mPointPaint.color = Color.parseColor("green")
        canvas.drawPoints(floatArrayOf(-250f, 500f, 250f, -500f), mPointPaint)


        mPath.rewind()
        mPath.moveTo(-500f, 0f)
        mPath.cubicTo(-250f, -500f, 250f, 500f, 500f, 0f)
        mPaint.color = Color.parseColor("purple")
        canvas.drawPath(mPath, mPaint)
        mPointPaint.color = Color.parseColor("purple")
        canvas.drawPoints(floatArrayOf(-250f, -500f, 250f, 500f), mPointPaint)

        //与 save() 成对出现，恢复之前保存的canvas状态，防止上述save之后的canvas操作对后续的绘制产生影响
        canvas.restore()//

        Logs.d("onDraw: r: $r,mWith: $mWidth")
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)

    }


}