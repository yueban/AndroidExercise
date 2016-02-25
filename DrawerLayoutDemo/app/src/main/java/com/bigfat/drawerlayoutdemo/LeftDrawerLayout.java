package com.bigfat.drawerlayoutdemo;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yueban on 09:24 11/1/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class LeftDrawerLayout extends ViewGroup {
    private static final int MIN_DRAWER_MARGIN = 64; // dp
    /**
     * Minimum velocity that will be detected as a fling
     */
    private static final int MIN_FLING_VELOCITY = 400; // dips per second
    private static final String TAG = LeftDrawerLayout.class.getSimpleName();
    private final ViewDragHelper mViewDragHelper;
    /**
     * drawer离父容器右边的最小外边距
     */
    private int mMinDrawerMargin;

    private View mLeftMenuView;
    private View mContentView;

    /**
     * drawer显示出来的占自身的百分比
     */
    private float mLeftMenuOnScreen;

    public LeftDrawerLayout(Context context) {
        this(context, null);
    }

    public LeftDrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LeftDrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final float density = getResources().getDisplayMetrics().density;
        final float minVel = MIN_FLING_VELOCITY * density;
        mMinDrawerMargin = (int) (MIN_DRAWER_MARGIN * density + 0.5f);

        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragCallback());
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        mViewDragHelper.setMinVelocity(minVel);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(widthSize, heightSize);

        mLeftMenuView = findViewById(R.id.id_drawer);
        MarginLayoutParams lp = (MarginLayoutParams) mLeftMenuView.getLayoutParams();
        final int drawerWidthSpec = getChildMeasureSpec(widthMeasureSpec,
                mMinDrawerMargin + lp.leftMargin + lp.rightMargin,
                lp.width);
        final int drawerHeightSpec = getChildMeasureSpec(heightMeasureSpec,
                lp.topMargin + lp.bottomMargin,
                lp.height);
        mLeftMenuView.measure(drawerWidthSpec, drawerHeightSpec);

        mContentView = findViewById(R.id.id_content);
        lp = (MarginLayoutParams) mContentView.getLayoutParams();
        final int contentWidthSpec = MeasureSpec.makeMeasureSpec(widthSize - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY);
        final int contentHeightSpec = MeasureSpec.makeMeasureSpec(heightSize - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY);
        mContentView.measure(contentWidthSpec, contentHeightSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        MarginLayoutParams lp = (MarginLayoutParams) mContentView.getLayoutParams();
        int left = lp.leftMargin + getPaddingLeft();
        int top = lp.topMargin + getPaddingTop();
        mContentView.layout(left, top, left + mContentView.getMeasuredWidth(), top + mContentView.getMeasuredHeight());

        lp = (MarginLayoutParams) mLeftMenuView.getLayoutParams();
        final int menuWidth = mLeftMenuView.getMeasuredWidth();
        left = -menuWidth + (int) (menuWidth * mLeftMenuOnScreen);
        top = lp.topMargin + getPaddingTop();
        mLeftMenuView.layout(left, top, left + menuWidth, top + mLeftMenuView.getMeasuredHeight());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    public void closeDrawer() {
        mLeftMenuOnScreen = 0.f;
        mViewDragHelper.smoothSlideViewTo(mLeftMenuView, -mLeftMenuView.getWidth(), mLeftMenuView.getTop());
    }

    public void openDrawer() {
        mLeftMenuOnScreen = 1.0f;
        mViewDragHelper.smoothSlideViewTo(mLeftMenuView, -0, mLeftMenuView.getTop());
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    class ViewDragCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mLeftMenuView;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return Math.max(-child.getWidth(), Math.min(left, 0));
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            mViewDragHelper.captureChildView(mLeftMenuView, pointerId);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            final int childWidth = releasedChild.getWidth();
            float offset = (childWidth + releasedChild.getLeft()) * 1.0f / childWidth;
            mViewDragHelper.settleCapturedViewAt(xvel > 0 || xvel == 0 && offset > 0.5f ? 0 : -childWidth, releasedChild.getTop());
            invalidate();
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            final int childWidth = changedView.getWidth();
            float offset = (float) (childWidth + left) / childWidth;
            mLeftMenuOnScreen = offset;
            changedView.setVisibility(offset == 0 ? INVISIBLE : VISIBLE);
            invalidate();
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return child == mLeftMenuView ? child.getWidth() : 0;
        }
    }
}
