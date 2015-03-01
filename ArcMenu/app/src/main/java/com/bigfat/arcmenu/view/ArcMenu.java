package com.bigfat.arcmenu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.bigfat.arcmenu.R;
import com.bigfat.arcmenu.view.util.OnMenuItemClickListener;
import com.bigfat.arcmenu.view.util.Position;
import com.bigfat.arcmenu.view.util.Status;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/3/1
 */
public class ArcMenu extends ViewGroup {

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
     * 菜单主按钮
     */
    private View mCenterButton;
    /**
     * 子菜单点击回调接口
     */
    private OnMenuItemClickListener mMenuItemClickListener;

    public void setOnMenuClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.mMenuItemClickListener = onMenuItemClickListener;
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
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
