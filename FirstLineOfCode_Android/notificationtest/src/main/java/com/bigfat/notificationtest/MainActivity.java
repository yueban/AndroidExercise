package com.bigfat.notificationtest;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.File;


public class MainActivity extends Activity implements View.OnClickListener {

    private Button sendNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendNotice = (Button) findViewById(R.id.send_notice);
        sendNotice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_notice:
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Intent intent = new Intent(this, NotificationActivity.class);
                PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                Notification notification = new Notification.Builder(this).setTicker("This is ticker text").setSmallIcon(R.drawable.icon).setContentTitle("This is content title").setContentText("This is content text").setContentIntent(pi).build();

                Uri soundUri = Uri.fromFile(new File("/system/media/audio/ringtones/-17-昔影.mp3"));
                notification.sound = soundUri;

                long[] vibrates = {0, 1000, 1000, 1000};
                notification.vibrate = vibrates;

                notification.ledARGB = Color.GREEN;
                notification.ledOnMS = 1000;
                notification.ledOffMS = 1000;
                notification.defaults = Notification.FLAG_SHOW_LIGHTS;

                manager.notify(1, notification);
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
