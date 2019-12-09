package com.juphoon.cloud.doodle;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

public class StrokeSeekBar extends RelativeLayout implements SeekBar.OnSeekBarChangeListener {

    public interface SeekListener {
        void onSeek(int value);
    }

    private static final int DEFAULT_COLOR = 0xff000000;
    private static final int DEFAULT_MIN_VALUE = 4;
    private static final int DEFAULT_MAX_VALUE = 48;

    private int mCurrentColor;
    private int mCurrentValue = DEFAULT_MIN_VALUE;
    private int mMinValue = DEFAULT_MIN_VALUE;
    private int mMaxValue = DEFAULT_MAX_VALUE;
    private SeekListener mSeekListener;
    private SeekBar mSeekBar;
    private ImageView mDotPreview;
    private ImageView mEraserPreview;

    private float mScreenDensity;

    public StrokeSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public StrokeSeekBar(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_stroke_seek_bar, this, true);
        mScreenDensity = context.getResources().getDisplayMetrics().density;

        mDotPreview = (ImageView) findViewById(R.id.stroke_seek_bar_preview);
        mEraserPreview = (ImageView) findViewById(R.id.eraser_stroke_seek_bar_preview);
        setPreviewLayoutParams(mDotPreview, mMinValue);
        setPreviewLayoutParams(mEraserPreview, mMinValue);
        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        mSeekBar.setProgress(mMinValue);
        mSeekBar.setMax(mMaxValue - mMinValue);
        mSeekBar.setOnSeekBarChangeListener(this);
        setColor(DEFAULT_COLOR);
    }

    public void setColor(int color) {
        if (mCurrentColor == color) {
            return;
        }
        mCurrentColor = color;
        Drawable drawable = getContext().getResources().getDrawable(R.drawable.stroke_seek_bar_preview).mutate();
        drawable.setColorFilter(mCurrentColor, PorterDuff.Mode.SRC_ATOP);
        mDotPreview.setBackground(drawable);
    }

    public void setEraserMode(boolean isEraser) {
        if (isEraser) {
            mDotPreview.setVisibility(INVISIBLE);
            mEraserPreview.setVisibility(VISIBLE);
        } else {
            mDotPreview.setVisibility(VISIBLE);
            mEraserPreview.setVisibility(INVISIBLE);
        }
    }

    public void seekTo(int value) {
        if (value > mMaxValue || value < mMinValue || value == mCurrentValue) {
            return;
        }
        mCurrentValue = value;
        mSeekBar.setProgress(mCurrentValue);
        setPreviewLayoutParams(mDotPreview, mCurrentValue);
        setPreviewLayoutParams(mEraserPreview, mCurrentValue);
        if (mSeekListener != null) {
            mSeekListener.onSeek(mCurrentValue);
        }
    }

    private int getStrokeWidthFromSelector(float value) {
        return (int) ((value / 2.0f) * mScreenDensity);
    }

    private void setPreviewLayoutParams(View view, int value) {
        view.setLayoutParams(new RelativeLayout.LayoutParams(
                getStrokeWidthFromSelector(value), getStrokeWidthFromSelector(value)));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int newValue = mMinValue + progress;
        setPreviewLayoutParams(mDotPreview, newValue);
        setPreviewLayoutParams(mEraserPreview, newValue);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mSeekListener != null) {
            mSeekListener.onSeek(seekBar.getProgress() + mMinValue);
        }
    }

    public void setSeekListener(SeekListener seekListener) {
        mSeekListener = seekListener;
    }
}
