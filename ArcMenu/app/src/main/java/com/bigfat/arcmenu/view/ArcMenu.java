package com.bigfat.arcmenu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.bigfat.arcmenu.R;
import com.bigfat.arcmenu.view.util.OnMenuItemClickListener;
import com.bigfat.arcmenu.view.util.Position;
import com.bigfat.arcmenu.view.util.Status;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/3/1
 */
public class ArcMenu extends ViewGroup implements View.OnClickListener {

    public static final String TAG = "ArcMenu";

    /**
     * 菜单展开半径
     */
    private int mRadius;
    /**
     * 菜单位置
     */
    private Position mPosition = Position.RIGHT_BOTTOM;
    /**
     * 菜单状态
     */
    private Status mCurrentStatus = Status.CLOSE;
    /**
     * 是否正在执行动画
     */
    private boolean mIsAnim;
    /**
     * 菜单主按钮
     */
    private View mCenterButton;
    /**
     * 子菜单点击回调接口
     */
    private OnMenuItemClickListener mOnMenuItemClickListener;

    public void setOnMenuClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.mOnMenuItemClickListener = onMenuItemClickListener;
    }

    public ArcMenu(Context context) {
        this(context, null);
    }

    public ArcMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性值
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcMenu, defStyleAttr, 0);
        //菜单位置
        int pos = a.getInt(R.styleable.ArcMenu_position, Position.POS_RIGHT_BOTTOM);
        switch (pos) {
            case Position.POS_LEFT_TOP:
                mPosition = Position.LEFT_TOP;
                break;

            case Position.POS_LEFT_BOTTOM:
                mPosition = Position.LEFT_BOTTOM;
                break;
            case Position.POS_RIGHT_TOP:
                mPosition = Position.RIGHT_TOP;
                break;

            case Position.POS_RIGHT_BOTTOM:
                mPosition = Position.RIGHT_BOTTOM;
                break;
        }
        //菜单展开半径，默认100dp
        mRadius = (int) a.getDimension(R.styleable.ArcMenu_radius, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));
        a.recycle();

        Log.i(TAG, "posttion--->" + mPosition + "\tradius--->" + mRadius);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            //测量Child
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            layoutCenterButton();

            int count = getChildCount();
            for (int i = 0; i < count - 1; i++) {
                View child = getChildAt(i + 1);
                child.setVisibility(GONE);

                double radian = Math.PI / 2 / (count - 2) * i;
                int childL = (int) (mRadius * Math.sin(radian));
                int childT = (int) (mRadius * Math.cos(radian));
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                switch (mPosition) {
                    case LEFT_TOP:
                        break;
                    case LEFT_BOTTOM:
                        childT = getMeasuredHeight() - childT - childHeight;
                        break;
                    case RIGHT_TOP:
                        childL = getMeasuredWidth() - childL - childWidth;
                        break;
                    case RIGHT_BOTTOM:
                        childT = getMeasuredHeight() - childT - childHeight;
                        childL = getMeasuredWidth() - childL - childWidth;
                        break;
                }
                child.layout(childL, childT, childL + childWidth, childT + childHeight);
            }
        }
    }

    /**
     * 定位主菜单按钮
     */
    private void layoutCenterButton() {
        mCenterButton = getChildAt(0);
        mCenterButton.setOnClickListener(this);

        int l = 0;
        int t = 0;
        int width = mCenterButton.getMeasuredWidth();
        int height = mCenterButton.getMeasuredHeight();

        switch (mPosition) {
            case LEFT_TOP:
                l = 0;
                t = 0;
                break;
            case LEFT_BOTTOM:
                l = 0;
                t = getMeasuredHeight() - height;
                break;
            case RIGHT_TOP:
                l = getMeasuredWidth() - width;
                t = 0;
                break;
            case RIGHT_BOTTOM:
                l = getMeasuredWidth() - width;
                t = getMeasuredHeight() - height;
                break;
        }
        mCenterButton.layout(l, t, l + width, t + height);
    }

    @Override
    public void onClick(View v) {
        if (!mIsAnim) {
            rotateCenterButton(v, 0f, 360f, 300);
            toggleMenu(300);
        }
    }

    /**
     * 切换菜单展开/收起状态
     *
     * @param duration 子菜单项动画时间
     */
    private void toggleMenu(int duration) {
        final int count = getChildCount();
        for (int i = 0; i < count - 1; i++) {
            final View childView = getChildAt(i + 1);
            int offset = duration / 20;
            int x = mCenterButton.getLeft() - childView.getLeft();
            int y = mCenterButton.getTop() - childView.getTop();
            TranslateAnimation translateAnimation;
            if (mCurrentStatus == Status.CLOSE) {
                translateAnimation = new TranslateAnimation(x, 0, y, 0);
                childView.setClickable(true);
                childView.setFocusable(true);
            } else {
                translateAnimation = new TranslateAnimation(0, x, 0, y);
                childView.setClickable(false);
                childView.setFocusable(false);
            }
            translateAnimation.setDuration(duration);
            translateAnimation.setStartOffset(i * offset);
            final int finalI = i;
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mIsAnim = true;
                    childView.setVisibility(VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mCurrentStatus == Status.OPEN) {//open to close
                        childView.setVisibility(View.GONE);
                    }
                    if (finalI == count - 2) {//最后一个子菜单的动画结束
                        mIsAnim = false;
                        changeStatus();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            childView.startAnimation(translateAnimation);
            //设置监听器
            childView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnMenuItemClickListener != null) {
                        mOnMenuItemClickListener.onClick(childView, finalI);
                    }
                    menuItemAnim(finalI);
                    changeStatus();
                }
            });
        }
    }

    private void changeStatus() {
        mCurrentStatus = mCurrentStatus == Status.OPEN ? Status.CLOSE : Status.OPEN;
    }

    /**
     * menuItem的点击动画
     *
     * @param pos menuItem的位置0~count-2
     */
    private void menuItemAnim(int pos) {
        for (int i = 0; i < getChildCount() - 1; i++) {
            View childView = getChildAt(i + 1);
            if (pos == i) {
                childView.startAnimation(scaleBigAnim(300));
            } else {
                childView.startAnimation(scaleSmallAnim(300));
            }
            childView.setFocusable(false);
            childView.setClickable(false);
        }
    }

    private Animation scaleBigAnim(int duration) {
        AnimationSet animationSet = new AnimationSet(true);

        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 4, 1, 4, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);

        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        animationSet.setDuration(duration);
        animationSet.setFillAfter(true);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mIsAnim = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIsAnim = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return animationSet;
    }

    private Animation scaleSmallAnim(int duration) {
        AnimationSet animationSet = new AnimationSet(true);

        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);

        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        animationSet.setDuration(duration);
        animationSet.setFillAfter(true);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mIsAnim = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIsAnim = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return animationSet;
    }

    private void rotateCenterButton(View v, float start, float end, int duration) {
        RotateAnimation animation = new RotateAnimation(start, end, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(duration);
        v.startAnimation(animation);
    }
}
