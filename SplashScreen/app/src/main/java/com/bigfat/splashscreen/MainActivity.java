package com.bigfat.splashscreen;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity {

    private int splashColor = Color.RED;

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

        final RelativeLayout mainBg = (RelativeLayout) findViewById(R.id.id_main_bg);
        TimerTask timerTask = new TimerTask() {
            private boolean isBlue = true;

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isBlue) {
                            mainBg.setBackgroundColor(Color.BLUE);
                        } else {
                            mainBg.setBackgroundColor(splashColor);
                        }
                        isBlue = !isBlue;
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 0, 200);
    }
}