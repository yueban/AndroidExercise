package com.yueban.motionlayoutdemo.sample;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.constraintlayout.motion.widget.MotionHelper;

public class FadeIn extends MotionHelper {
    public FadeIn(Context context) {
        super(context);
    }

    public FadeIn(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FadeIn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setProgress(View view, float progress) {
        view.setAlpha(progress);
    }
}