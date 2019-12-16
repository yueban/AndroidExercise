package com.yueban.accessibilitydemo;

import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import com.lzf.easyfloat.EasyFloat;
import com.lzf.easyfloat.enums.ShowPattern;
import com.lzf.easyfloat.enums.SidePattern;
import com.lzf.easyfloat.interfaces.OnFloatCallbacks;
import com.lzf.easyfloat.utils.DisplayUtils;
import com.yueban.accessibilitydemo.util.LogUtils;
import com.yueban.accessibilitydemo.util.ScreenUtil;

/**
 * @author wenchao
 * Date 2019/8/26 10:30
 * Description 加密悬浮窗
 */
public class AssistFloatWindow {
    public static final String FLOAT_WINDOW_ENCRYPT = "AssistFloatWindow";
    private static AssistFloatWindow instance;
    private int floatWindowX;
    private int floatWindowY;

    private AssistFloatWindow() {
        floatWindowX = ScreenUtil.getScreenWidthPixels(MyApp.getContext()) -
                DisplayUtils.INSTANCE.dp2px(MyApp.getContext(), 30);
    }

    public static AssistFloatWindow getInstance() {
        if (instance == null) {
            synchronized (AssistFloatWindow.class) {
                if (instance == null) {
                    instance = new AssistFloatWindow();
                }
            }
        }
        return instance;
    }

    public void open(Context context, final FloatWindowCallback floatWindowCallback) {
        floatWindowY = ScreenUtil.getScreenHeightPixels(context) * 2 / 5;
        EasyFloat.with(MyApp.getContext())
                .setLayout(R.layout.layout_assist_float_window)
                .setShowPattern(ShowPattern.ALL_TIME)
                .setSidePattern(SidePattern.RESULT_RIGHT)
                .setGravity(Gravity.END, 0, floatWindowY)
                .setAppFloatAnimator(null)
                .registerCallbacks(new OnFloatCallbacks() {
                    @Override
                    public void createdResult(boolean isCreated, String msg, View view) {
                        LogUtils.d(AssistService.TAG, "createdResult--> isCreated:" + isCreated + "\tmsg:" + isCreated);
                    }

                    @Override
                    public void show(View view) {
                    }

                    @Override
                    public void hide(View view) {
                    }

                    @Override
                    public void dismiss() {
                    }

                    @Override
                    public void touchEvent(View view, MotionEvent event) {
                    }

                    @Override
                    public void drag(View view, MotionEvent event) {
                        if (floatWindowCallback != null) {
                            floatWindowCallback.onDrag(event);
                        }
                    }

                    @Override
                    public void dragEnd(View view) {
                        if (floatWindowCallback != null) {
                            floatWindowCallback.onDragEnd();
                        }

//                    View ivLogo = view.findViewById(R.id.ic_logo);
//                    int[] location = new int[2];
//                    ivLogo.getLocationOnScreen(location);
//                    int height = ivLogo.getHeight();
//                    floatWindowY = location[1] - height / 2;
                    }
                })
                .show();
    }


    public void show(Context context, String tag, int offsetX, int offsetY) {
        EasyFloat.showAppFloat(context, tag, offsetX, offsetY);
    }


    public void dismiss(Context context) {
        EasyFloat.dismissAppFloat(context, null);
    }

    public void dismiss(Context context, String tag) {
        EasyFloat.dismissAppFloat(context, tag);
    }

    public boolean isShowing() {
        return EasyFloat.appFloatIsShow(null);
    }

    public boolean isShowing(String tag) {
        return EasyFloat.appFloatIsShow(tag);
    }

    public void updateLocation(Context context) {
        if (isShowing()) {
            floatWindowX = ScreenUtil.getScreenWidthPixels(context) - DisplayUtils.INSTANCE.dp2px(context, 5);
            floatWindowY = floatWindowY * ScreenUtil.getScreenHeightPixels(context) / ScreenUtil.getScreenWidthPixels(context);
            show(context, null, floatWindowX, floatWindowY);
        }
        if (isShowing(FLOAT_WINDOW_ENCRYPT)) {
            floatWindowX = ScreenUtil.getScreenWidthPixels(context) - DisplayUtils.INSTANCE.dp2px(context, 5);
            floatWindowY = floatWindowY * ScreenUtil.getScreenHeightPixels(context) / ScreenUtil.getScreenWidthPixels(context);
            show(context, FLOAT_WINDOW_ENCRYPT, floatWindowX, floatWindowY);
        }
    }

}
