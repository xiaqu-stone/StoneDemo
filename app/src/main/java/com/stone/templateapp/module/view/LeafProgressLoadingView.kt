package com.stone.templateapp.module.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.stone.commonutils.ctx
import com.stone.commonutils.resizeBitmap
import com.stone.log.Logs
import com.stone.templateapp.R


/**
 * Created By: sqq
 * Created Time: 2019/1/16 18:39.
 *
 * 节点选择控件
 *
 */
class LeafProgressLoadingView : View {
    constructor(ctx: Context) : this(ctx, null)
    constructor(ctx: Context, attributeSet: AttributeSet?) : this(ctx, attributeSet, 0)
    constructor(ctx: Context, attributeSet: AttributeSet?, defStyle: Int) : super(ctx, attributeSet, defStyle) {
        initAttr(attributeSet, defStyle)
    }

    var mChangeListener: ((position: Int, msg: String) -> Unit)? = null
    private var mCircleColor = 0
    private var mCircleSelectColor = 0
    private var mTextSize = 0f
    private var mSelectTextSize = 0f
    private var mTextColor = 0
    private var mSelectTextColor = 0
    private var mDegree = 0f
    private fun initAttr(attributeSet: AttributeSet?, defStyle: Int) {
        val ta = ctx.obtainStyledAttributes(attributeSet, R.styleable.LeafProgressLoadingView, defStyle, 0)
        mPercent = ta.getFloat(R.styleable.LeafProgressLoadingView_stone_leaf_percent, 0.5f).toDouble()
        mDegree = ta.getFloat(R.styleable.LeafProgressLoadingView_stone_leaf_degree, 45f)
        ta.recycle()
    }

    private val mPaint = Paint()
    private val mTextPaint = Paint()
    private val mPath = Path()
    private var rectF: RectF = RectF()
    /**
     * 当前进度的百分比
     */
    private var mPercent = 0.3

    private var oldProgress = 0
    var progress = 50
        set(value) {
            this.oldProgress = this.progress
            field = when {
                value > 100 -> 100
                value < 0 -> 0
                else -> value
            }
            leafDynamicPath(duration = 3000)
            postInvalidate()
        }

    //View的大小
    private var mViewWidth: Int = 0
    private var mViewHeight: Int = 0
    //可绘制区域大小（去除Padding之后）
    private var mWidth: Int = 0
    private var mHeight: Int = 0

    //为选中圆的半径
    private var r: Float = 92f
    //选中圆的半径
    private var rA: Float = 0f
    //两个圆的圆心之间的间隔距离
    private var len: Float = 126 * 2.5f

    private var MAX_AMPLITUDE = r

    private var mLeafBitmap: Bitmap
    private var mMatrix: Matrix

    init {
        mPaint.isAntiAlias = true
        mPaint.color = Color.parseColor("red")
        mPaint.strokeWidth = 5f
        mPaint.style = Paint.Style.FILL
        mPaint.isDither = true

        mTextPaint.isAntiAlias = true
        mTextPaint.color = Color.parseColor("black")
        mTextPaint.strokeWidth = 5f
        mTextPaint.style = Paint.Style.FILL
        mTextPaint.textAlign = Paint.Align.CENTER
        mTextPaint.isDither = true

        isClickable = true

        mLeafBitmap = BitmapFactory.decodeResource(ctx.resources, R.drawable.ic_leaf)
        mMatrix = Matrix()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), 400)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mViewWidth = w
        mViewHeight = h
        mWidth = mViewWidth - paddingLeft - paddingRight
        mHeight = mViewHeight - paddingTop - paddingBottom

        val maxRA = Math.min(mWidth / 5f, mHeight * 1f) / 2//大圆的最大半径
        //考虑边际问题
        if (rA >= maxRA) rA = maxRA
        if (rA == 0f) rA = maxRA
        r = 0.8f * rA
        MAX_AMPLITUDE = r
        //计算r 进度条的圆弧半径 以及 进度条的长度
        len = mWidth - mPaint.strokeWidth - 2 * rA + r
        rectF.set(0f, -rA, 2 * rA, rA)
        mLeafBitmap = mLeafBitmap.resizeBitmap(2 * r / 3, r / 3)
    }

    private var leaf = Leaf()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //保存之前Canvas的状态，save之后可以调用Canvas的平移、旋转等操作
        canvas.save()
        //将原点移动到可绘制区域（去除padding的content区域）左边界的Y轴中间位置
        canvas.translate(paddingLeft + 0f, paddingTop + mHeight / 2f)

        mPath.rewind()
        //直接画出两端的半圆，通过close来完成闭合；圆弧的矩形边界要考虑画笔的宽度
