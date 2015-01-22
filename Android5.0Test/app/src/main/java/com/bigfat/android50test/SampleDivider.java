package com.bigfat.android50test;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/1/21
 */
public class SampleDivider extends RecyclerView.ItemDecoration {

    //默认分隔条Drawable样式的ID
    private static final int[] ATTRS = {android.R.attr.listDivider};
    //分隔条Drawable对象
    private Drawable mDivider;

    public SampleDivider(Context context) {
        TypedArray a = context.obtainStyledAttributes(ATTRS);
        //获取系统分隔条Drawable对象
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    //绘制所有列表项的分隔条
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        //左边缘坐标
        int left = parent.getPaddingLeft();
        //右边缘坐标
        int right = parent.getWidth() - parent.getPaddingRight();
        //获取列表项总数
        int childCount = parent.getChildCount();

        //绘制所有列表项的分割线
        for (int i = 0; i < childCount; i++) {
            //获得当前的列表项
            View child = parent.getChildAt(i);
            //获取当前列表项的布局参数信息
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            //计算分隔条左上角的纵坐标
            int top = child.getBottom() + params.bottomMargin;
            //计算分隔条右下角的纵坐标
            int bottom = top + mDivider.getIntrinsicHeight();
            //设置分隔条绘制的位置
            mDivider.setBounds(left, top, right, bottom);
            //将分隔条绘制在Canvas上
            mDivider.draw(c);
        }
    }
}
