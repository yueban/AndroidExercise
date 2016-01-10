package com.bigfat.viewpagerinscrollviewdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends FrameLayout {
    public static final String TAG = "FlowLayout";
    public final int LAYOUT_DIRECTION_LTR = 0;
    public final int LAYOUT_DIRECTION_RTL = 1;
    private int layoutDirection;
    /**
     * 用于一行一行的存储所有的View
     */
    private List<List<View>> mAllViews = new ArrayList<>();
    /**
     * 每一行的高度
     */
    private List<Integer> mLineHeight = new ArrayList<>();

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        this.layoutDirection = a.getInt(R.styleable.FlowLayout_layout_direction, LAYOUT_DIRECTION_LTR);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //MeasureSpec.EXACTLY测量模式
//        int sizeWith = MeasureSpec.getSize(widthMeasureSpec);
//        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
//        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
//        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
////        Log.i(TAG, "sizeWith--->" + String.valueOf(sizeWith));
////        Log.i(TAG, "sizeHeight--->" + String.valueOf(sizeHeight));
////        Log.i(TAG, String.valueOf(modeWidth == MeasureSpec.EXACTLY));
//
//        //MeasureSpec.AT_MOST测量模式，即宽/高设置为wrap_content时
//        int width = 0;
//        int height = 0;
//        //记录每一行的宽度与高度
//        int lineWidth = 0;
//        int lineHeight = 0;
//        //得到内部元素的个数
//        int cCount = getChildCount();
//        for (int i = 0; i < cCount; i++) {
//            //得到子View
//            View child = getChildAt(i);
//            if (child.getVisibility() == GONE) {
//                continue;
//            }
//            //测量子View
//            measureChild(child, widthMeasureSpec, heightMeasureSpec);
//            //得到LayoutParams，这里之所以是MarginLayoutParams，是因为在generateLayoutParams方法中返回的是MarginLayoutParams
//            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
//            //子View占据的宽度
//            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
//            //子View占据的高度
//            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
//            //如果该行已有控件占满一行，则换行
//            if (lineWidth + childWidth > sizeWith - getPaddingLeft() - getPaddingRight()) {
//                //取最宽的一行的宽度作为FlowLayout的宽度
//                width = Math.max(width, lineWidth);
//                //高度叠加
//                height += lineHeight;
//
//                //重置新的一行的行宽，行高
//                lineWidth = childWidth;
//                lineHeight = childHeight;
//            } else {//不换行
//                //宽度叠加
//                lineWidth += childWidth;
//                //高度取最大值
//                lineHeight = Math.max(lineHeight, childHeight);
//            }
//            //如果是最后一个控件，要记录行宽与行高，因为不论换不换行，都会漏记/不对比最后一个控件的宽高
//            if (i == cCount - 1) {
//                width = Math.max(lineWidth, childWidth);
//                height += lineHeight;
//            }
//        }
//        //根据测量模式设置宽高
//        setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWith : width + getPaddingLeft() + getPaddingRight(),
//                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height + getPaddingTop() + getPaddingBottom());

        if (getChildCount() > 0) {
            final View child = getChildAt(0);
            int height = getMeasuredHeight();
            if (child.getMeasuredHeight() < height) {
                final FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();

                int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                        getPaddingLeft() + getPaddingRight(), lp.width);
                height -= getPaddingTop();
                height -= getPaddingBottom();
                int childHeightMeasureSpec =
                        MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }

    }

    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();

        int childWidthMeasureSpec;
        int childHeightMeasureSpec;

        childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, getPaddingLeft()
                + getPaddingRight(), lp.width);

        childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed,
                                           int parentHeightMeasureSpec, int heightUsed) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin
                        + widthUsed, lp.width);
        final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                lp.topMargin + lp.bottomMargin, MeasureSpec.UNSPECIFIED);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed,l,t,r,b);
//        //onLayout可能会被多次调用，所以先清空List
//        mAllViews.clear();
//        mLineHeight.clear();
//
//        int width = getWidth();
//        int cCount = getChildCount();
//        //每行子View
//        List<View> lineViews = new ArrayList<>();
//        //每行宽高
//        int lineWidth = 0;
//        int lineHeight = 0;
//        //遍历子View
//        for (int i = 0; i < cCount; i++) {
//            //取得子View，及相关属性
//            View child = getChildAt(i);
//            if (child.getVisibility() == GONE) {
//                continue;
//            }
//            int childWidth = child.getMeasuredWidth();
//            int childHeight = child.getMeasuredHeight();
//            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
//            //如果需要换行
//            if (lineWidth + childWidth + lp.leftMargin + lp.rightMargin > width - getPaddingLeft() - getPaddingRight()) {
//                //记录当前行数据
//                mLineHeight.add(lineHeight);
//                mAllViews.add(lineViews);
//                //重置行数据
//                lineWidth = 0;
//                lineHeight = 0;
//                lineViews = new ArrayList<>();
//            }
//            //计算当前行宽高
//            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
//            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
//            //添加当前子View至当前行子View列表中
//            lineViews.add(child);
//        }
//        //记录最后一行数据
//        mLineHeight.add(lineHeight);
//        mAllViews.add(lineViews);
//
//
//        int top = getPaddingTop();
//        //行数
//        int lineNum = mAllViews.size();
//        //遍历每行
//        for (int i = 0; i < lineNum; i++) {
//            int left = getPaddingLeft();
//            int right = getMeasuredWidth() - getPaddingRight();
//            //获取当前行所有View
//            lineViews = mAllViews.get(i);
//            lineHeight = mLineHeight.get(i);
//            //遍历当前行View
//            for (int j = 0; j < lineViews.size(); j++) {
//                View child = lineViews.get(j);
//                //判断View显示状态
//                if (child.getVisibility() == View.GONE) {
//                    continue;
//                }
//                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
//                //设置子View的位置
//                int cLeft = 0;
//                int cTop = 0;
//                int cRight = 0;
//                int cBottom = 0;
//                cTop = top + lp.topMargin;
//                cBottom = cTop + child.getMeasuredHeight();
//                switch (layoutDirection) {
//                    case LAYOUT_DIRECTION_LTR://从左向右
//                        cLeft = left + lp.leftMargin;
//                        cRight = cLeft + child.getMeasuredWidth();
//
//                        left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
//                        break;
//
//                    case LAYOUT_DIRECTION_RTL://从右向左
//                        cRight = right - lp.rightMargin;
//                        cLeft = cRight - child.getMeasuredWidth();
//
//                        right -= child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
//                        break;
//                }
//                //布局
//                child.layout(cLeft, cTop, cRight, cBottom);
//            }
//            top += lineHeight;
//        }
    }

    // 继承自margin，支持子视图android:layout_margin属性
    public static class LayoutParams extends MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }
    }
}
