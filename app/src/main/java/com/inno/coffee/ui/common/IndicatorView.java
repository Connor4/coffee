package com.inno.coffee.ui.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.inno.coffee.R;

import java.util.ArrayList;
import java.util.List;

public class IndicatorView extends View {
    private static final String TAG = "IndicatorView";
    private Paint mIndicatorPaint;
    private Paint mBackgroundPaint;
    private final RectF mViewRect = new RectF();
    private final RectF mIndicatorRect = new RectF();
    private int mIndicatorWidth;
    private int mIndicatorHeight;
    private int mIndicatorColor;
    private int mBackgroundColor;
    private int mIndicatorRadius = 20;
    private int mCount = 1;

    private List<Integer> mPositionList = new ArrayList<>(); // 锚点位置
    private float mLastPositionOffsetSum; // 用来判断左滑右滑
    private IndicatorOnPageChangeListener mPageChangeListener = new IndicatorOnPageChangeListener();

    public IndicatorView(Context context) {
        this(context, null);
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView, defStyleAttr, 0);
        mIndicatorWidth = typedArray.getDimensionPixelSize(R.styleable.IndicatorView_indicatorWidth, 200);
        mIndicatorHeight = typedArray.getDimensionPixelSize(R.styleable.IndicatorView_indicatorHeight, 4);
        mIndicatorColor = typedArray.getColor(R.styleable.IndicatorView_indicatorColor, Color.TRANSPARENT);
        mBackgroundColor = typedArray.getColor(R.styleable.IndicatorView_backgroundColor, Color.TRANSPARENT);
        typedArray.recycle();
        init();
    }

    private void init() {
        mIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIndicatorPaint.setColor(mIndicatorColor);
        mIndicatorPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(mBackgroundColor);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        mViewRect.set(0, 1, viewWidth, mIndicatorHeight);

        mIndicatorWidth = viewWidth / mCount;
        mIndicatorRect.set(0, 0, mIndicatorWidth, mIndicatorHeight);

        for (int i = 0; i < mCount; i++) {
            mPositionList.add(i * mIndicatorWidth);
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(mViewRect, mIndicatorRadius, mIndicatorRadius, mBackgroundPaint);
        canvas.drawRoundRect(mIndicatorRect, mIndicatorRadius, mIndicatorRadius, mIndicatorPaint);
    }

    /**
     * 与viewpager设置关联
     */
    public void setupWithViewPager(ViewPager viewPager) {
        if (viewPager != null) {
            viewPager.addOnPageChangeListener(mPageChangeListener);
        }
    }

    /**
     * 直接选中某个位置
     */
    public void setSelectPosition(int index) {
        if (index < 0 || index > mPositionList.size()) {
            return;
        }
        int start = mPositionList.get(index);
        mIndicatorRect.set(start, 0, mIndicatorWidth + start, mIndicatorHeight);
        invalidate();
    }

    public void setIndicatorCount(int count) {
        if (count <= 1) {
            return;
        }
        setVisibility(View.VISIBLE);
        mCount = count;
        invalidate();
    }

    /**
     * 设置指示器移动
     *
     * @param position       与vp关联的位置
     * @param positionOffset vp的
     */
    public void setIndicatorMove(int position, float positionOffset) {
        float currentPositionOffsetSum = position + positionOffset;
        boolean leftToRight = mLastPositionOffsetSum <= currentPositionOffsetSum; // 从右往左
        // 从左往右
        if (mLastPositionOffsetSum == currentPositionOffsetSum) {
            return;
        }
        int currentPosition = getCurrentPosition(position, positionOffset, leftToRight);
        mIndicatorRect.set(currentPosition, 0, mIndicatorWidth + currentPosition, mIndicatorHeight);
        mLastPositionOffsetSum = currentPositionOffsetSum;
        invalidate();
    }

    private int getCurrentPosition(int position, float positionOffset, boolean leftToRight) {
        int currentPosition; // 当前移动位置
        int currentPositionStart; // 当前锚点
        if (leftToRight) {
            currentPositionStart = mPositionList.get(position);
            currentPosition = currentPositionStart + (int) (mIndicatorWidth * positionOffset);
        } else {
            // 右往左当前锚点往前走，position + 1 用它来容错的，都是用小于的
            // 右往左整个过程都是position一样的，不会变
            currentPositionStart = position + 1 > mPositionList.size() ? mPositionList.get(position) : mPositionList.get(position + 1);
            currentPosition = currentPositionStart - (int) (mIndicatorWidth * (1 - positionOffset));
        }
        return currentPosition;
    }

    private class IndicatorOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            setIndicatorMove(position, positionOffset);
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

}
