package com.bigfat.handlertest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";

    private TextView tvMain;

    private ImageView imgMain;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            tvMain.setText(msg.arg1 + "-" + msg.arg2 + "=" + (msg.arg1 - msg.arg2) + "\n" + msg.obj);
        }
    };

    private Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            img_index++;
            img_index = img_index % 3;
            imgMain.setImageResource(Constant.IMG[img_index]);
            mHandler.postDelayed(myRunnable, 1000);
        }
    };

    private int img_index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
//        mHandler.postDelayed(myRunnable, 1000);

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);

                    Message msg = new Message();
                    msg.arg1 = 101;
                    msg.arg2 = 30;
                    Person p = new Person("小明", 12);
                    msg.obj = p;
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(1000);
//                    mHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            tvMain.setText("没有报错");
//                        }
//                    });
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
    }

    private void initView() {
        tvMain = (TextView) findViewById(R.id.tv_main);
        imgMain = (ImageView) findViewById(R.id.img_main);
    }
}
