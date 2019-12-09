package com.juphoon.cloud.doodle.utils;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;

public class DoodleUtils {

    public static DisplayMetrics getRealDisplayMetrics(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            if (hasJellyBeanMr1()) {
                wm.getDefaultDisplay().getRealMetrics(metrics);
            } else {
                wm.getDefaultDisplay().getMetrics(metrics);
            }
        }
        return metrics;
    }

    public static Matrix[] createRotationMatrixForRendering(WindowManager windowManager) {
        DisplayMetrics metrics = new DisplayMetrics();
        if (hasJellyBeanMr1()) {
            windowManager.getDefaultDisplay().getRealMetrics(metrics);
        } else {
            windowManager.getDefaultDisplay().getMetrics(metrics);
        }
        int smaller = Math.min(metrics.widthPixels, metrics.heightPixels);
        int larger = Math.max(metrics.widthPixels, metrics.heightPixels);

        Matrix[] matrices = new Matrix[4];
        matrices[Surface.ROTATION_0] = new Matrix();

        matrices[Surface.ROTATION_90] = new Matrix();
        matrices[Surface.ROTATION_90].postRotate(-90);
        matrices[Surface.ROTATION_90].postTranslate(0, smaller);

        matrices[Surface.ROTATION_180] = new Matrix();
        matrices[Surface.ROTATION_180].postRotate(180);
        matrices[Surface.ROTATION_180].postTranslate(smaller, larger);

        matrices[Surface.ROTATION_270] = new Matrix();
        matrices[Surface.ROTATION_270].postRotate(90);
        matrices[Surface.ROTATION_270].postTranslate(larger, 0);
        return matrices;
    }


    public static Matrix[] createRotationMatrixForInputting(WindowManager windowManager) {
        DisplayMetrics metrics = new DisplayMetrics();
        if (hasJellyBeanMr1()) {
            windowManager.getDefaultDisplay().getRealMetrics(metrics);
        } else {
            windowManager.getDefaultDisplay().getMetrics(metrics);
        }
        int smaller = Math.min(metrics.widthPixels, metrics.heightPixels);
        int larger = Math.max(metrics.widthPixels, metrics.heightPixels);

        Matrix[] matrices = new Matrix[4];
        matrices[Surface.ROTATION_0] = new Matrix();

        matrices[Surface.ROTATION_90] = new Matrix();
        matrices[Surface.ROTATION_90].postTranslate(0, -smaller);
        matrices[Surface.ROTATION_90].postRotate(90);

        matrices[Surface.ROTATION_180] = new Matrix();
        matrices[Surface.ROTATION_180].postTranslate(-smaller, -larger);
        matrices[Surface.ROTATION_180].postRotate(180);

        matrices[Surface.ROTATION_270] = new Matrix();
        matrices[Surface.ROTATION_270].postTranslate(-larger, 0);
        matrices[Surface.ROTATION_270].postRotate(-90);
        return matrices;
    }

    public static Matrix createRotationMatrixForBitmap(int orientation, int width, int height) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case Surface.ROTATION_90:
                matrix.postTranslate(width, 0);
                matrix.postRotate(90);
                break;
            case Surface.ROTATION_180:
                matrix.postTranslate(width, height);
                matrix.postRotate(180);
                break;
            case Surface.ROTATION_270:
                matrix.postTranslate(0, height);
                matrix.postRotate(-90);
                break;
        }
        return matrix;
    }

    public static Point getRelativePosition(ViewGroup ancestor, View child) {
        ViewParent p = child.getParent();
        int left = child.getLeft();
        int top = child.getTop();
        while (p != ancestor && p instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) p;
            left += group.getLeft();
            top += group.getTop();
            p = p.getParent();
        }
        return new Point(left, top);
    }

    private static boolean hasJellyBeanMr1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }
}
