package com.bigfat.handlertest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/3/10
 */
public class ThreeActivity extends ActionBarActivity {

    private static final String TAG = "ThreeActivity";

    private TextView tvTitle;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            tvTitle.setText("handler2");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);

        tvTitle = (TextView) findViewById(R.id.tv_three_title);
        new Thread(new Runnable() {
            @Override
            public void run() {
//                handler1();
//                handler2();
//                UiThread();
                ViewThread();
            }
        }).start();
    }

    private void handler1() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                tvTitle.setText("handler1");
            }
        });
    }

    private void handler2() {
        handler.sendEmptyMessage(1);
    }

    private void UiThread() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvTitle.setText("UiThread");
            }
        });
    }

    private void ViewThread() {
        tvTitle.post(new Runnable() {
            @Override
            public void run() {
                tvTitle.setText("ViewThread");
            }
        });
    }
}
