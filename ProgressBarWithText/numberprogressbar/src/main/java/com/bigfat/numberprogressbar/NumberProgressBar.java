package com.bigfat.numberprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

import static com.bigfat.numberprogressbar.NumberProgressBar.ProgressTextVisibility.Invisible;
import static com.bigfat.numberprogressbar.NumberProgressBar.ProgressTextVisibility.Visible;

public class NumberProgressBar extends View {
    /**
     * For save and restore instance of progressbar.
     */
    private static final String INSTANCE_STATE = "saved_instance";
    private static final String INSTANCE_TEXT_COLOR = "text_color";
    private static final String INSTANCE_TEXT_SIZE = "text_size";
    private static final String INSTANCE_BAR_HEIGHT = "bar_height";
    private static final String INSTANCE_PROGRESS_BAR_COLOR = "progress_bar_color";
    private static final String INSTANCE_SECONDARY_BAR_COLOR = "secondary_bar_color";
    private static final String INSTANCE_MAX = "max";
    private static final String INSTANCE_PROGRESS = "progress";
    private static final String INSTANCE_SUFFIX = "suffix";
    private static final String INSTANCE_PREFIX = "prefix";
    private static final String INSTANCE_TEXT_VISIBILITY = "text_visibility";
    private static final int PROGRESS_TEXT_VISIBLE = 0;
    private final int default_text_color = Color.rgb(66, 145, 241);
    private final int default_progress_color = Color.rgb(66, 145, 241);
    private final int default_secondary_color = Color.rgb(204, 204, 204);
    private final float default_progress_text_offset;
    private final float default_text_size;
    private final float default_bar_height;
    private float mMaxProgress = 100;
    /**
     * Current progress, can not exceed the max progress.
     */
    private float mCurrentProgress = 0;
    /**
     * The progress area bar color.
     */
    private int mProgressBarColor;
    /**
     * The bar secondary area color.
     */
    private int mSecondaryBarColor;
    /**
     * The progress text color.
     */
    private int mTextColor;
    /**
     * The progress text size.
     */
    private float mTextSize;
    /**
     * The height of the progress area.
     */
    private float mBarHeight;
    /**
     * The suffix of the number.
     */
    private String mSuffix = "%";
    /**
     * The prefix.
     */
    private String mPrefix = "";
    /**
     * The width of the text that to be drawn.
     */
    private float mDrawTextWidth;

    /**
     * The drawn text start.
     */
    private float mDrawTextStart;

    /**
     * The drawn text end.
     */
    private float mDrawTextEnd;

    /**
     * The text that to be drawn in onDraw().
     */
    private String mCurrentDrawText;

    /**
     * The Paint of the progress area.
     */
    private Paint mProgressBarPaint;
    /**
     * The Paint of the secondary area.
     */
    private Paint mSecondaryBarPaint;
    /**
     * The Paint of the progress text.
     */
    private Paint mTextPaint;

    /**
     * Secondary bar area to draw rect.
     */
    private RectF mSecondaryRectF = new RectF(0, 0, 0, 0);
    /**
     * Progress bar area rect.
     */
    private RectF mProgressRectF = new RectF(0, 0, 0, 0);

    /**
     * The progress text offset.
     */
    private float mOffset;

    private boolean mDrawProgressBar = true;

    private boolean mIfDrawText = true;

    /**
     * Listener
     */
    private OnProgressBarListener mListener;

    public NumberProgressBar(Context context) {
        this(context, null);
    }

