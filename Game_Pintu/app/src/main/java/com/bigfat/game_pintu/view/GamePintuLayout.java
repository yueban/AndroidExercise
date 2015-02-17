package com.bigfat.game_pintu.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bigfat.game_pintu.R;
import com.bigfat.game_pintu.util.ImagePiece;
import com.bigfat.game_pintu.util.ImageSplitterUtil;
import com.bigfat.game_pintu.util.Utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/2/17
 */
public class GamePintuLayout extends RelativeLayout implements View.OnClickListener {
    /**
     * 容器的宽度
     */
    private int mWidth;
    /**
     * 默认是3x3的拼图
     */
    private int mColumn = 3;
    /**
     * 容器的内边距
     */
    private int mPadding;
    /**
     * 每张小图的间距(dp)
     */
    private int mMargin = 3;
    /**
     * 图片对象
     */
    private ImageView[] mPintuImgs;
    /**
     * 每张拼图宽度
     */
    private int mItemWidth;
    /**
     * 游戏的大图
     */
    private Bitmap mBitmap;
    /**
     * 游戏的拼图
     */
    private List<ImagePiece> mItems;
    /**
     * 用于控制测量操作只进行一次
     */
    private boolean mOnce = false;

    public GamePintuLayout(Context context) {
        this(context, null);
    }

    public GamePintuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GamePintuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mMargin, getResources().getDisplayMetrics());
        mPadding = Utils.min(getPaddingLeft(), getPaddingRight(), getPaddingTop(), getPaddingBottom());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = Utils.min(getMeasuredWidth(), getMeasuredHeight());
        if (!mOnce) {
            initBitmap();
            initItem();
            mOnce = true;
        }
        setMeasuredDimension(mWidth, mWidth);
    }


    /**
     * 切图及排序
     */
    private void initBitmap() {
        if (mBitmap == null) {
            mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test);
        }
        mItems = ImageSplitterUtil.splitImage(mBitmap, mColumn);
        //图片乱序
        Collections.sort(mItems, new Comparator<ImagePiece>() {
            @Override
            public int compare(ImagePiece lhs, ImagePiece rhs) {
                return Math.random() > 0.5 ? 1 : -1;
            }
        });
    }

    /**
     * 设置ImageView的宽高等属性
     */
    private void initItem() {
        mItemWidth = (mWidth - mPadding * 2 - mMargin * (mColumn - 1)) / mColumn;
        mPintuImgs = new ImageView[mColumn * mColumn];
        //设置拼图ImageView属性
        for (int i = 0; i < mPintuImgs.length; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setOnClickListener(this);
            imageView.setImageBitmap(mItems.get(i).getBitmap());
            mPintuImgs[i] = imageView;
            //设置id
            imageView.setId(i + 1);
            //设置index，即乱序后图片真实的索引
            imageView.setTag(i + "_" + mItems.get(i).getIndex());

            //设置宽高及依赖关系
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mItemWidth, mItemWidth);
            //不是第一列
            if (i % mColumn != 0) {
                lp.leftMargin = mMargin;
                lp.addRule(RelativeLayout.RIGHT_OF, mPintuImgs[i - 1].getId());
            }
            //不是第一行
            if (i + 1 > mColumn) {
                lp.topMargin = mMargin;
                lp.addRule(RelativeLayout.BELOW, mPintuImgs[i - mColumn].getId());
            }
            //将ImageView添加到容器中
            addView(imageView, lp);
        }
    }

    @Override
    public void onClick(View v) {

    }
}
