package com.bigfat.handlertest;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/3/10
 */
public class SecondActivity extends ActionBarActivity {

    private static final String TAG = "SecondActivity";

    private TextView textView;

    private HandlerThread handlerThread;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textView = new TextView(SecondActivity.this);
        textView.setText("lol");
        setContentView(textView);

        handlerThread = new HandlerThread("handler Thread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper()) {

            @Override
            public void handleMessage(Message msg) {
                Log.i(TAG, "currentThread:" + Thread.currentThread());
            }
        };
    }
}
