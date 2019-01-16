package com.stone.templateapp.module.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.stone.commonutils.ctx
import com.stone.log.Logs
import com.stone.templateapp.R
import java.text.NumberFormat


/**
 * Created By: sqq
 * Created Time: 2019/1/7 18:39.
 *
 * Bezier实现百分比进度的注水球
 *
 */
class BezierProgressView : View {
    constructor(ctx: Context) : this(ctx, null)
    constructor(ctx: Context, attributeSet: AttributeSet?) : this(ctx, attributeSet, 0)
    constructor(ctx: Context, attributeSet: AttributeSet?, defStyle: Int) : super(ctx, attributeSet, defStyle) {
        initAttr(attributeSet, defStyle)
    }

    private fun initAttr(attributeSet: AttributeSet?, defStyle: Int) {
        val ta = ctx.obtainStyledAttributes(attributeSet, R.styleable.BezierProgressView, defStyle, 0)
        mPercent = ta.getFloat(R.styleable.BezierProgressView_bezier_percent, 0.5f).toDouble()
        ta.recycle()
    }

    private val mPaint = Paint()
    private val mCirclePaint = Paint()
    private val mTextPaint = Paint()
    private val mPath = Path()
    /**
     * 当前进度的百分比
     */
    private var mPercent = 0.3

    var percent = 50
        set(value) {
            field = when {
                value > 100 -> 100
                value < 0 -> 0
                else -> value
            }
            mPercent = field.toDouble() / 100
            postInvalidate()
        }

    init {
        mPaint.isAntiAlias = true
        mPaint.color = Color.parseColor("cyan")
        mPaint.strokeWidth = 5f
        mPaint.style = Paint.Style.FILL
        mPaint.isDither = true

        mCirclePaint.isAntiAlias = true
        mCirclePaint.color = Color.parseColor("black")
        mCirclePaint.strokeWidth = 15f
        mCirclePaint.style = Paint.Style.STROKE
        mCirclePaint.isDither = true

        mTextPaint.isAntiAlias = true
        mTextPaint.color = Color.parseColor("purple")
        mTextPaint.strokeWidth = 5f
        mTextPaint.style = Paint.Style.STROKE
        mTextPaint.isDither = true
        mTextPaint.textSize = 140f
    }

    //View的大小
    private var mViewWidth: Int = 0
    private var mViewHeight: Int = 0
    //可绘制区域大小（去除Padding之后）
    private var mWidth: Int = 0
    private var mHeight: Int = 0

    private var r: Float = 0f
    private lateinit var rectF: RectF
    private val mPointF = PointF(0f, 0f)
    private var mAnimateDx = 0f
    private var mAnimateMaxDx = 0f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mViewWidth = w
        mViewHeight = h
        mWidth = mViewWidth - paddingLeft - paddingRight
        mHeight = mViewHeight - paddingTop - paddingBottom

        r = Math.min(mWidth, mHeight) * 0.4f
        mAnimateMaxDx = r
        rectF = RectF(-r, -r, r, r)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //保存之前Canvas的状态，save之后可以调用Canvas的平移、旋转等操作
        canvas.save()
        canvas.translate(measuredWidth / 2f, measuredHeight / 2f)
//        canvas.drawPoint(0f, 0f, mCirclePaint)//绘制原点

        mPath.rewind()
        mPath.addCircle(0f, 0f, r + mCirclePaint.strokeWidth / 2, Path.Direction.CW)
        //裁剪画布为圆形，不绘制超出圆边界的path
        canvas.clipPath(mPath)

        //递增or递减控制点的Y坐标
        mPath.rewind()

        //根据当前百分比，计算出当前圆的Y坐标
        val yCoordinate = r * (1 - 2 * mPercent)
        //根据y坐标计算出，原点与交点形成的直线 与 Y的正半轴形成的夹角度数
        val angle = Math.acos(yCoordinate / r)
        //根据夹角度数计算出当前交点的x坐标
        val xCoordinate = r * Math.sin(angle)
        //正弦周期的x轴长度
        val t = xCoordinate.toFloat()
        mPath.moveTo(-2 * t + mAnimateDx, yCoordinate.toFloat())
//        mPath.moveTo(-xCoordinate.toFloat(), yCoordinate.toFloat())

        //利用二阶Bezier实现正弦波水纹效果
//        mPath.rQuadTo(xCoordinate.toFloat() / 2, -r / 8, xCoordinate.toFloat(), 0f)
//        mPath.rQuadTo(xCoordinate.toFloat() / 2, r / 8, xCoordinate.toFloat(), 0f)

        for (i in 0..2) {
            mPath.rQuadTo(t / 4, t / 8, t / 2, 0f)
            mPath.rQuadTo(t / 4, -t / 8, t / 2, 0f)
        }

        //利用三阶Bezier实现水纹效果
//        mPath.rCubicTo(xCoordinate.toFloat() / 2, -r / 6, xCoordinate.toFloat() * 3 / 2, r / 6, 2 * xCoordinate.toFloat(), 0f)
        val dDegree = Math.toDegrees(angle).toFloat()
        mPath.addArc(rectF, 90 - dDegree, dDegree * 2)
//        mPath.close()
        canvas.drawPath(mPath, mPaint)
        mPath.rewind()

        val numberFormat = NumberFormat.getPercentInstance()
        //百分比保留几位小数：0：10%；1：10.0%
        numberFormat.minimumFractionDigits = 1
        numberFormat.maximumFractionDigits = 3
        val format = numberFormat.format(mPercent)
//        Logs.i("onDraw: $format")
        textCenter(arrayOf(format), mTextPaint, canvas, mPointF, Paint.Align.CENTER)
//        textCenter(arrayOf("进度", format), mTextPaint, canvas, mPointF, Paint.Align.CENTER)

        canvas.drawCircle(0f, 0f, r, mCirclePaint)

        //与 save() 成对出现，恢复之前保存的canvas状态，防止上述save之后的canvas操作对后续的绘制产生影响
        canvas.restore()//

        if (mAnimateMaxDx.toInt() != t.toInt()) {
            startAnimation(t)
        }
//        postInvalidateDelayed(200)
        Logs.d("onDraw: r: $r,mWith: $mWidth,mAnimateDx:$mAnimateDx")
    }

    private var animator: ValueAnimator? = null
    fun startAnimation(max: Float) {
        mAnimateMaxDx = max
        animator?.cancel()
        animator = ValueAnimator.ofFloat(0f, max)
//        animator.repeatMode = ValueAnimator.REVERSE
        animator?.repeatCount = ValueAnimator.INFINITE
        animator?.duration = 1000
        animator?.interpolator = LinearInterpolator()
        animator?.addUpdateListener {
            mAnimateDx = it.animatedValue as Float
            invalidate()
        }
        animator?.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
        animator = null
    }

    /**
     * 多行文本居中、居右、居左
     * @param strings 文本字符串列表
     * @param paint 画笔
     * @param canvas 画布
     * @param point 点的坐标
     * @param align 居中、居右、居左
     */
    protected fun textCenter(strings: Array<String>, paint: Paint, canvas: Canvas, point: PointF, align: Paint.Align) {
        paint.textAlign = align
        val fontMetrics = paint.fontMetrics
        val top = fontMetrics.top
        val bottom = fontMetrics.bottom
        val length = strings.size
        val total = (length - 1) * (-top + bottom) + (-fontMetrics.ascent + fontMetrics.descent)
        val offset = total / 2 - bottom
        for (i in 0 until length) {
            val yAxis = -(length - i - 1) * (-top + bottom) + offset
            canvas.drawText(strings[i], point.x, point.y + yAxis, paint)
        }
    }
}