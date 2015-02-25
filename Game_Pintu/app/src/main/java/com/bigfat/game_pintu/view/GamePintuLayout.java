package com.bigfat.game_pintu.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bigfat.game_pintu.R;
import com.bigfat.game_pintu.util.ImagePiece;
import com.bigfat.game_pintu.util.ImageSplitterUtil;
import com.bigfat.game_pintu.util.OnGamePintuListener;
import com.bigfat.game_pintu.util.Utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/2/17
 */
public class GamePintuLayout extends RelativeLayout implements View.OnClickListener {

    private static final int TIME_CHANGED = 0x110;
    private static final int NEXT_LEVEL = 0x111;

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
    private boolean mOnce;
    /**
     * 拼图游戏回调
     */
    private OnGamePintuListener mOnGamePintuListener;
    /**
     * 游戏时间
     */
    private int mTime;
    /**
     * 是否启用游戏倒计时
     */
    private boolean isTimeEnabled;
    /**
     * 当前游戏等级
     */
    private int mLevel = 1;
    /**
     * 游戏是否过关
     */
    private boolean isGameSuccess;
    /**
     * 游戏是否结束
     */
    private boolean isGameOver;
    /**
     * 游戏是否暂停
     */
    private boolean isPause;

    /**
     * 设置拼图游戏回调接口
     */
    public void setOnGamePintuListener(OnGamePintuListener onGamePintuListener) {
        this.mOnGamePintuListener = onGamePintuListener;
    }

    /**
     * 设置是否开启游戏计时
     */
    public void setTimeEnabled(boolean isTimeEnabled) {
        this.isTimeEnabled = isTimeEnabled;
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME_CHANGED:
                    if (isGameSuccess || isGameOver || isPause) {
                        return;
                    }
                    if (mOnGamePintuListener != null) {
                        mOnGamePintuListener.timeChanged((int) mTime);
                        if (mTime == 0) {
                            isGameOver = true;
                            mOnGamePintuListener.gameOver();
                            return;
                        }
                    }
                    mTime--;
                    //一秒后继续更新游戏计时
                    mHandler.sendEmptyMessageDelayed(TIME_CHANGED, 1000);
                    break;

                case NEXT_LEVEL:
                    mLevel++;
                    if (mOnGamePintuListener != null) {
                        mOnGamePintuListener.nextLevel(mLevel);
                    } else {
                        nextLevel();
                    }
                    break;
            }
        }
    };

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
            gameStart();
            mOnce = true;
        }
        setMeasuredDimension(mWidth, mWidth);
    }

    /**
     * 游戏开始
     */
    public void gameStart() {
        isGameSuccess = false;
        isGameOver = false;
        removeAllViews();
        mAnimLayout = null;
        initBitmap();
        initItem();
        checkTimeEnable();
    }

    /**
     * 游戏暂停
     */
    public void pause() {
        isPause = true;
    }

    /**
     * 从暂停中恢复
     */
    public void resume() {
        if (isPause) {
            isPause = false;
            mHandler.sendEmptyMessage(TIME_CHANGED);
        }
    }

    /**
     * 检查计时设置
     */
    private void checkTimeEnable() {
        if (isTimeEnabled) {
            countTimeBaseLevel();
            mHandler.sendEmptyMessage(TIME_CHANGED);
        }
    }

    /**
     * 基于关卡等级设置游戏时间
     */
    private void countTimeBaseLevel() {
        mTime = (int) (Math.pow(2, mLevel - 1) * 60);
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
            //设置点击事件监听器
            imageView.setOnClickListener(this);
            //将ImageView添加到容器中
            addView(imageView, lp);
        }
    }

    private ImageView mFirst;
    private ImageView mSecond;

    @Override
    public void onClick(View v) {
        if (isAniming) {
            return;
        }
        //第一张图片被点击两次
        if (mFirst == v) {
            mFirst.setColorFilter(null);
            mFirst = null;
            return;
        }

        if (mFirst == null) {
            mFirst = (ImageView) v;
            mFirst.setColorFilter(0x550000cc);
        } else {
            mSecond = (ImageView) v;
            exchangeView();
        }
    }

    /**
     * 动画层
     */
    private RelativeLayout mAnimLayout;

    private boolean isAniming;

    /**
     * 交换选中的两张图片
     */
    private void exchangeView() {
        mFirst.setColorFilter(null);

        //构造动画层
        setUpAnimLayout();

        final String firstTag = (String) mFirst.getTag();
        final String secondTag = (String) mSecond.getTag();

        final Bitmap firstBitmap = mItems.get(Utils.getImageIdByTag(firstTag)).getBitmap();
        final Bitmap secondBitmap = mItems.get(Utils.getImageIdByTag(secondTag)).getBitmap();

        //创建动画层上的ImageView
        ImageView first = new ImageView(getContext());
        first.setImageBitmap(mItems.get(Utils.getImageIdByTag((String) mFirst.getTag())).getBitmap());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mItemWidth, mItemWidth);
        lp.leftMargin = mFirst.getLeft() - mPadding;
        lp.topMargin = mFirst.getTop() - mPadding;
        first.setLayoutParams(lp);
        mAnimLayout.addView(first);

        ImageView second = new ImageView(getContext());
        second.setImageBitmap(mItems.get(Utils.getImageIdByTag((String) mSecond.getTag())).getBitmap());
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(mItemWidth, mItemWidth);
        lp2.leftMargin = mSecond.getLeft() - mPadding;
        lp2.topMargin = mSecond.getTop() - mPadding;
        second.setLayoutParams(lp2);
        mAnimLayout.addView(second);

        //创建动画
        TranslateAnimation firstAnim = new TranslateAnimation(0, mSecond.getLeft() - mFirst.getLeft(), 0, mSecond.getTop() - mFirst.getTop());
        firstAnim.setDuration(300);
        firstAnim.setFillAfter(true);
        first.startAnimation(firstAnim);

        TranslateAnimation secondAnim = new TranslateAnimation(0, mFirst.getLeft() - mSecond.getLeft(), 0, mFirst.getTop() - mSecond.getTop());
        secondAnim.setDuration(300);
        secondAnim.setFillAfter(true);
        second.startAnimation(secondAnim);


        firstAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mFirst.setVisibility(INVISIBLE);
                mSecond.setVisibility(INVISIBLE);
                isAniming = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mFirst.setImageBitmap(secondBitmap);
                mSecond.setImageBitmap(firstBitmap);

                mFirst.setTag(secondTag);
                mSecond.setTag(firstTag);

                mFirst.setVisibility(VISIBLE);
                mSecond.setVisibility(VISIBLE);

                mFirst = mSecond = null;
                //移除动画层图片
                mAnimLayout.removeAllViews();
                //检查是否过关
                checkSuccess();
                isAniming = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 构造动画层
     */
    private void setUpAnimLayout() {
        mAnimLayout = new RelativeLayout(getContext());
        addView(mAnimLayout);
    }

    /**
     * 检查是否过关
     */
    private void checkSuccess() {
        boolean isSuccess = true;
        for (int i = 0; i < mPintuImgs.length; i++) {
            String tag = (String) mPintuImgs[i].getTag();
            if (i != Utils.getIndexByTag(tag)) {
                isSuccess = false;
                break;
            }
        }
        if (isSuccess) {
            isGameSuccess = true;
//            Toast.makeText(getContext(), "isSuccess:" + isSuccess, Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessage(NEXT_LEVEL);
        }
    }

    /**
     * 下一关
     */
    public void nextLevel() {
        mColumn++;
        gameStart();
    }
}