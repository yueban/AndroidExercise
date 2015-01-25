package com.bigfat.qq_50_slidingmenu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.bigfat.qq_50_slidingmenu.R;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/1/25
 */
public class SlidingMenu extends HorizontalScrollView {

    private LinearLayout mWrapper;
    private ViewGroup mMenu;
    private ViewGroup mContent;

    private int mScreenWidth;
    private int mMenuWidth;
    private int mMenuRightPadding = 50;//dp

    private boolean firstMeasure = true;//避免多次测量
    private boolean isOpen = false;//menu是否打开

    /**
     * 代码中new一个对象，会调用该构造方法
     */
    public SlidingMenu(Context context) {
        this(context, null);
    }

    /**
     * 未使用自定义属性时，两个参数的构造方法会被调用
     */
    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 使用了自定义属性时，会调用此构造方法
     */
    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SlidingMenu, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.SlidingMenu_rightPadding:
                    mMenuRightPadding = (int) a.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics()));
                    break;
            }
        }
        a.recycle();

        DisplayMetrics outMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (firstMeasure) {
            mWrapper = (LinearLayout) getChildAt(0);
            mMenu = (ViewGroup) mWrapper.getChildAt(0);
            mContent = (ViewGroup) mWrapper.getChildAt(1);

            mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth - mMenuRightPadding;
            mContent.getLayoutParams().width = mScreenWidth;

            firstMeasure = false;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 通过设置偏移量，将menu隐藏
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            this.scrollTo(mMenuWidth, 0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                if (getScrollX() < mMenuWidth / 2) {
                    this.smoothScrollTo(0, 0);
                    isOpen = true;
                } else {
                    this.smoothScrollTo(mMenuWidth, 0);
                    isOpen = false;
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 打开菜单
     */
    public void openMenu() {
        if (isOpen) {
            return;
        }
        this.smoothScrollTo(0, 0);
        isOpen = true;
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (!isOpen) {
            return;
        }
        this.smoothScrollTo(mMenuWidth, 0);
        isOpen = false;
    }

    /**
     * 切换菜单开/关状态
     */
    public void toggle() {
        if (isOpen) {
            closeMenu();
        } else {
            openMenu();
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //Log.i("SlidingMenu", "l--->" + l);
        float scale = l * 1.0f / mMenuWidth;//1.0-0.0
        float leftScale = 1.0f - 0.3f * scale;
        float rightScale = 0.7f + 0.3f * scale;
        float leftAlpha = 1.0f - 0.4f * scale;
        //调用属性动画
        //menu偏移
        mMenu.setTranslationX(l * 0.7f);
        //menu缩放
        mMenu.setPivotX(0);
        mMenu.setPivotY(mMenu.getHeight() / 2);
        mMenu.setScaleX(leftScale);
        mMenu.setScaleY(leftScale);
        //menu透明度变化
        mMenu.setAlpha(leftAlpha);
        //content缩放
        mContent.setPivotX(0);
        mContent.setPivotY(mContent.getHeight() / 2);
        mContent.setScaleX(rightScale);
        mContent.setScaleY(rightScale);
    }
}
