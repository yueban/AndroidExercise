package com.bigfat.notificationtest;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2014/12/9
 */
public class NotificationActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_layout);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(1);
    }
}
