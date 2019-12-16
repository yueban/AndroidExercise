package com.yueban.accessibilitydemo;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.yueban.accessibilitydemo.util.LogUtils;
import com.yueban.accessibilitydemo.util.ScreenUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author wenchao
 * Date 2019/8/26 15:32
 * Description
 */
public class AssistService extends AccessibilityService implements FloatWindowCallback {
    public static final int SCREEN_VERTICAL = 0;

    public static final int SCREEN_HORIZONTAL = 1;
    public static final String TAG = AssistService.class.getSimpleName();
    /**
     * 屏幕横竖屏状态 0:竖屏;1:横屏;
     */
    private int screenState = 0;

    private boolean isDragging;
    private List<AccessibilityNodeInfo> nodeInfos;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.d(TAG, "onCreate()");
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        LogUtils.d(TAG, "onServiceConnected()");
        LogUtils.d(TAG, "isShowing: " + AssistFloatWindow.getInstance().isShowing());
        if (!AssistFloatWindow.getInstance().isShowing()) {
            AssistFloatWindow.getInstance().open(MyApp.getContext(), this);
        }
    }

    private List<AccessibilityNodeInfo> toViewHeirarchy(AccessibilityNodeInfo nodeInfo) {
        List<AccessibilityNodeInfo> result = new ArrayList<>();
        visitViews(nodeInfo, result);
        return result;
    }

    private void visitViews(AccessibilityNodeInfo nodeInfo, List<AccessibilityNodeInfo> result) {
        for (int i = 0; i < nodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo childNode = nodeInfo.getChild(i);
            CharSequence text = childNode.getText();
            if (!TextUtils.isEmpty(text)) {
                result.add(childNode);
            }
            visitViews(childNode, result);
        }
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (TextUtils.isEmpty(event.getPackageName())) {
            return;
        }
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            isScreenChange();
            //监听横竖屏切换
            int width = ScreenUtil.getScreenWidthPixels(getApplicationContext());
            int height = ScreenUtil.getScreenHeightPixels(getApplicationContext());

            int tempState;
            if (width > height) {
                tempState = SCREEN_HORIZONTAL;
            } else {
                tempState = SCREEN_VERTICAL;
            }
            if (tempState == screenState) {
                return;
            }
            screenState = tempState;
            AssistFloatWindow.getInstance().updateLocation(getApplicationContext());
        }
    }

    public boolean isScreenChange() {
        //获取设置的配置信息
        Configuration mConfiguration = this.getResources().getConfiguration();
        //获取屏幕方向
        int ori = mConfiguration.orientation;

        if (ori == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏
            return true;
        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏
            return false;
        }
        return false;
    }

    @Override
    public void onInterrupt() {
        LogUtils.d(TAG, "onInterrupt()");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtils.d(TAG, "onUnbind()");
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d(TAG, "onDestroy()");

        AssistFloatWindow.getInstance().dismiss(MyApp.getContext());
        AssistFloatWindow.getInstance().dismiss(MyApp.getContext(), AssistFloatWindow.FLOAT_WINDOW_ENCRYPT);
    }

    @Override
    public void onDrag(MotionEvent event) {
        LogUtils.d(TAG, "onDrag()-->\tx:" + event.getRawX() + "\ty:" + event.getRawY());

        if (!isDragging) {
            parseRootNode();
            isDragging = true;
        }

        float x = event.getRawX();
        float y = event.getRawY();
        captureNode(x, y);
    }

    private void captureNode(float x, float y) {
        // TODO yueban: 获取 x,y 坐标点对应的 view
    }

    @Override
    public void onDragEnd() {
        LogUtils.d(TAG, "onDragEnd()");

        isDragging = false;
    }

    private void parseRootNode() {
        nodeInfos = toViewHeirarchy(getRootInActiveWindow());
        Collections.reverse(nodeInfos);
    }
}
