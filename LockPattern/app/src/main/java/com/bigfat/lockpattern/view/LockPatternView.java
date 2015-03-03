package com.bigfat.lockpattern.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.bigfat.lockpattern.model.Point;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/3/3
 */
public class LockPatternView extends View {
    private static final String TAG = "LockPatternView";
    /**
     * 画笔
     */
    private Paint mPaint;
    private Point[][] points = new Point[3][3];
    private boolean mIsInit;
    /**
     * 解锁区域正方形的边长
     */
    private int mWidth;
    /**
     * 点阵的X轴偏移量
     */
    private int offsetX;
    /**
     * 点阵的Y轴偏移量
     */
    private int offsetY;
    /**
     * 点的半径
     */
    private int mPointRadius;

    public LockPatternView(Context context) {
        super(context);
    }

    public LockPatternView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LockPatternView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!mIsInit) {
            initPaint();
            initPoints();
        }
        points2Canvas(canvas);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * 初始化点
     */
    private void initPoints() {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        mWidth = Math.min(width, height);
        mPointRadius = mWidth / 12;
        if (width > height) {
            offsetX = (width - height) / 2;
        } else {
            offsetY = (height - width) / 2;
        }
//        Log.i(TAG, "width:" + width + "height:" + height + "offsetX:" + offsetX + "offsetY:" + offsetY);
        //初始化点
        int period = mWidth / 4;
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                points[x][y] = new Point(offsetX + period * (y + 1), offsetY + period * (x + 1));
            }
        }

        mIsInit = true;
    }

    /**
     * 将点绘制到画布上
     */
    private void points2Canvas(Canvas canvas) {
        for (Point[] pointArray : points) {
            for (Point point : pointArray) {
                switch (point.getState()) {
                    case NORMAL:
                        mPaint.setColor(0xff0000ff);
                        canvas.drawCircle(point.getX(), point.getY(), mPointRadius, mPaint);
                        break;
                    case PRESSED:
                        mPaint.setColor(0xff00ff00);
                        canvas.drawCircle(point.getX(), point.getY(), mPointRadius, mPaint);
                        break;
                    case ERROR:
                        mPaint.setColor(0xffff0000);
                        canvas.drawCircle(point.getX(), point.getY(), mPointRadius, mPaint);
                        break;
                }
            }
        }
    }
}
