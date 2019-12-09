package com.juphoon.cloud.doodle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;


public class DotView extends AppCompatImageView {
    private Paint mPaint = new Paint();
    private int mRadius;

    public DotView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DotView(Context context, int color, int radius) {
        super(context);
        setup(color, radius);
    }

    public void setup(int color, int radius) {
        mPaint.setColor(color);
        mRadius = radius;
        mPaint.setAntiAlias(true);
        setScaleType(ScaleType.CENTER_INSIDE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int max = Math.max(measuredWidth, measuredHeight);
        setMeasuredDimension(max, max);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadius, mPaint);
        super.draw(canvas);
    }

    public void changeColor(int color) {
        mPaint.setColor(color);
        invalidate();
    }
}
