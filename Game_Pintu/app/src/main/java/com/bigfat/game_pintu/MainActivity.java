package com.bigfat.game_pintu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.bigfat.game_pintu.util.OnGamePintuListener;
import com.bigfat.game_pintu.view.GamePintuLayout;


public class MainActivity extends ActionBarActivity {

    private GamePintuLayout mGamePintuLayout;
    private TextView mTvLevel;
    private TextView mTvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGamePintuLayout = (GamePintuLayout) findViewById(R.id.id_gamePintu);
        mTvLevel = (TextView) findViewById(R.id.id_level);
        mTvTime = (TextView) findViewById(R.id.id_time);

        mGamePintuLayout.setTimeEnabled(true);
        mGamePintuLayout.setOnGamePintuListener(new OnGamePintuListener() {
            @Override
            public void nextLevel(final int nextLevel) {
                new AlertDialog.Builder(MainActivity.this).setTitle("游戏信息").setMessage("恭喜过关！").setPositiveButton("开始", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mGamePintuLayout.nextLevel();
                        mTvLevel.setText(String.valueOf(nextLevel));
                    }
                }).show();
            }

            @Override
            public void timeChanged(int currentTime) {
                mTvTime.setText(String.valueOf(currentTime));
            }

            @Override
            public void gameOver() {
                new AlertDialog.Builder(MainActivity.this).setTitle("游戏信息").setMessage("游戏结束").setPositiveButton("重新开始", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mGamePintuLayout.gameStart();
                    }
                }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
            }
        });
    }

    @Override
    protected void onPause() {
        mGamePintuLayout.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mGamePintuLayout.resume();
        super.onResume();
    }
}