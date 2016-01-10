package com.bigfat.circularprogress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by yueban on 8/11/15.
 */
public class CircularProgressIndicator extends View {
    private static int startDegree = 90;
    private Paint pointPaint = new Paint();
    private int pointSpace;//标度长方形的间隔
    private int pointLength;//标度长方形的长度
    private int progress = 30;

    public CircularProgressIndicator(Context context) {
        this(context, null);
    }

    public CircularProgressIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularProgressIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        pointPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
        pointSpace = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        pointLength = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (startDegree < 0) {
            startDegree = 45;
        }
        canvas.translate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        //calculate
        float radius = getMeasuredWidth() / 2;
        int count = (int) ((2 * Math.PI * radius) * (360 - startDegree * 2) / 360 / pointSpace);
        float degreeSpace = (float) ((360 - startDegree * 2) * 1.0 / count);
        canvas.rotate(180 + startDegree, 0, 0);
        //draw
        boolean isIndicatorDrawn = false;
        for (int i = 0; i < count; i++) {
            if (i * 1.0 / count < progress * 1.0 / 100) {
                pointPaint.setColor(Color.BLUE);
            } else {
                if (!isIndicatorDrawn) {
                    pointPaint.setColor(Color.YELLOW);
                    canvas.drawLine(0, -radius, 0, -radius + pointLength * 5, pointPaint);
                    canvas.rotate(degreeSpace, 0, 0);
                    isIndicatorDrawn = true;
                    continue;
                }
                pointPaint.setColor(Color.GRAY);
            }
            canvas.drawLine(0, -radius, 0, -radius + pointLength, pointPaint);
            canvas.rotate(degreeSpace, 0, 0);
        }
        canvas.rotate(startDegree, 0, 0);
    }
}