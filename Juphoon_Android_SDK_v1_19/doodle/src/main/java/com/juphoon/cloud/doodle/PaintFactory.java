package com.juphoon.cloud.doodle;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

public class PaintFactory {
    public static final int TYPE_BRUSH = 0;
    public static final int TYPE_ERASE = 1;

    public static Paint createPaint(int type) {
        Paint paint = new Paint();
        if (type == TYPE_BRUSH) {
            paint.setAntiAlias(true);
            paint.setDither(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);
        } else if (type == TYPE_ERASE) {
            paint.setAntiAlias(true);
            paint.setDither(true);
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        }
        return paint;
    }
}
