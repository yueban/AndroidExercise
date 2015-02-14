package com.bigfat.guaguaka.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.bigfat.guaguaka.R;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/2/14
 */
public class GuaGuaKa extends View {

    //刮刮卡覆盖层
    private Paint mOuterPaint;
    private Path mPath;
    private Canvas mCanvas;
    private Bitmap mBitmap;

    //用户手指触摸的最后位置（即当前位置）
    private int mLastX;
    private int mLastY;

    //刮刮卡真实内容
    private Bitmap bitmap;

    public GuaGuaKa(Context context) {
        this(context, null);
    }

    public GuaGuaKa(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuaGuaKa(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        //初始化画布
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(0xffc0c0c0);
        //设置画笔属性
        setOuterPaint();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                mPath.moveTo(mLastX, mLastY);
                break;

            case MotionEvent.ACTION_MOVE:
                int dx = Math.abs(x - mLastX);
                int dy = Math.abs(y - mLastY);

                if (dx > 3 || dy > 3) {
                    mPath.lineTo(x, y);
                }
                mLastX = x;
                mLastY = y;
                break;

            case MotionEvent.ACTION_UP:

                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, null);
        drawPath();
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    private void drawPath() {
        mOuterPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mCanvas.drawPath(mPath, mOuterPaint);
    }

    /**
     * 设置画笔属性
     */
    private void setOuterPaint() {
        mOuterPaint.setColor(Color.RED);
        mOuterPaint.setAntiAlias(true);
        mOuterPaint.setDither(true);
        mOuterPaint.setStrokeJoin(Paint.Join.ROUND);
        mOuterPaint.setStrokeCap(Paint.Cap.ROUND);
        mOuterPaint.setStyle(Paint.Style.STROKE);
        mOuterPaint.setStrokeWidth(20);
    }

    private void init() {
        mOuterPaint = new Paint();
        mPath = new Path();
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.t2);
    }
}
