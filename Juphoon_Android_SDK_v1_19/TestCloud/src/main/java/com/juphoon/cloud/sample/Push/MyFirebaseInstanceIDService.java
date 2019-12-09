package com.juphoon.cloud.sample.Push;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.juphoon.cloud.JCPushTemplate;
import com.juphoon.cloud.sample.JCWrapper.JCManager;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        JCPushTemplate pushInfo = new JCPushTemplate();
        pushInfo.initWithGCM("1019326510974", token);
        JCManager.getInstance().push.addPushInfo(pushInfo);
        pushInfo.initWithCall(JCPushTemplate.GCM, JCManager.getInstance().client.getUserId(), "呼叫", "1");
        JCManager.getInstance().push.addPushInfo(pushInfo);
        pushInfo.initWithText(JCPushTemplate.GCM, JCManager.getInstance().client.getUserId(), "Text", "消息", "0");
        JCManager.getInstance().push.addPushInfo(pushInfo);
    }
}
