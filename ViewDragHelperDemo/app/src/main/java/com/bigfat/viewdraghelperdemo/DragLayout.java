package com.bigfat.viewdraghelperdemo;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by yueban on 23:13 10/1/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class DragLayout extends LinearLayout {
  private final ViewDragHelper mViewDragHelper;
  private View mDragView;
  private View mAutoBackView;
  private View mEdgeTrackerView;
  //自动回弹View原点
  private Point mAutoBackOriginPos = new Point();

  public DragLayout(Context context) {
    this(context, null);
  }

  public DragLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    mViewDragHelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback());
    mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    mViewDragHelper.processTouchEvent(event);
    return true;
  }

  @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
    final int action = MotionEventCompat.getActionMasked(ev);
    if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
      mViewDragHelper.cancel();
      return false;
    }
    return mViewDragHelper.shouldInterceptTouchEvent(ev);
  }

  @Override public void computeScroll() {
    if (mViewDragHelper.continueSettling(true)) {
      invalidate();
    }
  }

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);
    mAutoBackOriginPos.x = mAutoBackView.getLeft();
    mAutoBackOriginPos.y = mAutoBackView.getTop();
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    mDragView = getChildAt(0);
    mAutoBackView = getChildAt(1);
    mEdgeTrackerView = getChildAt(2);
  }

  class DragHelperCallback extends ViewDragHelper.Callback {

    @Override public boolean tryCaptureView(View child, int pointerId) {
      return child == mDragView || child == mAutoBackView;
    }

    @Override public int clampViewPositionHorizontal(View child, int left, int dx) {
      int leftBound = getPaddingLeft();
      int rightBound = getWidth() - child.getWidth() - getPaddingRight();
      return Math.min(Math.max(left, leftBound), rightBound);
    }

    @Override public int clampViewPositionVertical(View child, int top, int dy) {
      int topBound = getPaddingTop();
      int bottomBound = getHeight() - getPaddingBottom() - child.getHeight();
      return Math.min(Math.max(topBound, top), bottomBound);
    }

    @Override public void onViewReleased(View releasedChild, float xvel, float yvel) {
      if (releasedChild == mAutoBackView) {
        mViewDragHelper.settleCapturedViewAt(mAutoBackOriginPos.x, mAutoBackOriginPos.y);
        invalidate();
      }
    }

    @Override public void onEdgeDragStarted(int edgeFlags, int pointerId) {
      mViewDragHelper.captureChildView(mEdgeTrackerView, pointerId);
    }

    @Override public int getViewVerticalDragRange(View child) {
      return getMeasuredHeight() - child.getMeasuredHeight();
    }

    @Override public int getViewHorizontalDragRange(View child) {
      return getMeasuredWidth() - child.getMeasuredWidth();
    }
  }
}