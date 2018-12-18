package com.stone.templateapp.module.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.stone.commonutils.DisplayExtKt;
import com.stone.log.Logs;
import com.stone.templateapp.R;

public class TRule extends View {
    //不可设置更改
    private Context mContext;
    private OnRulerChangeListener onRulerChangeListener;
    private GestureDetector gestureDetector;
    private int mOnceTouchEventOffset;
    private int mScrollingOffset;
    private int mPos;
    int mPosition;
    private Paint mTextPoint;
    private Paint mBottomPaint;
    private Paint mMiddlePaint;
    private Paint mBigScalePaint;
    private Paint mSmallScalePaint;
    private float mHeight;
    private int mWidth;
    //更改初始数据
    private int mCurrentIndex = 0;
    //设置初始格式(起始下标、是否显示中心文本、中心文本、下标文本的后缀)
    private int mIndexStart;
    private boolean mShowCentText;
    private String mCentText;
    private String mIndexText;
    //设置颜色相关
    private int mTextColor;
    private int mTextColorChoose;
    private int mMiddleLineColor;
    private int mBigScaleColor;
    private int mSmallScaleColor;
    private int mBottomLineColor;
    //设置高度
    private float mSmallScaleHeight;
    private float mMiddleLineHeight;
    private float mBottomLineHeight;
    private float mBigScaleHeight;
    //设置小刻度间隔
    private int mSmallScaleSpace;
    //设置大刻度、小刻度个数
    private int mBigScaleNum;
    private int mSmallScaleNum;
    //文本大小
    private float mTextSize;
    private float mTextSizeChoose;
    //设置宽度
    private float mMiddleLineWidth;
    private float mBigScaleWidth;
    private float mSmallScaleWidth;
    //设置控件底部到底部线距离
    private float mToBottomHeight;
    //设置文本底部到中心线顶部距离
    private float mToLineTop;
    //设置灵敏度
    private float mSensitiveness;


    public TRule(Context context) {
        this(context, null, 0);
    }