    public NumberProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.numberProgressBarStyle);
    }

    public NumberProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        default_bar_height = dp2px(1.5f);
        default_text_size = sp2px(10);
        default_progress_text_offset = dp2px(3.0f);

        //load styled attributes.
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NumberProgressBar,
                defStyleAttr, 0);

        mProgressBarColor = attributes.getColor(R.styleable.NumberProgressBar_progress_color, default_progress_color);
        mSecondaryBarColor = attributes.getColor(R.styleable.NumberProgressBar_progress_secondary_color, default_secondary_color);
        mTextColor = attributes.getColor(R.styleable.NumberProgressBar_progress_text_color, default_text_color);
        mTextSize = attributes.getDimension(R.styleable.NumberProgressBar_progress_text_size, default_text_size);

        mBarHeight = attributes.getDimension(R.styleable.NumberProgressBar_progress_bar_height, default_bar_height);
        mOffset = attributes.getDimension(R.styleable.NumberProgressBar_progress_text_offset, default_progress_text_offset);

        int textVisible = attributes.getInt(R.styleable.NumberProgressBar_progress_text_visibility, PROGRESS_TEXT_VISIBLE);
        if (textVisible != PROGRESS_TEXT_VISIBLE) {
            mIfDrawText = false;
        }

        setMax(attributes.getFloat(R.styleable.NumberProgressBar_progress_max, 100));
        setProgress(attributes.getFloat(R.styleable.NumberProgressBar_progress_current, 0));

        attributes.recycle();
        initializePainters();
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return (int) mTextSize;
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return Math.max((int) mTextSize, (int) mBarHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec, true), measure(heightMeasureSpec, false));
    }

    private int measure(int measureSpec, boolean isWidth) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int padding = isWidth ? getPaddingLeft() + getPaddingRight() : getPaddingTop() + getPaddingBottom();
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = isWidth ? getSuggestedMinimumWidth() : getSuggestedMinimumHeight();
            result += padding;
            if (mode == MeasureSpec.AT_MOST) {
                if (isWidth) {
                    result = Math.max(result, size);
                } else {
                    result = Math.min(result, size);
                }
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mIfDrawText) {
            calculateDrawRectF();
        } else {
            calculateDrawRectFWithoutProgressText();
        }

        canvas.drawRect(mSecondaryRectF, mSecondaryBarPaint);

        if (mDrawProgressBar) {
            canvas.drawRect(mProgressRectF, mProgressBarPaint);
        }

        if (mIfDrawText)
            canvas.drawText(mCurrentDrawText, mDrawTextStart, mDrawTextEnd, mTextPaint);
    }

    private void initializePainters() {
        mProgressBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressBarPaint.setColor(mProgressBarColor);

        mSecondaryBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSecondaryBarPaint.setColor(mSecondaryBarColor);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
    }

    private void calculateDrawRectFWithoutProgressText() {
        mProgressRectF.left = getPaddingLeft();
        mProgressRectF.top = getHeight() / 2.0f - mBarHeight / 2.0f;
        mProgressRectF.right = (getWidth() - getPaddingLeft() - getPaddingRight()) / (getMax() * 1.0f) * getProgress() + getPaddingLeft();
        mProgressRectF.bottom = getHeight() / 2.0f + mBarHeight / 2.0f;

        mSecondaryRectF.left = getPaddingLeft();
        mSecondaryRectF.right = getWidth() - getPaddingRight();
        mSecondaryRectF.top = getHeight() / 2.0f + -mBarHeight / 2.0f;
        mSecondaryRectF.bottom = getHeight() / 2.0f + mBarHeight / 2.0f;
    }

    private void calculateDrawRectF() {
        mCurrentDrawText = String.format("%.1f", getProgress() * 100 / getMax());
        mCurrentDrawText = mPrefix + mCurrentDrawText + mSuffix;
        mDrawTextWidth = mTextPaint.measureText(mCurrentDrawText);

        mDrawTextStart = getWidth() - getPaddingRight() - mDrawTextWidth;
        mDrawTextEnd = (int) ((getHeight() / 2.0f) - ((mTextPaint.descent() + mTextPaint.ascent()) / 2.0f));

        mSecondaryRectF.left = getPaddingLeft();
        mSecondaryRectF.right = mDrawTextStart - mOffset;
        mSecondaryRectF.top = getHeight() / 2.0f + -mBarHeight / 2.0f;
        mSecondaryRectF.bottom = getHeight() / 2.0f + mBarHeight / 2.0f;

        if (getProgress() == 0) {
            mDrawProgressBar = false;
        } else {
            mDrawProgressBar = true;
            mProgressRectF.left = getPaddingLeft();
            mProgressRectF.top = getHeight() / 2.0f - mBarHeight / 2.0f;
            mProgressRectF.right = getPaddingLeft() + (mSecondaryRectF.right - mSecondaryRectF.left) * getProgress() / (getMax() * 1.0f);
            mProgressRectF.bottom = getHeight() / 2.0f + mBarHeight / 2.0f;
        }
    }

    /**
     * Get progress text color.
     *
     * @return progress text color.
     */
    public int getTextColor() {
        return mTextColor;
    }

    /**
     * Get progress text size.
     *
     * @return progress text size.
     */
    public float getProgressTextSize() {
        return mTextSize;
    }

    public void setProgressTextSize(float textSize) {
        this.mTextSize = textSize;
        mTextPaint.setTextSize(mTextSize);
        invalidate();
    }

    public int getSecondaryBarColor() {
        return mSecondaryBarColor;
    }

    public void setSecondaryBarColor(int barColor) {
        this.mSecondaryBarColor = barColor;
        mSecondaryBarPaint.setColor(mProgressBarColor);
        invalidate();
    }

    public int getProgressBarColor() {
        return mProgressBarColor;
    }

    public void setProgressBarColor(int progressColor) {
        this.mProgressBarColor = progressColor;
        mProgressBarPaint.setColor(mProgressBarColor);
        invalidate();
    }

    public float getProgress() {
        return mCurrentProgress;
    }

    public void setProgress(float progress) {
        if (progress <= getMax() && progress >= 0) {
            this.mCurrentProgress = progress;
            invalidate();
        }
    }

    public float getMax() {
        return mMaxProgress;
    }

    public void setMax(float maxProgress) {
        if (maxProgress > 0) {
            this.mMaxProgress = maxProgress;
            invalidate();
        }
    }

    public float getProgressBarHeight() {
        return mBarHeight;
    }

    public void setProgressBarHeight(float height) {
        mBarHeight = height;
    }

    public void setProgressTextColor(int textColor) {
        this.mTextColor = textColor;
        mTextPaint.setColor(mTextColor);
        invalidate();
    }

    public String getSuffix() {
        return mSuffix;
    }

    public void setSuffix(String suffix) {
        if (suffix == null) {
            mSuffix = "";
        } else {
            mSuffix = suffix;
        }
    }

    public String getPrefix() {
        return mPrefix;
    }

    public void setPrefix(String prefix) {
        if (prefix == null)
            mPrefix = "";
        else {
            mPrefix = prefix;
        }
    }

    public void incrementProgressBy(int by) {
        if (by > 0) {
            setProgress(getProgress() + by);
        }

        if (mListener != null) {
            mListener.onProgressChange(getProgress(), getMax());
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putInt(INSTANCE_TEXT_COLOR, getTextColor());
        bundle.putFloat(INSTANCE_TEXT_SIZE, getProgressTextSize());
        bundle.putFloat(INSTANCE_BAR_HEIGHT, getProgressBarHeight());
        bundle.putInt(INSTANCE_PROGRESS_BAR_COLOR, getProgressBarColor());
        bundle.putInt(INSTANCE_SECONDARY_BAR_COLOR, getSecondaryBarColor());
        bundle.putFloat(INSTANCE_MAX, getMax());
        bundle.putFloat(INSTANCE_PROGRESS, getProgress());
        bundle.putString(INSTANCE_SUFFIX, getSuffix());
        bundle.putString(INSTANCE_PREFIX, getPrefix());
        bundle.putBoolean(INSTANCE_TEXT_VISIBILITY, getProgressTextVisibility());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            mTextColor = bundle.getInt(INSTANCE_TEXT_COLOR);
            mTextSize = bundle.getFloat(INSTANCE_TEXT_SIZE);
            mBarHeight = bundle.getFloat(INSTANCE_BAR_HEIGHT);
            mProgressBarColor = bundle.getInt(INSTANCE_PROGRESS_BAR_COLOR);
            mSecondaryBarColor = bundle.getInt(INSTANCE_SECONDARY_BAR_COLOR);
            initializePainters();
            setMax(bundle.getInt(INSTANCE_MAX));
            setProgress(bundle.getInt(INSTANCE_PROGRESS));
            setPrefix(bundle.getString(INSTANCE_PREFIX));
            setSuffix(bundle.getString(INSTANCE_SUFFIX));
            setProgressTextVisibility(bundle.getBoolean(INSTANCE_TEXT_VISIBILITY) ? Visible : Invisible);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public float dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public float sp2px(float sp) {
        final float scale = getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public boolean getProgressTextVisibility() {
        return mIfDrawText;
    }

    public void setProgressTextVisibility(ProgressTextVisibility visibility) {
        mIfDrawText = visibility == Visible;
        invalidate();
    }

    public void setOnProgressBarListener(OnProgressBarListener listener) {
        mListener = listener;
    }

    public enum ProgressTextVisibility {
        Visible, Invisible
    }
}