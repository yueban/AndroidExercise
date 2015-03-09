package com.bigfat.handlertest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private TextView tvMain;
    private ImageView imgMain;
    private Button btnMainStop;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Toast.makeText(MainActivity.this,"1",Toast.LENGTH_SHORT).show();
            return false;
        }
    }) {
        @Override
        public void handleMessage(Message msg) {
//            tvMain.setText(msg.arg1 + "-" + msg.arg2 + "=" + (msg.arg1 - msg.arg2) + "\n" + msg.obj);
            Toast.makeText(MainActivity.this,"2",Toast.LENGTH_SHORT).show();
        }
    };

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            img_index++;
            img_index = img_index % 3;
            imgMain.setImageResource(Constant.IMG[img_index]);
            mHandler.postDelayed(mRunnable, 1000);
        }
    };

    private int img_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();

        mHandler.postDelayed(mRunnable, 1000);

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);

                    Message msg = Message.obtain(mHandler,new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"0",Toast.LENGTH_SHORT).show();
                        }
                    });
                    msg.arg1 = 101;
                    msg.arg2 = 30;
                    Person p = new Person("小明", 12);
                    msg.obj = p;
                    msg.sendToTarget();


//                    mHandler.sendMessage(msg);
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
        btnMainStop = (Button) findViewById(R.id.btn_main_stop);
    }

    private void initEvent() {
        btnMainStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mHandler.removeCallbacks(mRunnable);
    }
}
