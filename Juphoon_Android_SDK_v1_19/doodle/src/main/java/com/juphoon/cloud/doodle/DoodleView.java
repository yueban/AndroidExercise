package com.juphoon.cloud.doodle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.view.View;
import android.view.WindowManager;

import com.juphoon.cloud.doodle.utils.DoodleUtils;

public class DoodleView extends View {

    private Bitmap mCacheBitmap;
    private Canvas mCacheCanvas;
    private PaintFlagsDrawFilter mDrawFilter;
    private WindowManager mWindowManager;
    private Matrix[] mMatrices;

    public DoodleView(Context context) {
        super(context);
    }

    public DoodleView(Context context, int width, int height) {
        super(context);
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

        mCacheBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        mCacheCanvas = new Canvas();
        mCacheCanvas.setDrawFilter(mDrawFilter);
        mCacheCanvas.setBitmap(mCacheBitmap);

        mWindowManager = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE));

        mMatrices = DoodleUtils.createRotationMatrixForRendering(mWindowManager);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(mDrawFilter);

        if (mCacheBitmap != null) {
            int rotation = mWindowManager.getDefaultDisplay().getRotation();
            canvas.drawBitmap(mCacheBitmap, mMatrices[rotation], null);
        }
    }

    public void drawImage(Bitmap bitmap, float x, float y, float width, float height, Matrix matrix) {
        if (width < 1 || height < 1) {
            mCacheCanvas.save();
            mCacheCanvas.setMatrix(matrix);
            mCacheCanvas.drawBitmap(bitmap, x, y, null);
            mCacheCanvas.restore();
        } else {
            mCacheCanvas.save();
            mCacheCanvas.setMatrix(matrix);
            mCacheCanvas.drawBitmap(bitmap, null, new RectF(x, y, x + width, y + height), null);
            mCacheCanvas.restore();
        }
        invalidate();
    }

    public void drawPath(Path path, Paint paint, Matrix matrix) {
        if (path != null && paint != null) {
            if (matrix != null) {
                mCacheCanvas.save();
                mCacheCanvas.setMatrix(matrix);
                mCacheCanvas.drawPath(path, paint);
                mCacheCanvas.restore();
            } else {
                mCacheCanvas.drawPath(path, paint);
            }
            postInvalidate();
        }
    }

    public void drawPoint(float x, float y, Paint paint, Matrix matrix) {
        if (paint != null && x >= 0 && y >= 0) {
            if (matrix != null) {
                mCacheCanvas.save();
                mCacheCanvas.setMatrix(matrix);
                mCacheCanvas.drawPoint(x, y, paint);
                mCacheCanvas.restore();
            } else {
                mCacheCanvas.drawPoint(x, y, paint);
            }
            postInvalidate();
        }
    }

    public void clean() {
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mCacheCanvas.drawPaint(paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        invalidate();
    }

    public Bitmap getCachedBitmap() {
        return mCacheBitmap;
    }
}