    public TRule(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TRule(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        gestureDetector = new GestureDetector(context, gestureListener);
        initPaint();
        initAttr(attrs, defStyleAttr);
    }

    private void initPaint() {
        mMiddlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBottomPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBigScalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSmallScalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPoint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    private void initAttr(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.TRule, defStyleAttr, 0);
        //标尺开始显示位置(以小刻度为单位)
        mCurrentIndex = typedArray.getInteger(R.styleable.TRule_set_location, 0);
        //大刻度数、一个大刻度包含小刻度数、小刻度间隔宽度
        mBigScaleNum = typedArray.getInteger(R.styleable.TRule_big_scale_num, 12);
        mSmallScaleNum = typedArray.getInteger(R.styleable.TRule_small_scale_num, 10);
        mSmallScaleSpace = (int) typedArray.getDimension(R.styleable.TRule_small_scale_space, DisplayExtKt.dp2px(mContext, 10));
        //中间线、大、小刻度高度、宽度
        mMiddleLineHeight = typedArray.getDimension(R.styleable.TRule_middle_line_height, DisplayExtKt.dp2px(mContext, 40));
        mMiddleLineWidth = typedArray.getDimension(R.styleable.TRule_middle_line_width, DisplayExtKt.dp2px(mContext, (float) 0.5));
        mBigScaleHeight = typedArray.getDimension(R.styleable.TRule_big_scale_height, DisplayExtKt.dp2px(mContext, 20));
        mBigScaleWidth = typedArray.getDimension(R.styleable.TRule_big_scale_width, DisplayExtKt.dp2px(mContext, (float) 0.5));
        mSmallScaleHeight = typedArray.getDimension(R.styleable.TRule_small_scale_height, DisplayExtKt.dp2px(mContext, 10));
        mSmallScaleWidth = typedArray.getDimension(R.styleable.TRule_small_scale_width, DisplayExtKt.dp2px(mContext, (float) 0.5));
        //中间线、大、小刻度颜色
        mMiddleLineColor = typedArray.getColor(R.styleable.TRule_middle_line_color, mContext.getResources().getColor(R.color.middle_line_color));
        mBigScaleColor = typedArray.getColor(R.styleable.TRule_big_scale_color, mContext.getResources().getColor(R.color.big_scale_color));
        mSmallScaleColor = typedArray.getColor(R.styleable.TRule_small_scale_color, mContext.getResources().getColor(R.color.small_scale_color));
        //刻度字体大小以及选中大小
        mTextSize = typedArray.getDimension(R.styleable.TRule_text_size, DisplayExtKt.sp2px(mContext, (float) 15.5));
        mTextSizeChoose = typedArray.getDimension(R.styleable.TRule_text_size_choose, DisplayExtKt.sp2px(mContext, 18));
        //刻度字体颜色、以及选中颜色
        mTextColor = typedArray.getColor(R.styleable.TRule_text_color, mContext.getResources().getColor(R.color.text_color));
        mTextColorChoose = typedArray.getColor(R.styleable.TRule_text_color_choose, mContext.getResources().getColor(R.color.text_color_choose));
        //底部线颜色、高、底部线距离控件底部距离
        mBottomLineColor = typedArray.getColor(R.styleable.TRule_bottom_color, mContext.getResources().getColor(R.color.bottom_line_color));
        mBottomLineHeight = typedArray.getDimension(R.styleable.TRule_bottom_line_height, DisplayExtKt.dp2px(mContext, (float) 0.5));
        mToBottomHeight = typedArray.getDimension(R.styleable.TRule_bottom_line_to_view_bottom, DisplayExtKt.dp2px(mContext, 2));
        //文本底部到中间线顶部距离
        mToLineTop = typedArray.getDimension(R.styleable.TRule_text_bottom_to_line_top, DisplayExtKt.dp2px(mContext, 30));
        //灵敏度(以倍数为记,默认为1,类型为float)
        mSensitiveness = typedArray.getFloat(R.styleable.TRule_sensitiveness, 1);
        //各个下标对应文本(如1月,2月则string为"月",如1天、2天则为"天",前缀Index自动添加)
        mIndexText = typedArray.getString(R.styleable.TRule_index_text);
        //中间标记文本、是否显示(只有大刻度数目为偶数个时候生效)
        mCentText = typedArray.getString(R.styleable.TRule_center_text);
        mShowCentText = typedArray.getBoolean(R.styleable.TRule_show_center_text, true);
        //下标索引开始位置,默认为零
        mIndexStart = typedArray.getInteger(R.styleable.TRule_index_start, 1);
        typedArray.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        MeasureSpec.getSize(widthMeasureSpec);
//        MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        mWidth = getWidth();
        mHeight = getHeight();
        drawBottomLine(canvas);
        drawScale(canvas);
        drawMiddleLine(canvas);
    }


    private void drawBottomLine(Canvas canvas) {
        mBottomPaint.setStrokeWidth(mBottomLineHeight);
        mBottomPaint.setColor(mBottomLineColor);
        canvas.drawLine(0, mHeight - mToBottomHeight - mBottomLineHeight / 2, mWidth, mHeight - mToBottomHeight - mBottomLineHeight / 2, mBottomPaint);
    }

    private void drawMiddleLine(Canvas canvas) {
        mMiddlePaint.setStrokeWidth(mMiddleLineWidth);
        mMiddlePaint.setColor(mMiddleLineColor);
        canvas.drawLine(mWidth / 2, mHeight - mToBottomHeight - mBottomLineHeight - mMiddleLineHeight, mWidth / 2, mHeight - mToBottomHeight - mBottomLineHeight, mMiddlePaint);
    }

    private void drawScale(Canvas canvas) {
        mBigScalePaint.setColor(mBigScaleColor);
        mBigScalePaint.setStrokeWidth(mBigScaleWidth);
        mSmallScalePaint.setColor(mSmallScaleColor);
        mSmallScalePaint.setStrokeWidth(mSmallScaleWidth);
        float startLocation = (mWidth / 2) - mSmallScaleSpace * mCurrentIndex;
        for (int i = 0; i <= mSmallScaleNum * mBigScaleNum; i++) {
            float location = startLocation + i * mSmallScaleSpace;
            if (i % mSmallScaleNum == 0) {
                //大刻度
                canvas.drawLine(location, mHeight - mToBottomHeight - mBottomLineHeight, location, mHeight - mToBottomHeight - mBottomLineHeight - mBigScaleHeight, mBigScalePaint);
                String drawStr = "";
                if (mBigScaleNum % 2 == 0 && mShowCentText) {
                    drawStr = bigNumIsEven(i);
                } else {
                    drawStr = bigNumIsOdd(i);
                }
                Rect bounds = new Rect();
                if (i == mCurrentIndex) {
                    mTextPoint.setColor(mTextColorChoose);
                    mTextPoint.setTextSize(mTextSizeChoose);
                } else {
                    mTextPoint.setColor(mTextColor);
                    mTextPoint.setTextSize(mTextSize);
                }
                mTextPoint.getTextBounds(drawStr, 0, drawStr.length(), bounds);
                Logs.d("大刻度文字： " + drawStr);
                //添加刻度文字
                canvas.drawText(drawStr, location - bounds.width() / 2, mHeight - mToBottomHeight - mMiddleLineHeight - mToLineTop, mTextPoint);
            } else {
                //小刻度
                canvas.drawLine(location, mHeight - mToBottomHeight - mBottomLineHeight, location, mHeight - mToBottomHeight - mBottomLineHeight - mSmallScaleHeight, mSmallScalePaint);
            }

        }
    }


    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    private int space = 1;//大刻度的间隔

    @NonNull
    private String bigNumIsOdd(int i) {
        String drawStr;
        if (mIndexStart > 0) {
            int j = i / mSmallScaleNum + mIndexStart;
            if (mIndexText == null) mIndexText = "月份";
            drawStr = j * space + mIndexText;
        } else {
            if (mIndexText == null) mIndexText = "月份";
            drawStr = i / mSmallScaleNum + mIndexText;
        }
        return drawStr;
    }

    @NonNull
    private String bigNumIsEven(int i) {
        String drawStr;
        if (i / mSmallScaleNum < mBigScaleNum / 2) {
            int j = i / mSmallScaleNum;
            if (mIndexStart > 0) j = j + mIndexStart;
            if (mIndexText == null) mIndexText = "月份";
            drawStr = j + mIndexText;
        } else if (i / mSmallScaleNum == mBigScaleNum / 2) {
            if (mCentText == null) mCentText = "全部";
            drawStr = mCentText;
        } else {
            int j = i / mSmallScaleNum - 1;
            if (mIndexStart > 0) j = j + mIndexStart;
            if (mIndexText == null) mIndexText = "月份";
            drawStr = j + mIndexText;
        }
        return drawStr;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mOnceTouchEventOffset = 0;
            case MotionEvent.ACTION_MOVE:
                gestureDetector.onTouchEvent(event);
                break;
            case MotionEvent.ACTION_UP:
                int round = 0;
                if (Math.abs(mOnceTouchEventOffset) > mSmallScaleSpace * mSmallScaleNum) {
                    round = Math.round(mPos / (float) mSmallScaleNum);
                } else if (mOnceTouchEventOffset < 0) {
                    double ceil = Math.ceil(mPos / (float) mSmallScaleNum);
                    round = (int) ceil;
                } else {
                    double floor = Math.floor(mPos / (float) mSmallScaleNum);
                    round = (int) floor;
                }
                innerSetCurrentIndex(round * mSmallScaleNum, true, true);
        }
        return true;
    }

    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            doScroll((int) -distanceX);
            invalidate();
            return true;
        }
    };

    private void doScroll(int delta) {
        //每次onTouch完成之后要考虑具体偏移设计,需要参照单次onTouch总的偏移量(手势的onScroll方法会多次执行,所以需要做累加).
        mOnceTouchEventOffset += delta * mSensitiveness;
        //偏移量叠加
        mScrollingOffset += delta * mSensitiveness;
        //总共滚动了多少个Item
        int mCount = mScrollingOffset / mSmallScaleSpace;
        //当前刻度位置
        mPos = mCurrentIndex - mCount;
        if (mPos < 0) {
            mPos = 0;
        } else if (mPos >= mBigScaleNum * mSmallScaleNum) {
            mPos = mBigScaleNum * mSmallScaleNum;
        }
        if (mPos != mCurrentIndex) {
            innerSetCurrentIndex(mPos, true, false);
        }
        mScrollingOffset = mScrollingOffset - mCount * mSmallScaleSpace;
    }


    public void innerSetCurrentIndex(int index, boolean inner, boolean callBack) {
        if (mCurrentIndex < 0 || mCurrentIndex > mBigScaleNum * mSmallScaleNum) {
            return;
        }
        if (!inner) {
            mCurrentIndex = Math.round(index / mSmallScaleNum) * mSmallScaleNum;
        } else {
            mCurrentIndex = index;
        }
        invalidate();
        if (mCurrentIndex % mSmallScaleNum >= mSmallScaleNum / 2) {
            mPosition = mCurrentIndex / mSmallScaleNum + 1;
        } else {
            mPosition = mCurrentIndex / mSmallScaleNum;
        }
        if (onRulerChangeListener != null && callBack) {
            switch (mBigScaleNum % 2) {
                case 0:
                    if (mShowCentText) evenCallBack(mPosition);
                    else oddCallBack(mPosition);
                    break;
                case 1:
                    oddCallBack(mPosition);
                    break;
            }
        }
    }


    private void evenCallBack(int position) {
        if (mIndexStart > 0) {
            if (position < mBigScaleNum / 2)
                onRulerChangeListener.onRuleChanged(this, position + mIndexStart);
            else if (position == mBigScaleNum / 2)
                onRulerChangeListener.onRuleChanged(this, -1);
            else onRulerChangeListener.onRuleChanged(this, position + mIndexStart - 1);
        } else {
            if (position - mIndexStart < mBigScaleNum / 2)
                onRulerChangeListener.onRuleChanged(this, position);
            else if (position == mBigScaleNum / 2)
                onRulerChangeListener.onRuleChanged(this, -1);
            else onRulerChangeListener.onRuleChanged(this, position - 1);
        }
    }

    private void oddCallBack(int position) {
        if (mIndexStart > 0) {
            onRulerChangeListener.onRuleChanged(this, position + mIndexStart);
        } else {
            onRulerChangeListener.onRuleChanged(this, position);
        }

    }

    //-------------------------事件监听------------------------------------
    public interface OnRulerChangeListener {
        void onRuleChanged(Object object, int position);
    }

    public void setOnRulerChangeListener(OnRulerChangeListener onRulerChangeListener) {
        this.onRulerChangeListener = onRulerChangeListener;
    }

    //--------------------------初始数据设置-------------------------------
    public void setLocation(int index) {
        innerSetCurrentIndex(index, false, true);
    }
    //-------------------------设置格式------------------------------------

    public void setIndexStart(int indexStart) {
        mIndexStart = indexStart;
    }

    public void setShowCentText(boolean showCentText) {
        mShowCentText = showCentText;
    }

    public void setCentText(String centText) {
        mCentText = centText;
    }

    public void setIndexText(String indexText) {
        mIndexText = indexText;
    }
    //-----------------------------设置样式---------------------------------

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public void setTextColorChoose(int textColorChoose) {
        mTextColorChoose = textColorChoose;
    }

    public void setMiddleLineColor(int middleLineColor) {
        mMiddleLineColor = middleLineColor;
    }

    public void setBigScaleColor(int bigScaleColor) {
        mBigScaleColor = bigScaleColor;
    }

    public void setSmallScaleColor(int smallScaleColor) {
        mSmallScaleColor = smallScaleColor;
    }

    public void setBottomLineColor(int bottomLineColor) {
        mBottomLineColor = bottomLineColor;
    }

    public void setSmallScaleHeight(float smallScaleHeight) {
        mSmallScaleHeight = smallScaleHeight;
    }

    public void setMiddleLineHeight(float middleLineHeight) {
        mMiddleLineHeight = middleLineHeight;
    }

    public void setBottomLineHeight(float bottomLineHeight) {
        mBottomLineHeight = bottomLineHeight;
    }

    public void setBigScaleHeight(float bigScaleHeight) {
        mBigScaleHeight = bigScaleHeight;
    }

    public void setSmallScaleSpace(int smallScaleSpace) {
        mSmallScaleSpace = smallScaleSpace;
    }

    public void setBigScaleNum(int bigScaleNum) {
        mBigScaleNum = bigScaleNum;
    }

    public void setSmallScaleNum(int smallScaleNum) {
        mSmallScaleNum = smallScaleNum;
    }

    public void setTextSize(float textSize) {
        mTextSize = textSize;
    }

    public void setTextSizeChoose(float textSizeChoose) {
        mTextSizeChoose = textSizeChoose;
    }

    public void setMiddleLineWidth(float middleLineWidth) {
        mMiddleLineWidth = middleLineWidth;
    }

    public void setBigScaleWidth(float bigScaleWidth) {
        mBigScaleWidth = bigScaleWidth;
    }

    public void setSmallScaleWidth(float smallScaleWidth) {
        mSmallScaleWidth = smallScaleWidth;
    }

    public void setToBottomHeight(float toBottomHeight) {
        mToBottomHeight = toBottomHeight;
    }

    public void setToLineTop(float toLineTop) {
        mToLineTop = toLineTop;
    }

    public void setSensitiveness(float sensitiveness) {
        mSensitiveness = sensitiveness;
    }
}
