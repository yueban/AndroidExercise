package com.bigfat.lockpattern.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.bigfat.lockpattern.model.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/3/3
 */
public class LockPatternView extends View {

    private static final String TAG = "LockPatternView";

    public interface OnPatternChangeListener {
        void onPatternChange(String password);
    }

    /**
     * 密码选择结果回调接口
     */
    private OnPatternChangeListener mOnPatternChangeListener;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 点阵
     */
    private Point[][] points = new Point[3][3];
    /**
     * 按下的点的集合
     */
    private List<Point> mSelectedPoints = new ArrayList<>();
    /**
     * 是否初始化
     */
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
    /**
     * 触摸按下时是否直接按在一个点上，如果不是的话，将不进行之后移动的判断
     */
    private boolean mIsFirstMoveDownOnPoint;
    /**
     * 触摸移动的x轴坐标
     */
    private int movingX;
    /**
     * 触摸移动的y轴坐标
     */
    private int movingY;
    /**
     * 是否触摸时没有触摸到点
     */
    private boolean mIsMovingNoPoint;


    public LockPatternView(Context context) {
        super(context);
    }

    public LockPatternView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LockPatternView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setmOnPatternChangeListener(OnPatternChangeListener mOnPatternChangeListener) {
        this.mOnPatternChangeListener = mOnPatternChangeListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mIsMovingNoPoint = false;
        movingX = (int) event.getX();
        movingY = (int) event.getY();
        Point point = null;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: //移动开始
                resetPoints();
                postInvalidate();
                point = checkSelectPoint(movingX, movingY);
                if (point != null) {
                    mIsFirstMoveDownOnPoint = true;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (mIsFirstMoveDownOnPoint) {
                    point = checkSelectPoint(movingX, movingY);
                    if (point == null) {
                        mIsMovingNoPoint = true;
                    }
                }
                break;

            case MotionEvent.ACTION_UP: //移动结束
                StringBuilder password = new StringBuilder();
                if (mSelectedPoints.size() < 5) {//5个点以内长度不足，使用错误绘制
                    setSelectedPointError();
                } else {
                    for (Point p : mSelectedPoints) {
                        password.append(String.valueOf(p.getIndex()));
                    }
                }
                if (mOnPatternChangeListener != null) {
                    mOnPatternChangeListener.onPatternChange(password.toString());
                }
                mIsFirstMoveDownOnPoint = false;
                break;
        }
        //重复选中检查
        if (mIsFirstMoveDownOnPoint && point != null) {
            if (!isPointSelected(point)) {//没有被选中过的点，添加到选中队列中
                point.setState(Point.Status.PRESSED);
                mSelectedPoints.add(point);
            } else {//已经被选中过的点，当做没有点来处理
                mIsMovingNoPoint = true;
            }
        }
        //重绘View
        postInvalidate();
        return true;
    }

    /**
     * 重置点阵状态
     */
    private void resetPoints() {
        for (Point point : mSelectedPoints) {
            point.setState(Point.Status.NORMAL);
        }
        mSelectedPoints.clear();
    }

    /**
     * 判断点是否已被选中过
     */
    private boolean isPointSelected(Point point) {
        return mSelectedPoints.contains(point);
    }

    /**
     * 将已选点设置为错误状态
     */
    private void setSelectedPointError() {
        for (Point point : mSelectedPoints) {
            point.setState(Point.Status.ERROR);
        }
    }

    /**
     * 获取触摸的点
     *
     * @param x 触摸的x坐标
     * @param y 触摸的y坐标
     * @return 触摸位置的点
     */
    private Point checkSelectPoint(float x, float y) {
        for (Point[] pointArray : points) {
            for (Point point : pointArray) {
                if (point.with(mPointRadius, x, y)) {
                    return point;
                }
            }
        }
        return null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!mIsInit) {
            initPaint();
            initPoints();
        }
        points2Canvas(canvas);
        if (mSelectedPoints.size() > 0) {
            for (int i = 1; i < mSelectedPoints.size(); i++) {
                lines2Canvas(canvas, mSelectedPoints.get(i - 1), mSelectedPoints.get(i));
            }
            if (mIsMovingNoPoint) {
                lines2Canvas(canvas, mSelectedPoints.get(mSelectedPoints.size() - 1), new Point(movingX, movingY));
            }
        }
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(10);
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
                points[x][y].setIndex(x * 3 + y);
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

    /**
     * 画线
     *
     * @param canvas 画布
     * @param pointA 点A
     * @param pointB 点B
     */
    private void lines2Canvas(Canvas canvas, Point pointA, Point pointB) {
        switch (pointA.getState()) {
            case PRESSED:
                mPaint.setColor(0xff00ff00);
                canvas.drawLine(pointA.getX(), pointA.getY(), pointB.getX(), pointB.getY(), mPaint);
                break;
            case ERROR:
                mPaint.setColor(0xffff0000);
                canvas.drawLine(pointA.getX(), pointA.getY(), pointB.getX(), pointB.getY(), mPaint);
                break;
        }
    }
}
