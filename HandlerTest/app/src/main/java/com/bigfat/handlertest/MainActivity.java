package com.bigfat.handlertest;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    private TextView tvMain;
    private ImageView imgMain;

    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    tvMain.setText("没有报错");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void initView() {
        tvMain = (TextView) findViewById(R.id.tv_main);
        imgMain = (ImageView) findViewById(R.id.img_main);
    }
}
