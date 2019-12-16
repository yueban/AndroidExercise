package com.yueban.accessibilitydemo;

import android.view.MotionEvent;

/**
 * 炫富窗是否打开的回调
 *
 * @author zongyujie
 */
public interface FloatWindowCallback {
    void onDrag(MotionEvent event);

    void onDragEnd();
}

