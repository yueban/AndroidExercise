package com.yueban.sdcardidentity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.util.Arrays;

/**
 * @author yueban fbzhh007@gmail.com
 * @date 2019-11-04
 */
public class MountReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case Intent.ACTION_MEDIA_MOUNTED:

                Log.d("MountReceiver", "ACTION_MEDIA_MOUNTED:\t" + intent.toString());
                String path = intent.getData().getPath();
                Log.d("MountReceiver", "ACTION_MEDIA_MOUNTED:\t" + path);
                Log.d("MountReceiver", "ACTION_MEDIA_MOUNTED:\t" + Arrays.toString(new File(path).listFiles()));

                break;
            case Intent.ACTION_MEDIA_UNMOUNTED:
                Log.d("MountReceiver", "ACTION_MEDIA_UNMOUNTED:\t" + intent.toString());
                break;
        }
    }
}
