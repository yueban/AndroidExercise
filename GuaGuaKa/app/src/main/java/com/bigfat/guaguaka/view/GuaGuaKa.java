package com.bigfat.guaguaka.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.bigfat.guaguaka.R;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/2/14
 */
public class GuaGuaKa extends View {

    /**
     * 刮刮卡刮完的回调
     */
    public interface OnGuaGuaKaCompleteListener {
        void onComplete();
    }

    private OnGuaGuaKaCompleteListener onGuaGuaKaCompleteListener;

    //刮刮卡覆盖层
    private Paint mOuterPaint;
    private Path mPath;
    private Canvas mCanvas;
    private Bitmap mOuterBitmap;//”刮刮乐“封面
    private Bitmap mBitmap;//整个覆盖层

    //用户手指触摸的最后位置（即当前位置）
    private int mLastX;
    private int mLastY;

    //刮刮卡真实内容
    //刮奖文本
    private String mText;
    private int mTextSize;
    private int mTextColor;
    private Paint mTextPaint;
    //记录刮奖文本信息的宽高
    private Rect mTextBound;

    //是否完成刮奖，即显示刮奖结果
    private volatile boolean mIsComplete;

    private int mCornerRadius = 30;

    //计算刮开区域
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            int w = getWidth();
            int h = getHeight();

            float wipeArea = 0;
            float totalArea = w * h;
            //获得Bitmap上所有像素
            int[] mPixels = new int[w * h];
            mBitmap.getPixels(mPixels, 0, w, 0, 0, w, h);
            //计算已擦除像素
            for (int i = 0; i < w; i++) {
                for (int j = 0; j < h; j++) {
                    int index = i + j * w;
                    if (mPixels[index] == 0) {
                        wipeArea++;
                    }
                }
            }
            //计算擦除/刮开百分比
            if (wipeArea > 0 && totalArea > 0) {
                int percent = (int) (wipeArea / totalArea * 100);
                Log.i("TAG", "percent--->" + percent);
                //如果刮开区域超过60%，则直接显示全部
                if (percent > 60) {
                    mIsComplete = true;
                    postInvalidate();
                }
            }
        }
    };

    public GuaGuaKa(Context context) {
        this(context, null);
    }

    public GuaGuaKa(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuaGuaKa(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GuaGuaKa, defStyleAttr, 0);
        int count = a.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.GuaGuaKa_text:
                    mText = a.getString(attr);
                    break;

                case R.styleable.GuaGuaKa_textColor:
                    mTextColor = a.getColor(attr, Color.DKGRAY);
                    break;

                case R.styleable.GuaGuaKa_textSize:
                    mTextSize = (int) a.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 24, getResources().getDisplayMetrics()));
                    break;
            }
        }

        a.recycle();
    }

    public void setText(String text) {
        this.mText = text;
    }

    public void setOnGuaGuaKaCompleteListener(OnGuaGuaKaCompleteListener onGuaGuaKaCompleteListener) {
        this.onGuaGuaKaCompleteListener = onGuaGuaKaCompleteListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        //初始化画布
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
//        mCanvas.drawColor(0xffc0c0c0);
        mCanvas.drawRoundRect(new RectF(0, 0, width, height), mCornerRadius, mCornerRadius, mOuterPaint);
        mCanvas.drawBitmap(mOuterBitmap, null, new RectF(0, 0, width, height), null);
        //设置画笔属性
        setOuterPaint();
        setTextPaint();
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
                new Thread(mRunnable).start();
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawBitmap(bitmap, 0, 0, null);
        //刮奖信息
        canvas.drawText(mText, getWidth() / 2 - mTextBound.width() / 2, getHeight() / 2 + mTextBound.height() / 2, mTextPaint);
        if (mIsComplete) {
            if (onGuaGuaKaCompleteListener != null) {
                onGuaGuaKaCompleteListener.onComplete();
            }
        } else {
            //刮开区域
            drawPath();
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
    }

    /**
     * 绘制覆盖层刮开的路径
     */
    private void drawPath() {
        mOuterPaint.setStyle(Paint.Style.STROKE);
        mOuterPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mCanvas.drawPath(mPath, mOuterPaint);
    }

    private void init() {
        mOuterPaint = new Paint();
        mPath = new Path();
        mOuterBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.fg_guaguaka);

        mTextPaint = new Paint();
        mTextBound = new Rect();

        mText = "谢谢惠顾";
        mTextColor = Color.DKGRAY;
        mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 24, getResources().getDisplayMetrics());
    }

    /**
     * 设置覆盖层画笔属性
     */
    private void setOuterPaint() {
        mOuterPaint.setColor(0xffc0c0c0);
        mOuterPaint.setAntiAlias(true);
        mOuterPaint.setDither(true);
        mOuterPaint.setStrokeJoin(Paint.Join.ROUND);
        mOuterPaint.setStrokeCap(Paint.Cap.ROUND);
        mOuterPaint.setStyle(Paint.Style.FILL);
        mOuterPaint.setStrokeWidth(36);
    }

    /**
     * 设置刮奖信息画笔属性
     */
    private void setTextPaint() {
        mTextPaint.setColor(mTextColor);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(mTextSize);
        //获得当前画笔绘制文本的宽高
        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
    }
}
