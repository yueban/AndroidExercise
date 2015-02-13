package com.bigfat.viewpageranim;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/2/13
 */
public class ViewPagerWithTransformerAnim extends ViewPager {

    private View mLeft;
    private View mRight;

    private float mTrans;
    private float mScale;
    private float mAlpha;

    private static final float MIN_SCALE = 0.7f;
    private static final float MIN_ALPHA = 0.5f;

    private Map<Integer, View> mChildren = new HashMap<>();

    public ViewPagerWithTransformerAnim(Context context) {
        this(context, null);
    }

    public ViewPagerWithTransformerAnim(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setViewForPosition(View view, Integer position) {
        mChildren.put(position, view);
    }

    public void removeViewFromPosition(Integer position) {
        mChildren.remove(position);
    }

    @Override
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
//        Log.i("TAG", "position--->" + position + "\toffset--->" + offset + "\toffsetPixels--->" + offsetPixels);
//        Log.i("TAG", "getCurrentItem--->" + getCurrentItem());
        mLeft = mChildren.get(position);
        mRight = mChildren.get(position + 1);

        animStack(mLeft, mRight, offset, offsetPixels);
        super.onPageScrolled(position, offset, offsetPixels);
    }

    /**
     * 设置左右Page动画
     *
     * @param left         左边View
     * @param right        右边View
     * @param offset       偏移百分比（0~1）
     * @param offsetPixels 偏移像素值
     */
    private void animStack(View left, View right, float offset, int offsetPixels) {
        if (right != null) {
            mTrans = -getWidth() - getPageMargin() + offsetPixels;
            mScale = MIN_SCALE + (1 - MIN_SCALE) * offset;
            mAlpha = MIN_ALPHA + (1 - MIN_ALPHA) * offset;

            ViewHelper.setTranslationX(right, mTrans);
            ViewHelper.setScaleX(right, mScale);
            ViewHelper.setScaleY(right, mScale);
            ViewHelper.setAlpha(right, mAlpha);
        }
        //左边在上方
        if (left != null) {
            left.bringToFront();
        }
//        //设置左边View动画
//        ViewHelper.setPivotX(mLeft, mLeft.getWidth() / 2);
//        ViewHelper.setPivotY(mLeft, mLeft.getMeasuredHeight());
//        ViewHelper.setRotation(mLeft, -20 * (1 - offset));
//        //设置右边View动画
//        ViewHelper.setPivotX(mRight, mRight.getWidth() / 2);
//        ViewHelper.setPivotY(mRight, mRight.getMeasuredHeight());
//        ViewHelper.setRotation(mRight, 20 * offset);
    }
}
