package com.juphoon.cloud.doodle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.juphoon.cloud.JCDoodle;
import com.juphoon.cloud.JCDoodleAction;
import com.juphoon.cloud.JCDoodleInteractor;
import com.juphoon.cloud.doodle.utils.DoodleUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DoodleLayout extends RelativeLayout implements
        DoodleTouchHelper.Listener, JCDoodleInteractor {

    public interface DoodleControllerListener {

        void onExitDoodle();

        void onFaceMode();

        void onShareDoodle(Bitmap bitmap);

        void onWillCleanDoodle();
    }

    private static final int DOODLE_MODE_DRAW = 0;
    private static final int DOODLE_MODE_ERASER = 1;
    private static final int DOODLE_MODE_STICKER = 2;

    private final int[] COLORS = {0xffff0000, 0xffff7900, 0xfffff100, 0xff8fc320, 0xff0182ff,
            0xff9100ef, 0xffff0094, 0xfffda5a5, 0xff96bed6, 0xff000000};

    private DoodleView mDoodleView;

    private Paint mBrush;
    private Paint mErase;
    private Path mPathToSend;

    private ColorSelector mColorSelector;
    private DotView mColorDot;
    private ImageView mStrokeButton;
    private StrokeSeekBar mStrokeSeekBar;
    private ImageView mEraserButton;
    private StrokeSeekBar mEraserStrokeSeekBar;
    private ImageView mStickerButton;
    private StickerSelector mStickerGrid;
    private ImageView mFloatingSticker;
    private ImageView mFloatingStickerDelete;

    private int mDoodleMode = DOODLE_MODE_DRAW;
    private int mCurrentColorValue;
    private int mCurrentStrokeWidth = 14;
    private int mCurrentEraserStrokeWidth = 14;
    private boolean mActionShouldStop;
    private boolean mSettled;
    private int mDegree;

    private DoodleTouchHelper mTouchHelper;
    private float mScreenDensity;

    private WindowManager mWindowManager;
    private Matrix[] mMatrices;
    private int mCanvasWidth;
    private int mCanvasHeight;

    private Handler mHandler = new Handler();
    private long mLastActionTimestamp = -1L;

    private JCDoodle mJCDoodle;
    private JCDoodleAction.Builder mCurrentActionBuilder;

    private DoodleControllerListener mDoodleControllerListener;

    public DoodleLayout(Context context) {
        super(context);
        init(context);
    }

    public DoodleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void destroy() {
        setDoodleMode(DOODLE_MODE_DRAW);
    }

    public void injectJCDoodle(JCDoodle jcDoodle) {
        mJCDoodle = jcDoodle;
    }

    public void setDoodleControllerListener(DoodleControllerListener doodleControllerListener) {
        mDoodleControllerListener = doodleControllerListener;
    }

    public void cleanDoodle() {
        mActionShouldStop = true;
        mDoodleView.clean();
        mJCDoodle.generateDoodleAction(new JCDoodleAction.Builder(JCDoodle.ACTION_CLEAR).build());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mTouchHelper.routeTouchEvent(event);
    }

    @Override
    public void onDrawStart(float startX, float startY) {
        log("onDrawStart startX:" + startX + " startY:" + startY);
        hidePopupControllers();
        mPathToSend.moveTo(startX, startY);
        mLastActionTimestamp = SystemClock.elapsedRealtime();
        mCurrentActionBuilder = new JCDoodleAction.Builder(mDoodleMode == DOODLE_MODE_ERASER
                ? JCDoodle.ACTION_ERASER : JCDoodle.ACTION_DRAW)
                .paintColor(mCurrentColorValue)
                .paintStrokeWidth(mDoodleMode == DOODLE_MODE_ERASER
                        ? screenBrushWidth2dataBrushWidth(mCurrentEraserStrokeWidth)
                        : screenBrushWidth2dataBrushWidth(mCurrentStrokeWidth));
        mCurrentActionBuilder.addActionPoint(0, screeX2DataX(startX), screenY2dataY(startY));
    }

    @Override
    public void onDrawing(float preX, float preY, float x, float y) {
        log("onDrawing pointX:" + x + " pointY:" + y);
        mPathToSend.quadTo(preX, preY, (preX + x) / 2, (preY + y) / 2);
        Matrix matrix = getTransformMatrix();
        mDoodleView.drawPath(mPathToSend, mDoodleMode == DOODLE_MODE_ERASER ? mErase : mBrush, matrix);
        if (mCurrentActionBuilder != null) {
            long interval = SystemClock.elapsedRealtime() - mLastActionTimestamp;
            mLastActionTimestamp += interval;
            mCurrentActionBuilder.addActionPoint((int) (interval),
                    screeX2DataX(x), screenY2dataY(y));
        }
    }

    @Override
    public void onDrawStop() {
        log("onDrawStop");
        mLastActionTimestamp = 0;
        mPathToSend.reset();
        onDoodleActionDone();
    }

    @Override
    public void onDrawPoint(float pointX, float pointY) {
        log("onDrawPoint pointX:" + pointX + " pointY:" + pointY);
        mPathToSend.reset();
    }

    @Override
    public void onStickerDragStart(final float x, final float y) {
        log("onStickerDragStart");
        hidePopupControllers();
        cancelStickerFreeze();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mSettled) {
                    copySticker(x, y);
                } else {
                    Rect rect = new Rect(mFloatingSticker.getLeft(), mFloatingSticker.getTop(),
                            mFloatingSticker.getRight(), mFloatingSticker.getBottom());
                    if (!rect.contains((int) x, (int) y)) {
                        freezeSticker();
                        copySticker(x, y);
                    }
                }
            }
        });
    }

    @Override
    public void onStickerDragging(float preX, float preY, float x, float y) {
        log("onStickerDragging");
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mFloatingSticker.getLayoutParams();
        params.leftMargin += x - preX;
        params.topMargin += y - preY;
        mFloatingSticker.setLayoutParams(params);

        params = (FrameLayout.LayoutParams) mFloatingStickerDelete.getLayoutParams();
        params.leftMargin += x - preX;
        params.topMargin += y - preY;
        mFloatingStickerDelete.setLayoutParams(params);

        cancelStickerFreeze();
    }

    @Override
    public void onStickerDragDone() {
        log("onStickerDragDone");
        scheduleStickerFreeze();
    }

    @Override
    public void onStickerReshapeStart() {
        log("onStickerReshapeStart");
        hidePopupControllers();
        Object tag = mFloatingSticker.getTag();
        if (tag instanceof StickerModel) {
            StickerModel model = (StickerModel) tag;
            model.tempOriginalX = mFloatingSticker.getLeft();
            model.tempOriginalY = mFloatingSticker.getTop();
            model.tempWidthInPixel = mFloatingSticker.getWidth();
            model.tempHeightInPixel = mFloatingSticker.getHeight();
        }
    }

    @Override
    public void onStickerReshaping(PointF scalePoint, float scale) {
        log("onStickerReshaping");
        Object tag = mFloatingSticker.getTag();
        if (tag instanceof StickerModel) {
            StickerModel model = (StickerModel) tag;
            int stickerWidth = mFloatingSticker.getMeasuredWidth() - model.emojiPadding * 2;
            if (model.isEmoji) {
                if (scale > 1) {
                    if (stickerWidth >= getResources().getDimensionPixelSize(R.dimen.doodle_sticker_emoji_max_size)) {
                        return;
                    }
                } else {
                    if (stickerWidth < getResources().getDimensionPixelSize(R.dimen.doodle_sticker_emoji_min_size)) {
                        return;
                    }
                }
            } else {
                if (scale < 1 && stickerWidth < (model.widthInPixel / 4)) {
                    return;
                } else if (scale > 1 && stickerWidth > (model.widthInPixel * 1.5)) {
                    return;
                }
            }
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mFloatingSticker.getLayoutParams();
            params.leftMargin = (int) (scalePoint.x - (scalePoint.x - model.tempOriginalX) * scale);
            params.topMargin = (int) (scalePoint.y - (scalePoint.y - model.tempOriginalY) * scale);
            params.width = Math.round(model.tempWidthInPixel * scale);
            params.height = Math.round(model.tempHeightInPixel * scale);
            mFloatingSticker.setLayoutParams(params);

            FrameLayout.LayoutParams params2 = (FrameLayout.LayoutParams) mFloatingStickerDelete.getLayoutParams();
            params2.leftMargin = params.leftMargin + params.width - mFloatingStickerDelete.getWidth() / 2;
            params2.topMargin = params.topMargin - mFloatingStickerDelete.getHeight() / 2;
            mFloatingStickerDelete.setLayoutParams(params2);
        }
    }

    @Override
    public void onDoodleReceived(JCDoodleAction doodleAction) {
        mLatestStroke = new Stroke();
        int actionType = doodleAction.getActionType();
        if (actionType == JCDoodle.ACTION_DRAW || actionType == JCDoodle.ACTION_ERASER) {
            mActionShouldStop = false;
            mLatestStroke.isErase = actionType == JCDoodle.ACTION_ERASER;
            mLatestStroke.brush.setColor(doodleAction.getPaintColor());
            float width = dataBrushWidth2screenBrushWidth(doodleAction.getPaintStrokeWidth());
            mLatestStroke.brush.setStrokeWidth(getStrokeWidthFromSelector(width));
            mLatestStroke.erase.setStrokeWidth(getStrokeWidthFromSelector(width));
            if (doodleAction.getIntervalPointList() != null && !doodleAction.getIntervalPointList().isEmpty()) {
                mActionList = new ArrayList<>();
                int size = doodleAction.getIntervalPointList().size();
                int count = 0;
                for (List<String> points : doodleAction.getIntervalPointList()) {
                    Action action = new Action(Integer.valueOf(points.get(0)), dataX2screenX(Float.valueOf(points.get(1))),
                            dataY2screenY(Float.valueOf(points.get(2))),
                            count == 0 ? Action.ACTION_FIRST : (count == size - 1 ? Action.ACTION_LAST : 0));
                    mActionList.add(action);
                    count ++;
                }
            }
            startHandleActions(false);
        } else if (actionType == JCDoodle.ACTION_STICKER) {
            mActionShouldStop = false;
            Bitmap bitmap = null;
            if (TextUtils.isEmpty(doodleAction.getStickerName())) {
                bitmap = getEmojiBitmap(doodleAction.getStickerUnicode(), getResources().getDimensionPixelSize(R.dimen.doodle_sticker_emoji_max_text_size));
            } else {
                int resourceId = StickerSelector.findImageResourceId(doodleAction.getStickerName());
                if (resourceId > 0) {
                    bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
                }
            }

            if (bitmap == null) {
                return;
            }
            Matrix matrix = new Matrix();
            matrix.setRotate(doodleAction.getStickerRotate(), bitmap.getWidth() / 2, bitmap.getHeight() / 2);
            Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
            Matrix matrixRotate = getTransformMatrix();
            mDoodleView.drawImage(newBitmap, dataX2screenX(doodleAction.getStickerX()),
                    dataY2screenY(doodleAction.getStickerY()),
                    dataWidth2screenWidth(doodleAction.getStickerWidth()),
                    dataHeight2screenHeight(doodleAction.getStickerHeight()), matrixRotate);
            bitmap.recycle();
        } else if (actionType == JCDoodle.ACTION_CLEAR) {
            mActionShouldStop = true;
            mDoodleView.clean();
        }
    }

    private void init(Context context) {
        Random random = new Random();
        int colorIndex = random.nextInt(COLORS.length);
        mCurrentColorValue = COLORS[colorIndex];
        // internal
        mTouchHelper = new DoodleTouchHelper();
        mTouchHelper.setListener(this);
        LayoutInflater.from(context).inflate(R.layout.doodle, this, true);

        mScreenDensity = context.getResources().getDisplayMetrics().density;

        mPathToSend = new Path();
        mBrush = PaintFactory.createPaint(PaintFactory.TYPE_BRUSH);
        mBrush.setStrokeWidth(getStrokeWidthFromSelector(mCurrentStrokeWidth));
        mBrush.setColor(mCurrentColorValue);
        mErase = PaintFactory.createPaint(PaintFactory.TYPE_ERASE);
        mErase.setStrokeWidth(getStrokeWidthFromSelector(mCurrentEraserStrokeWidth));

        mWindowManager = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE));
        mMatrices = DoodleUtils.createRotationMatrixForInputting(mWindowManager);

        DisplayMetrics metrics = DoodleUtils.getRealDisplayMetrics(getContext());
        int width = Math.min(metrics.widthPixels, metrics.heightPixels);
        int height = Math.max(metrics.widthPixels, metrics.heightPixels);
        if (metrics.widthPixels < metrics.heightPixels) {
            // vertical
            mCanvasWidth = width;
            mCanvasHeight = width * 16 / 9;
        } else {
            // landscape
            mCanvasWidth = height;
            mCanvasHeight = height * 9 / 16;
        }
        mDoodleView = new DoodleView(getContext(), width, height);
        mDoodleView.setBackgroundColor(0);
        addView(mDoodleView, 0, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        // find views
        mColorSelector = findViewById(R.id.color_view);
        mColorDot = findViewById(R.id.color_dot);
        mStrokeButton = findViewById(R.id.stroke);
        mStrokeSeekBar = findViewById(R.id.stroke_seek_bar);
        mEraserButton = findViewById(R.id.eraser);
        mEraserStrokeSeekBar = findViewById(R.id.eraser_stroke_seek_bar);
        mStickerButton = findViewById(R.id.sticker);
        mStickerGrid = findViewById(R.id.sticker_grid);
        mFloatingSticker = findViewById(R.id.floating_sticker);
        mFloatingStickerDelete = findViewById(R.id.floating_sticker_delete);
        mStickerGrid.setItemOnClickListener(mStickerClickListener);

        mColorSelector.initColors(COLORS, colorIndex);
        mColorSelector.setColorListener(mColorSelectListener);
        mColorDot.setup(mCurrentColorValue, getResources().getDimensionPixelSize(R.dimen.color_selector_radius));
        mStrokeSeekBar.setSeekListener(mSeekListener);
        mStrokeSeekBar.setEraserMode(false);
        mEraserStrokeSeekBar.setSeekListener(mSeekListener);
        mEraserStrokeSeekBar.setColor(Color.WHITE);
        mEraserStrokeSeekBar.setEraserMode(true);

        findViewById(R.id.face_mode).setOnClickListener(mViewClickListener);
        findViewById(R.id.onlineShare).setOnClickListener(mViewClickListener);
        findViewById(R.id.stroke).setOnClickListener(mViewClickListener);
        findViewById(R.id.eraser).setOnClickListener(mViewClickListener);
        findViewById(R.id.color).setOnClickListener(mViewClickListener);
        findViewById(R.id.sticker).setOnClickListener(mViewClickListener);
        findViewById(R.id.delete).setOnClickListener(mViewClickListener);
        findViewById(R.id.doodle_exit).setOnClickListener(mViewClickListener);
        findViewById(R.id.floating_sticker_delete).setOnClickListener(mViewClickListener);

        setDoodleMode(DOODLE_MODE_DRAW);
        focusStrokeButton(true);
    }

    private void onDoodleActionDone() {
        if (mCurrentActionBuilder != null && mJCDoodle != null) {
            JCDoodleAction action = mCurrentActionBuilder.build();
            mJCDoodle.generateDoodleAction(action);
            mCurrentActionBuilder = null;
        }
    }

    private void setDoodleMode(int doodleMode) {
        focusStrokeButton(doodleMode == DOODLE_MODE_DRAW);
        focusEraserButton(doodleMode == DOODLE_MODE_ERASER);
        focusStickerButton(doodleMode == DOODLE_MODE_STICKER);
        mDoodleMode = doodleMode;
        if (doodleMode == DOODLE_MODE_DRAW) {
            mTouchHelper.setMode(DoodleTouchHelper.MODE_NONE);
        } else if (doodleMode == DOODLE_MODE_ERASER) {
            mTouchHelper.setMode(DoodleTouchHelper.MODE_NONE);
        } else if (doodleMode == DOODLE_MODE_STICKER) {
            mTouchHelper.setMode(DoodleTouchHelper.MODE_DRAW_STICKER);
        }
    }

    private void toggleColorSelector() {
        int visibility = mColorSelector.getVisibility();
        if (visibility == View.VISIBLE) {
            mColorSelector.setVisibility(View.INVISIBLE);
        } else if (visibility == View.INVISIBLE) {
            mColorSelector.setVisibility(View.VISIBLE);
            mStrokeSeekBar.setVisibility(View.INVISIBLE);
            mEraserStrokeSeekBar.setVisibility(View.INVISIBLE);
            mStickerGrid.setVisibility(View.INVISIBLE);
        }
        setDoodleMode(DOODLE_MODE_DRAW);
    }

    private void toggleStrokeSeekBar() {
        int visibility = mStrokeSeekBar.getVisibility();
        if (visibility == View.VISIBLE) {
            mStrokeSeekBar.setVisibility(View.INVISIBLE);
        } else if (visibility == View.INVISIBLE) {
            mColorSelector.setVisibility(View.INVISIBLE);
            mStrokeSeekBar.setVisibility(View.VISIBLE);
            mEraserStrokeSeekBar.setVisibility(View.INVISIBLE);
            mStickerGrid.setVisibility(View.INVISIBLE);
        }
        mStrokeSeekBar.setColor(mCurrentColorValue);
        mStrokeSeekBar.seekTo(mCurrentStrokeWidth);

        setDoodleMode(DOODLE_MODE_DRAW);
    }

    private void toggleEraserSeekBar() {
        int visibility = mEraserStrokeSeekBar.getVisibility();
        if (visibility == View.VISIBLE) {
            mEraserStrokeSeekBar.setVisibility(View.INVISIBLE);
        } else if (visibility == View.INVISIBLE) {
            mColorSelector.setVisibility(View.INVISIBLE);
            mStrokeSeekBar.setVisibility(View.INVISIBLE);
            mEraserStrokeSeekBar.setVisibility(View.VISIBLE);
            mStickerGrid.setVisibility(View.INVISIBLE);
        }
        mEraserStrokeSeekBar.seekTo(mCurrentEraserStrokeWidth);
        setDoodleMode(DOODLE_MODE_ERASER);
    }

    private void toggleStickerSelector() {
        if (mStickerGrid.getVisibility() == View.VISIBLE) {
            mStickerGrid.setVisibility(View.INVISIBLE);
        } else {
            mColorSelector.setVisibility(View.INVISIBLE);
            mStrokeSeekBar.setVisibility(View.INVISIBLE);
            mEraserStrokeSeekBar.setVisibility(View.INVISIBLE);
            mStickerGrid.setVisibility(View.VISIBLE);
        }
        setDoodleMode(DOODLE_MODE_STICKER);
    }

    private void focusStrokeButton(boolean focus) {
        if (focus) {
            focusStickerButton(false);
            mStrokeButton.setImageResource(R.mipmap.doodle_stroke_focused);
        } else {
            mStrokeButton.setImageResource(R.mipmap.doodle_stroke);
        }
    }

    private void focusEraserButton(boolean focus) {
        if (focus) {
            mEraserButton.setImageResource(R.mipmap.doodle_eraser_focused);
        } else {
            mEraserButton.setImageResource(R.mipmap.doodle_eraser);
        }
    }

    private void focusStickerButton(boolean focus) {
        if (focus) {
            focusStrokeButton(false);
            mStickerButton.setImageResource(R.mipmap.doodle_sticker_focused);
        } else {
            mStickerButton.setImageResource(R.mipmap.doodle_sticker);
        }
    }

    private void startHandleActions(boolean isFast) {
        final Stroke stroke = mLatestStroke;
        stroke.actions.addAll(mActionList);
        if (isFast) {
            final int n = mActionList.size();
            for (int i = 0; i < n; i++) {
                handleAction(stroke, i);
            }
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    for (int i = 0; i < stroke.actions.size(); i++) {
                        if (mActionShouldStop) {
                            mActionShouldStop = false;
                            Message message = Message.obtain();
                            message.what = StrokeHandler.MSG_CLEAR_DOODLE;
                            mDelayHandler.sendMessage(message);
                            break;
                        }
                        Action action = stroke.actions.get(i);
                        if (action.intval > 0) {
                            try {
                                Thread.sleep(action.intval);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        Message message = Message.obtain();
                        message.what = StrokeHandler.MSG_HANDLE_ACTION;
                        message.obj = stroke;
                        message.arg1 = i;
                        mDelayHandler.sendMessage(message);
                    }
                }
            }).start();
        }
    }

    private void handleAction(Stroke stroke, int index) {
        Matrix matrix = getTransformMatrix();
        Action action = stroke.actions.get(index);
        if (action.isFirst()) {
            stroke.path.moveTo(action.x, action.y);
            stroke.mPeerStartX = action.x;
            stroke.mPeerStartY = action.y;
        } else {
            stroke.path.quadTo(stroke.mPeerPreX, stroke.mPeerPreY, (stroke.mPeerPreX + action.x) / 2, (stroke.mPeerPreY + action.y) / 2);
            mDoodleView.drawPath(stroke.path, stroke.isErase ? stroke.erase : stroke.brush, matrix);
        }

        if (action.isLast()) {
            if (!DoodleTouchHelper.isMoved(action.x, action.y, stroke.mPeerStartX, stroke.mPeerStartY) && !DoodleTouchHelper.isMoved(action.x, action.y, stroke.mPeerPreX, stroke.mPeerPreY)) {
                mDoodleView.drawPoint(action.x, action.y, stroke.isErase ? stroke.erase : stroke.brush, matrix);
                stroke.path.reset();
            } else {
                mDoodleView.drawPath(stroke.path, stroke.isErase ? stroke.erase : stroke.brush, matrix);
                stroke.path.reset();
            }
            stroke.mPeerPreX = stroke.mPeerStartY = stroke.mPeerPreX = stroke.mPeerPreY = -1;
        }
        stroke.mPeerPreX = action.x;
        stroke.mPeerPreY = action.y;
    }

    private void showFloatingSticker(StickerModel stickerModel, Rect rect) {
        mSettled = false;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(stickerModel.widthInPixel,
                stickerModel.heightInPixel);
        params.leftMargin = rect.left + rect.width() / 2 - stickerModel.widthInPixel / 2;
        params.topMargin = rect.top + rect.height() / 2 - stickerModel.heightInPixel / 2;
        mFloatingSticker.setLayoutParams(params);
        if (stickerModel.isEmoji) {
            Bitmap emojiBitmap = getEmojiBitmap(stickerModel.emojiText, stickerModel.emojiSizePX);
            int padding = (stickerModel.widthInPixel - emojiBitmap.getWidth()) / 2;
            stickerModel.emojiPadding = padding;
            mFloatingSticker.setPadding(padding, padding, padding, padding);
            mFloatingSticker.setImageBitmap(emojiBitmap);
        } else {
            mFloatingSticker.setPadding(stickerModel.emojiPadding, stickerModel.emojiPadding,
                    stickerModel.emojiPadding, stickerModel.emojiPadding);
            mFloatingSticker.setImageResource(stickerModel.imageResourceId);
        }
        mFloatingSticker.setTag(stickerModel.minimallyCopy());
        FrameLayout.LayoutParams params2 = (FrameLayout.LayoutParams) mFloatingStickerDelete.getLayoutParams();
        params2.leftMargin = rect.left + rect.width() / 2 + stickerModel.widthInPixel / 2 - mFloatingStickerDelete.getWidth() / 2;
        params2.topMargin = rect.top + rect.height() / 2 - stickerModel.heightInPixel / 2 - mFloatingStickerDelete.getHeight() / 2;
        mFloatingStickerDelete.setLayoutParams(params2);
        setDoodleMode(DOODLE_MODE_STICKER);
        showFloatingStickerAnimation();
        scheduleStickerFreeze();
    }

    private void deleteFloatingSticker() {
        mSettled = true;
        mFloatingSticker.setVisibility(INVISIBLE);
        mFloatingStickerDelete.setVisibility(INVISIBLE);
        cancelStickerFreeze();
    }

    private void freezeSticker() {
        if (mSettled || mTouchHelper.getMode() != DoodleTouchHelper.MODE_DRAW_STICKER) {
            return;
        }
        mSettled = true;

        StickerModel model = (StickerModel) mFloatingSticker.getTag();
        if (model == null) {
            return;
        }

        mFloatingSticker.setVisibility(View.INVISIBLE);
        mFloatingStickerDelete.setVisibility(View.INVISIBLE);
        int left = mFloatingSticker.getLeft();
        int top = mFloatingSticker.getTop();
        int width = mFloatingSticker.getWidth();
        int height = mFloatingSticker.getHeight();
        Drawable drawable = mFloatingSticker.getDrawable();
        Bitmap bitmap = null;
        if (drawable != null && drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        }
        if (bitmap == null) {
            return;
        }
        Matrix matrix = new Matrix();
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        int distance = (width - height) / 2;
        int bitmapWidth = 0;
        int bitmapHeight = 0;
        switch (mDegree) {
            case 0:
            case -180:
                bitmapWidth = width - model.emojiPadding * 2;
                bitmapHeight = height - model.emojiPadding * 2;
                break;
            case -90:
            case -270:
                bitmapWidth = height - model.emojiPadding * 2;
                bitmapHeight = width - model.emojiPadding * 2;
                left += distance;
                top -= distance;
                break;
        }
        Matrix matrixRotate = getTransformMatrix();
        mDoodleView.drawImage(newBitmap, left + model.emojiPadding, top + model.emojiPadding,
                bitmapWidth, bitmapHeight, matrixRotate);
        mCurrentActionBuilder = new JCDoodleAction.Builder(JCDoodle.ACTION_STICKER);
        mCurrentActionBuilder.stickerX(screeX2DataX(left + model.emojiPadding));
        mCurrentActionBuilder.stickerY(screenY2dataY(top + model.emojiPadding));
        mCurrentActionBuilder.stickerWidth(screenWidth2dataWidth(bitmapWidth));
        mCurrentActionBuilder.stickerHeight(screenHeight2dataHeight(bitmapHeight));
        mCurrentActionBuilder.stickerRotate(mDegree);
        mCurrentActionBuilder.stickerUnicode(model.emojiText);
        mCurrentActionBuilder.stickerName(model.stickerName);
        bitmap.recycle();

        onDoodleActionDone();
    }

    private void copySticker(float x, float y) {
        mSettled = false;
        mFloatingStickerDelete.setVisibility(View.INVISIBLE);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mFloatingSticker.getLayoutParams();
        int leftMargin = (int) (x - mFloatingSticker.getMeasuredWidth() / 2);
        int topMargin = (int) (y - mFloatingSticker.getMeasuredHeight() / 2);
        params.leftMargin = leftMargin;
        params.topMargin = topMargin;
        mFloatingSticker.setLayoutParams(params);

        FrameLayout.LayoutParams params2 = (FrameLayout.LayoutParams) mFloatingStickerDelete.getLayoutParams();
        params2.leftMargin = params.leftMargin + params.width - mFloatingStickerDelete.getMeasuredWidth() / 2;
        params2.topMargin = params.topMargin - mFloatingStickerDelete.getMeasuredHeight() / 2;
        mFloatingStickerDelete.setLayoutParams(params2);
        showFloatingStickerAnimation();
        scheduleStickerFreeze();
    }

    private float getStrokeWidthFromSelector(float value) {
//        return (value / 2.0f) * mScreenDensity;
        return value;
    }

    private Matrix getTransformMatrix() {
        int rotation = mWindowManager.getDefaultDisplay().getRotation();
        return mMatrices[rotation];
    }

    private float dataBrushWidth2screenBrushWidth(float dataWidth) {
        return dataWidth * mCanvasWidth;
    }

    private float screenBrushWidth2dataBrushWidth(float screenWidth) {
        return screenWidth / mCanvasWidth;
    }

    private float screeX2DataX(float screeX) {
        return screeX * 2 / mCanvasWidth - 1.0f;
    }

    private float screenY2dataY(float screenY) {
        return screenY * 2 / mCanvasHeight - 1.0f;
    }

    private float screenWidth2dataWidth(float screenWidth) {
        return screenWidth * 2 / mCanvasWidth;
    }

    private float screenHeight2dataHeight(float screenHeight) {
        return screenHeight * 2 / mCanvasHeight;
    }

    private float dataX2screenX(float dataX) {
        return (dataX + 1.0f) * mCanvasWidth / 2;
    }

    private float dataY2screenY(float dataY) {
        return (dataY + 1.0f) * mCanvasHeight / 2;
    }

    private float dataWidth2screenWidth(float dataWidth) {
        return dataWidth * mCanvasWidth / 2;
    }

    private float dataHeight2screenHeight(float dataHeight) {
        return dataHeight * mCanvasHeight / 2;
    }

    private Bitmap getEmojiBitmap(String text, float textSizePx) {
        Paint paint = new Paint();
        paint.setTextSize(textSizePx);
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        Bitmap bitmap = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawText(text, 0, rect.height() - rect.bottom, paint);
        return bitmap;
    }

    private void hidePopupControllers() {
        mColorSelector.setVisibility(View.INVISIBLE);
        mStrokeSeekBar.setVisibility(View.INVISIBLE);
        mEraserStrokeSeekBar.setVisibility(View.INVISIBLE);
        mStickerGrid.setVisibility(View.INVISIBLE);
    }

    private void showFloatingStickerAnimation() {
        ScaleAnimation animation = new ScaleAnimation(0.1f, 1, 0.1f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(150);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mFloatingSticker.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mFloatingStickerDelete.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        mFloatingSticker.startAnimation(animation);
    }

    private void scheduleStickerFreeze() {
        mHandler.postDelayed(mFreezeStickerAnimationRunnable, 2000);
    }

    private void cancelStickerFreeze() {
        mHandler.removeCallbacks(mFreezeStickerAnimationRunnable);
    }

    private void clearDoodleView() {
        mActionShouldStop = true;
        mDoodleView.clean();
    }

    private void log(String message) {
        Log.d("Doodle", message);
    }

    private OnClickListener mViewClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id != R.id.floating_sticker_delete) {
                freezeSticker();
            }
            if (id == R.id.color) {
                toggleColorSelector();
            } else if (id == R.id.stroke) {
                toggleStrokeSeekBar();
            } else if (id == R.id.eraser) {
                toggleEraserSeekBar();
            } else if (id == R.id.sticker) {
                toggleStickerSelector();
            } else if (id == R.id.delete) {
                mDoodleControllerListener.onWillCleanDoodle();
            } else if (id == R.id.face_mode) {
                mDoodleControllerListener.onFaceMode();
            } else if (id == R.id.onlineShare) {
                mDoodleControllerListener.onShareDoodle(mDoodleView.getCachedBitmap());
            } else if (id == R.id.floating_sticker_delete) {
                deleteFloatingSticker();
            } else if (id == R.id.doodle_exit) {
                if (mDoodleControllerListener != null) {
                    mDoodleControllerListener.onExitDoodle();
                }
            }
        }
    };

    private ColorSelector.ColorListener mColorSelectListener = new ColorSelector.ColorListener() {
        @Override
        public void onColorSelected(int color) {
            setDoodleMode(DOODLE_MODE_DRAW);
            mCurrentColorValue = color;
            mColorDot.changeColor(mCurrentColorValue);
            toggleColorSelector();
            mBrush.setColor(color);
        }
    };

    private StrokeSeekBar.SeekListener mSeekListener = new StrokeSeekBar.SeekListener() {
        @Override
        public void onSeek(int value) {
            if (mDoodleMode == DOODLE_MODE_ERASER) {
                mCurrentEraserStrokeWidth = value;
                mErase.setStrokeWidth(getStrokeWidthFromSelector(value));
            } else {
                mCurrentStrokeWidth = value;
                mBrush.setStrokeWidth(getStrokeWidthFromSelector(value));
            }
        }
    };

    private OnClickListener mStickerClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            setDoodleMode(DOODLE_MODE_STICKER);
            hidePopupControllers();
            Object tag = v.getTag();
            if (tag != null && tag instanceof StickerModel) {
                StickerModel model = (StickerModel) tag;
                Point p = DoodleUtils.getRelativePosition(DoodleLayout.this, v);
                showFloatingSticker(model, new Rect(p.x, p.y, p.x + v.getWidth(), p.y + v.getHeight()));
            }
        }
    };

    private Runnable mFreezeStickerAnimationRunnable = new Runnable() {
        @Override
        public void run() {
            freezeSticker();
        }
    };

    private static final class StrokeHandler extends Handler {
        static final int MSG_HANDLE_ACTION = 1;
        static final int MSG_CLEAR_DOODLE = 2;

        private WeakReference<DoodleLayout> mDoodleLayoutWeakReference;

        StrokeHandler(DoodleLayout layout) {
            mDoodleLayoutWeakReference = new WeakReference<>(layout);
        }

        @Override
        public void handleMessage(Message msg) {
            DoodleLayout layout = mDoodleLayoutWeakReference.get();
            if (layout == null) {
                return;
            }
            switch (msg.what) {
                case MSG_HANDLE_ACTION:
                    Stroke stroke = (Stroke) msg.obj;
                    layout.handleAction(stroke, msg.arg1);
                    break;
                case MSG_CLEAR_DOODLE:
                    layout.clearDoodleView();
                    break;
            }
        }
    }

    private final class Action {
        private static final int ACTION_FIRST = 1;
        private static final int ACTION_LAST = 2;

        int intval;
        float x;
        float y;
        int attributes;

        Action(int interval, float x, float y, int attributes) {
            this.intval = interval;
            this.x = x;
            this.y = y;
            this.attributes = attributes;
        }

        boolean isFirst() {
            return (attributes & ACTION_FIRST) != 0;
        }

        boolean isLast() {
            return (attributes & ACTION_LAST) != 0;
        }
    }

    private static final class Stroke {
        private float mPeerPreX;
        private float mPeerPreY;
        private float mPeerStartX;
        private float mPeerStartY;
        private boolean isErase;
        private Paint brush = PaintFactory.createPaint(PaintFactory.TYPE_BRUSH);
        private Paint erase = PaintFactory.createPaint(PaintFactory.TYPE_ERASE);
        private Path path = new Path();
        private List<Action> actions = new ArrayList<>();

        @Override
        public String toString() {
            int n = actions == null ? 0 : actions.size();
            String s = super.toString();
            return "Stroke" + s.substring(s.lastIndexOf("@")) + "|" + n;
        }
    }

    private Stroke mLatestStroke;
    private StrokeHandler mDelayHandler = new StrokeHandler(this);
    private List<Action> mActionList;
}
