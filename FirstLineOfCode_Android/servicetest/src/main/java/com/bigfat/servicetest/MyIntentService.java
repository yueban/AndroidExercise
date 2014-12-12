package com.bigfat.servicetest;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2014/12/12
 */
public class MyIntentService extends IntentService {

    public static final String TAG = "MyIntentService";

    public MyIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Thread id is " + Thread.currentThread().getId());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy executed");
    }
}
