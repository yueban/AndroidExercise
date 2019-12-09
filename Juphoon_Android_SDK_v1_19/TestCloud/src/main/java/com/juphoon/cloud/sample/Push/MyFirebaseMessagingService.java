package com.juphoon.cloud.sample.Push;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.juphoon.cloud.sample.JCApplication;
import com.juphoon.cloud.sample.MainActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

//        if (JCManager.getInstance().client.getState() != JCManager.getInstance().client.STATE_LOGINED) {
//            return;
//        }
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                JCApplication.sContext.startActivity(new Intent(JCApplication.sContext, MainActivity.class));
                Log.d("jcsample", "OnMessageReceived:" + remoteMessage.getData().toString());
            }
        });
    }
}
