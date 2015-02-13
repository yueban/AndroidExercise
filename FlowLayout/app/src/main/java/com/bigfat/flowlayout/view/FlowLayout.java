package com.bigfat.flowlayout.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/2/12
 */
public class FlowLayout extends ViewGroup {

    public static final String TAG = "FlowLayout";

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //MeasureSpec.EXACTLY测量模式
        int sizeWith = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        Log.i(TAG, "sizeWith--->" + String.valueOf(sizeWith));
        Log.i(TAG, "sizeHeight--->" + String.valueOf(sizeHeight));
//        Log.i(TAG, String.valueOf(modeWidth == MeasureSpec.EXACTLY));

        //MeasureSpec.AT_MOST测量模式，即宽/高设置为wrap_content时
        int width = 0;
        int height = 0;
        //记录每一行的宽度与高度
        int lineWidth = 0;
        int lineHeight = 0;
        //得到内部元素的个数
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            //得到子View
            View child = getChildAt(i);
            //测量子View
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            //得到LayoutParams，这里之所以是MarginLayoutParams，是因为在generateLayoutParams方法中返回的是MarginLayoutParams
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            //子View占据的宽度
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            //子View占据的高度
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            //如果改行已有控件占满一行，则换行
            if (lineWidth + childWidth > sizeWith - getPaddingLeft() - getPaddingRight()) {
                //取最宽的一行的宽度作为FlowLayout的宽度
                width = Math.max(width, lineWidth);
                //高度叠加
                height += lineHeight;

                //重置新的一行的行宽，行高
                lineWidth = childWidth;
                lineHeight = childHeight;
            } else {//不换行
                //宽度叠加
                lineWidth += childWidth;
                //高度取最大值
                lineHeight = Math.max(lineHeight, childHeight);
            }
            //如果是最后一个控件，要记录行宽与行高，因为不论换不换行，都会漏记/不对比最后一个控件的宽高
            if (i == cCount - 1) {
                width = Math.max(lineWidth, childWidth);
                height += lineHeight;
            }
        }
        //根据测量模式设置宽高
        setMeasuredDimension(modeWidth == MeasureSpec.AT_MOST ? width + getPaddingLeft() + getPaddingRight() : sizeWith,
                modeHeight == MeasureSpec.AT_MOST ? height + getPaddingTop() + getPaddingBottom() : sizeHeight);
    }

    /**
     * 用于一行一行的存储所有的View
     */
    private List<List<View>> mAllViews = new ArrayList<>();
    /**
     * 每一行的高度
     */
    private List<Integer> mLineHeight = new ArrayList<>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //onLayout可能会被多次调用，所以先清空List
        mAllViews.clear();
        mLineHeight.clear();

        int width = getWidth();
        int cCount = getChildCount();
        //每行子View
        List<View> lineViews = new ArrayList<>();
        //每行宽高
        int lineWidth = 0;
        int lineHeight = 0;
        //遍历子View
        for (int i = 0; i < cCount; i++) {
            //取得子View，及相关属性
            View child = getChildAt(i);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            //如果需要换行
            if (lineWidth + childWidth + lp.leftMargin + lp.rightMargin > width - getPaddingLeft() - getPaddingRight()) {
                //记录当前行数据
                mLineHeight.add(lineHeight);
                mAllViews.add(lineViews);
                //重置行数据
                lineWidth = 0;
                lineHeight = 0;
                lineViews = new ArrayList<>();
            }
            //计算当前行宽高
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
            //添加当前子View至当前行子View列表中
            lineViews.add(child);
        }
        //记录最后一行数据
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);

        //设置子View的位置
        int left = getPaddingLeft();
        int top = getPaddingTop();
        //行数
        int lineNum = mAllViews.size();
        //遍历每行
        for (int i = 0; i < lineNum; i++) {
            //获取当前行所有View
            lineViews = mAllViews.get(i);
            lineHeight = mLineHeight.get(i);
            //遍历当前行View
            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                //判断View显示状态
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int cLeft = left + lp.leftMargin;
                int cTop = top + lp.topMargin;
                int cRight = cLeft + child.getMeasuredWidth();
                int cBottom = cTop + child.getMeasuredHeight();
                //布局
                child.layout(cLeft, cTop, cRight, cBottom);

                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            }
            //每行结束后，左边距清空，高度叠加
            left = getPaddingLeft();
            top += lineHeight;
        }
    }

    /**
     * 与当前ViewGroup对应的LayoutParams
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
