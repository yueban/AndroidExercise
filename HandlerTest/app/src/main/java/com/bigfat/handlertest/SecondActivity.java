package com.bigfat.handlertest;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/3/10
 */
public class SecondActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String TAG = "SecondActivity";

    //    private TextView textView;
    private Button btnSend;
    private Button btnStop;

    private HandlerThread handlerThread;
    private Handler threadHandler;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "currentThread:" + Thread.currentThread());
            threadHandler.sendMessageDelayed(new Message(), 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        textView = new TextView(SecondActivity.this);
//        textView.setText("lol");
//        setContentView(textView);
        setContentView(R.layout.activity_second);

        initView();
        initEvent();

        handlerThread = new HandlerThread("handler Thread");
        handlerThread.start();
        threadHandler = new Handler(handlerThread.getLooper()) {

            @Override
            public void handleMessage(Message msg) {
//                Log.i(TAG, "currentThread:" + Thread.currentThread());
                Log.i(TAG, "currentThread:" + Thread.currentThread());
                handler.sendMessageDelayed(new Message(), 1000);
            }
        };
    }

    private void initView() {
        btnSend = (Button) findViewById(R.id.btn_second_send);
        btnStop = (Button) findViewById(R.id.btn_second_stop);
    }

    private void initEvent() {
        btnSend.setOnClickListener(this);
        btnStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_second_send:
                handler.sendEmptyMessage(1);
                break;

            case R.id.btn_second_stop:
                handler.removeMessages(1);
                break;
        }
    }
}