package com.bigfat.splashscreen;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.makeramen.RoundedImageView;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private static final String WX_APP_ID = "wx65287cdde8a8f593";

    public static IWXAPI iwxapi;
    private static final int PIC_SIZE = 400;
    private static final int THUMB_SIZE = 150;

    private RelativeLayout mRlMainBg;
    private RoundedImageView mBtnStartStop;
    private RoundedImageView mBtnShare;
    private RoundedImageView mBtnChangeColor;
    private LinearLayout mLlColorBarContainer;
    private SeekBar mSeekBarRed;
    private SeekBar mSeekBarGreen;
    private SeekBar mSeekBarBlue;

    private Timer mTimer;

    private int defaultColor;
    private int splashColor;

    private boolean isSplash = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏ActionBar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        //隐藏NavigationBar
//        View decorView = getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//        decorView.setSystemUiVisibility(uiOptions);

        defaultColor = getResources().getColor(R.color.main_bg_color);
        splashColor = getResources().getColor(R.color.default_splash_color);

        initView();
        initViewListener();
        initColorBar();

        //初始化颜色
        onSplashColorChanged();
        //注册微信
        regToWx();
    }

    private void regToWx() {
        iwxapi = WXAPIFactory.createWXAPI(MainActivity.this, WX_APP_ID, true);
        iwxapi.registerApp(WX_APP_ID);
    }

    private void shareToWx(Bitmap bmp) {
        WXImageObject imgObj = new WXImageObject(bmp);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);  // 设置缩略图

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf("img" + System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        iwxapi.sendReq(req);
    }

    private void initView() {
        mRlMainBg = (RelativeLayout) findViewById(R.id.id_main_bg);
        mBtnStartStop = (RoundedImageView) findViewById(R.id.id_btn_start_stop);
        mBtnShare = (RoundedImageView) findViewById(R.id.id_btn_share);
        mBtnChangeColor = (RoundedImageView) findViewById(R.id.id_btn_change_color);
        mLlColorBarContainer = (LinearLayout) findViewById(R.id.id_ll_color_bar_container);
        mSeekBarRed = (SeekBar) findViewById(R.id.id_seek_bar_red);
        mSeekBarGreen = (SeekBar) findViewById(R.id.id_seek_bar_green);
        mSeekBarBlue = (SeekBar) findViewById(R.id.id_seek_bar_blue);
    }

    private void initViewListener() {
        mBtnStartStop.setOnClickListener(this);
        mBtnShare.setOnClickListener(this);
        mBtnChangeColor.setOnClickListener(this);
        mSeekBarRed.setOnSeekBarChangeListener(this);
        mSeekBarGreen.setOnSeekBarChangeListener(this);
        mSeekBarBlue.setOnSeekBarChangeListener(this);
    }

    private void initColorBar() {
        int red = Color.red(splashColor);
        int green = Color.green(splashColor);
        int blue = Color.blue(splashColor);
        mSeekBarRed.setProgress(red);
        mSeekBarGreen.setProgress(green);
        mSeekBarBlue.setProgress(blue);
    }

    private void onSplashColorChanged() {
        mBtnStartStop.setColorFilter(splashColor);
        mBtnShare.setBorderColor(splashColor);
        mBtnChangeColor.setBorderColor(splashColor);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_btn_start_stop://开始/停止
                startOrStop();
                break;

            case R.id.id_btn_share://分享到微信
                shareToWx(Util.createSingleColorBitmap(splashColor, PIC_SIZE, PIC_SIZE));
                break;

            case R.id.id_btn_change_color://调整闪烁颜色
                mLlColorBarContainer.setVisibility(mLlColorBarContainer.getVisibility() == View.VISIBLE ?
                        View.INVISIBLE : View.VISIBLE);
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        splashColor = Color.rgb(mSeekBarRed.getProgress(), mSeekBarGreen.getProgress(), mSeekBarBlue.getProgress());
        onSplashColorChanged();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void startOrStop() {
        if (isSplash) {
            stop();
        } else {
            start();
        }
    }

    private void start() {
        if (mTimer == null) {
            mTimer = new Timer();
            TimerTask mTimerTask = new TimerTask() {
                private boolean isDefault = false;

                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!isSplash) {
                                return;
                            }
                            if (isDefault) {
                                mRlMainBg.setBackgroundColor(defaultColor);
                            } else {
                                mRlMainBg.setBackgroundColor(splashColor);
                            }
                            isDefault = !isDefault;
                        }
                    });
                }
            };
            mTimer.schedule(mTimerTask, 0, 100);
        }
        isSplash = true;
    }

    private void stop() {
        isSplash = false;
        mRlMainBg.setBackgroundColor(defaultColor);
    }
}