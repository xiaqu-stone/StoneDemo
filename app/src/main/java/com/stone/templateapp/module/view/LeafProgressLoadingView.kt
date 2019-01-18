package com.stone.templateapp.module.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.stone.commonutils.ctx
import com.stone.commonutils.sp2px
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
    private fun initAttr(attributeSet: AttributeSet?, defStyle: Int) {
        val ta = ctx.obtainStyledAttributes(attributeSet, R.styleable.StoneNodeSelectView, defStyle, 0)
        rA = ta.getDimension(R.styleable.StoneNodeSelectView_stone_selectRadius, 126f)
        r = ta.getDimension(R.styleable.StoneNodeSelectView_stone_radius, 92f)
        mCircleColor = ta.getColor(R.styleable.StoneNodeSelectView_stone_circle_color, Color.parseColor("gray"))
        mCircleSelectColor = ta.getColor(R.styleable.StoneNodeSelectView_stone_circle_selectColor, Color.parseColor("blue"))
        mTextSize = ta.getDimension(R.styleable.StoneNodeSelectView_stone_textSize, ctx.sp2px(14f).toFloat())
        mSelectTextSize = ta.getDimension(R.styleable.StoneNodeSelectView_stone_select_textSize, ctx.sp2px(18f).toFloat())
        mTextColor = ta.getInt(R.styleable.StoneNodeSelectView_stone_textColor, Color.parseColor("black"))
        mSelectTextColor = ta.getInt(R.styleable.StoneNodeSelectView_stone_select_textColor, Color.parseColor("white"))
        curPos = ta.getInt(R.styleable.StoneNodeSelectView_stone_node_position, 0)
        ta.recycle()
    }

    private val mPaint = Paint()
    private val mTextPaint = Paint()

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

    private var mNodeCount = 3
    var mDataList = arrayListOf("7天", "14天", "30天")
        set(value) {
            field = value; invalidate()
            mNodeCount = field.size
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
    //当前选中的是第几个节点
    var curPos = 0

    init {
        mPaint.isAntiAlias = true
        mPaint.color = Color.parseColor("gray")
        mPaint.strokeWidth = 10f
        mPaint.style = Paint.Style.FILL
        mPaint.isDither = true

        mTextPaint.isAntiAlias = true
        mTextPaint.color = Color.parseColor("black")
        mTextPaint.strokeWidth = 5f
        mTextPaint.style = Paint.Style.FILL
        mTextPaint.textAlign = Paint.Align.CENTER
        mTextPaint.isDither = true

        mNodeCount = mDataList.size
        isClickable = true
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

        val maxRA = Math.min(mWidth * 0.9f / mNodeCount, mHeight * 1f) / 2//大圆的最大半径
        //考虑边际问题
        if (rA >= maxRA) rA = maxRA
        if (rA == 0f) rA = 1.3f * r
        if (r >= rA) r = rA - 20f
        if (mNodeCount > 1) len = (mWidth - 2 * rA) / (mNodeCount - 1)
//        rectF = RectF(-r, -r, r, r)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (curPos >= mNodeCount) curPos = mNodeCount
        //保存之前Canvas的状态，save之后可以调用Canvas的平移、旋转等操作
        canvas.save()
        //将原点移动到可绘制区域（去除padding的content区域）左边界的Y轴中间位置
        canvas.translate(paddingLeft + 0f, paddingTop + mHeight / 2f)
        mPaint.color = mCircleColor
        canvas.drawLine(rA, 0f, mWidth - rA, 0f, mPaint)
        if (mNodeCount > 1) {
            for (i in 0 until mNodeCount) {
                if (i == curPos) {
                    mPaint.color = mCircleSelectColor
                    canvas.drawCircle(rA + i * len, 0f, rA, mPaint)
                } else {
                    mPaint.color = mCircleColor
                    canvas.drawCircle(rA + i * len, 0f, r, mPaint)
                }
            }
        } else {
            mPaint.color = mCircleSelectColor
            canvas.drawCircle(mWidth / 2f, 0f, rA, mPaint)
        }

        drawText(canvas)

        canvas.restore()
//        postInvalidateDelayed(200)
        Logs.d("onDraw: r: $r,mWith: $mWidth")
    }

    private fun drawText(canvas: Canvas) {
        // TODO: 2019/1/17 未做字体大小超出节点区域的限制
        val yAxis = getTextBaselineOffset(mTextPaint, mTextSize)
        val ySelectAxis = getTextBaselineOffset(mTextPaint, mSelectTextSize)

        if (mNodeCount > 1) {
            for (i in 0 until mNodeCount) {
                if (i == curPos) {
                    mTextPaint.color = mSelectTextColor
                    mTextPaint.textSize = mSelectTextSize
                    canvas.drawText(mDataList[i], rA + i * len, ySelectAxis, mTextPaint)
                } else {
                    mTextPaint.color = mTextColor
                    mTextPaint.textSize = mTextSize
                    canvas.drawText(mDataList[i], rA + i * len, yAxis, mTextPaint)
                }
            }
        } else {
            mTextPaint.color = mSelectTextColor
            mTextPaint.textSize = mSelectTextSize
            canvas.drawText(mDataList[0], mWidth / 2f, ySelectAxis, mTextPaint)
        }
    }

    private fun getTextBaselineOffset(mTextPaint: Paint, mTextSize: Float): Float {
        //根据字体大小计算文本的 baseline 位置
        mTextPaint.textSize = mTextSize
        val metrics = mTextPaint.fontMetrics
        return (-metrics.ascent + metrics.descent) / 2 - metrics.bottom
    }

    private var tempPos = -1
    private var moveTouchCount = 0
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isClickable) {//可点击时，判断是否在可点击区域
            //当节点只有一个时，不处理touch事件
            if (mNodeCount == 1) return false

            val touchPos = checkPoint(event)
            if (touchPos < 0) return false
            return when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    moveTouchCount = 0
                    tempPos = touchPos
                    parent.requestDisallowInterceptTouchEvent(true)//不允许父View拦截后续事件
                    true
                }
                MotionEvent.ACTION_UP -> {
                    if (tempPos == touchPos) {
                        if (curPos != touchPos) {
                            mChangeListener?.invoke(touchPos, mDataList[touchPos])
                        }
                        curPos = touchPos
                        tempPos = -1
                        invalidate()
                    }
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    moveTouchCount++
                    if (moveTouchCount > 2) {//处理可能出现的滑动冲突
                        //当MOVE事件多次出现时，允许父View拦截滑动事件，避免影响父View是可滑动View时的滑动效果
                        parent.requestDisallowInterceptTouchEvent(false)
                    }
                    Logs.d("StoneNodeSelectView.onTouchEvent() called with: event = [$event]")
                    return tempPos >= 0//
                }
                else -> false
            }
        } else {//不可点击时，不处理事件
            return false
        }
    }

    /**
     * 检查点是否在可点击的节点区域内部
     *
     * @return >=0:在可点击区域内，消费事件；-1: 不在可点击区域内 不处理事件
     */
    private fun checkPoint(event: MotionEvent): Int {
        for (i in 0 until mNodeCount) {
            val dx = Math.abs(event.x - (paddingLeft + rA + i * len))
            val dy = Math.abs(event.y - (paddingTop + mHeight / 2f))
            if (dx <= r && dy <= r) {//在节点区域内
                return i
            }
        }
        return -1
    }


}