//        mPath.moveTo(0f, 0f)
        rectF.set(0f + mPaint.strokeWidth / 2, -rA, 2 * rA + mPaint.strokeWidth / 2, rA)
        mPath.arcTo(rectF, 90f, 180f)
//        mPath.rLineTo(8 * rA - mPaint.strokeWidth, 0f)
        rectF.set(8 * rA - mPaint.strokeWidth / 2, -rA, 10 * rA - mPaint.strokeWidth / 2, rA)
        mPath.arcTo(rectF, -90f, 180f)
        mPath.close()
        mPaint.color = Color.parseColor("#E8D98E")
        mPaint.style = Paint.Style.FILL
        canvas.drawPath(mPath, mPaint)
        canvas.restore()

        //绘制填充
        canvas.save()
        canvas.translate(paddingLeft + rA, paddingTop + mHeight / 2f)
        rectF.set(-r, -r, r, r)
        //进度填充长度
        val pLen = (mPercent * len).toFloat()
        mPath.rewind()
        if (pLen >= r) {//当前进度超出圆弧区域
            mPath.arcTo(rectF, 90f, 180f, true)
            mPath.rLineTo(pLen - r, 0f)
            mPath.rLineTo(0f, r * 2)
        } else {//当前进度未超出圆弧区域
            //夹角的余弦值
            val cosV = (r - pLen) / r * 1.0
            val degree = Math.toDegrees(Math.acos(cosV)).toFloat()//弧度转化为角度
//            canvas.drawArc(rectF, 180 - degree, 2 * degree, false, mPaint)
            mPath.arcTo(rectF, 180 - degree, 2 * degree)
        }
        mPath.close()
        mPaint.color = Color.parseColor("#EBA500")
        canvas.drawPath(mPath, mPaint)
        canvas.restore()
        //
//        canvas.save()
//        canvas.translate(paddingLeft + (mWidth - rA), paddingTop + mHeight / 2f)
//        val w = r / 3
//        val h = r / 6
//        rectF.set(-leaf.x - w, leaf.y - h, -leaf.x + w, leaf.y + h)
//        canvas.drawBitmap(mLeafBitmap, null, rectF, mPaint)
//        canvas.restore()

        canvas.save()
        canvas.translate(paddingLeft + (mWidth - rA), paddingTop + mHeight / 2f)
        mMatrix.reset()
        mMatrix.postTranslate(leaf.x - mLeafBitmap.width / 2, leaf.y - mLeafBitmap.height / 2)
        mMatrix.postRotate(leaf.rotateDegree, leaf.x, leaf.y)//后两个参数：旋转中心坐标
        canvas.drawBitmap(mLeafBitmap, mMatrix, mPaint)
        canvas.restore()

        // TODO: 2019/1/28 叶子动画完成根据 progress 更新 mPercent ，绘制进度条

        Logs.d("onDraw: r: $r,leaf.x: ${leaf.x},,len：$len,,pLen: $pLen")
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mLeafBitmap.recycle()
        mMatrix.reset()
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)

    }

    override fun onFinishInflate() {
        super.onFinishInflate()
    }

    private fun getLeafY(x: Float): Float {
        return (0.7f * r * Math.sin(2 * Math.PI / len * x)).toFloat()
    }

    private var animator: ValueAnimator? = null
    fun leafDynamicPath(max: Float = len - mLeafBitmap.width, duration: Long) {
        animator?.cancel()
        animator = ValueAnimator.ofFloat(Math.abs(leaf.x), max)
        animator?.duration = duration - (Math.abs(leaf.x) / max * duration).toLong()
        animator?.repeatCount = ValueAnimator.INFINITE
        animator?.addUpdateListener { animation ->
            val v = animation.animatedValue as Float
            leaf.x = -v
            leaf.y = getLeafY(leaf.x)
            leaf.rotateDegree = 3 * v % max / max * 360
            Logs.i("leafDynamicPath: v=$v, max=$max")
            postInvalidate()
        }
        animator?.start()
    }


    class Leaf {
        var x = 0f
        var y = 0f
        var animator: ValueAnimator? = null
        var amplitude = 0
        var rotateDegree = 0f
    }
}