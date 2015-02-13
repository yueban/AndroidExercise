package com.bigfat.splashscreen;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private RelativeLayout mRlMainBg;
    private ImageButton mBtnStartStop;
    private ImageButton mBtnShare;
    private ImageButton mBtnChangeColor;
    private LinearLayout mLlColorBarContainer;
    private SeekBar mSeekBarRed;
    private SeekBar mSeekBarGreen;
    private SeekBar mSeekBarBlue;

    private Timer mTimer;

    private int defaultColor;
    private int splashColor = Color.RED;

    private boolean isSplash = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏ActionBar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        //隐藏NavigationBar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

        defaultColor = getResources().getColor(R.color.main_bg_color);

        initView();
        initViewListener();
        initColorBar();
    }

    private void initView() {
        mRlMainBg = (RelativeLayout) findViewById(R.id.id_main_bg);
        mBtnStartStop = (ImageButton) findViewById(R.id.id_btn_start_stop);
        mBtnShare = (ImageButton) findViewById(R.id.id_btn_share);
        mBtnChangeColor = (ImageButton) findViewById(R.id.id_btn_change_color);
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
        mSeekBarRed.setProgress(Color.red(splashColor));
        mSeekBarGreen.setProgress(Color.green(splashColor));
        mSeekBarBlue.setProgress(Color.blue(splashColor));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_btn_start_stop://开始/停止
                startOrStop();
                break;

            case R.id.id_btn_share://分享到微信

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