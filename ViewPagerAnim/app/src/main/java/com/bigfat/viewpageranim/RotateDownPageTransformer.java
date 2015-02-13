package com.bigfat.viewpageranim;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/2/13
 */
public class RotateDownPageTransformer implements ViewPager.PageTransformer {
    private static final float MAX_ROTATE = 20f;

    @Override
    public void transformPage(View view, float position) {
        //A页角度变化：0~-20；B页角度变化20~0
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            ViewHelper.setRotation(view, 0);
        } else if (position <= 0) { // [-1,0]
            ViewHelper.setPivotX(view, view.getWidth() / 2);
            ViewHelper.setPivotY(view, view.getMeasuredHeight());
            ViewHelper.setRotation(view, 20 * position);
        } else if (position <= 1) { // (0,1]
            ViewHelper.setPivotX(view, view.getWidth() / 2);
            ViewHelper.setPivotY(view, view.getMeasuredHeight());
            ViewHelper.setRotation(view, 20 * position);
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            ViewHelper.setRotation(view, 0);
        }
    }
}
