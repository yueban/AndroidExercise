package com.bigfat.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.bigfat.coolweather.receiver.AutoUpdateReceiver;
import com.bigfat.coolweather.util.HttpCallbackListener;
import com.bigfat.coolweather.util.HttpUtil;
import com.bigfat.coolweather.util.Utility;
import com.bigfat.coolweather.util.WeatherApiUtil;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2014/12/19
 */
public class AutoUpdateService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateWeather();
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int updatePeriod = 8 * 60 * 60 * 1000;//更新间隔
        long triggerAtTime = SystemClock.elapsedRealtime() + updatePeriod;//具体的更新时间
        Intent i = new Intent(this, AutoUpdateReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        //启动更新闹钟
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 更新天气信息
     */
    private void updateWeather() {
        String areaId = PreferenceManager.getDefaultSharedPreferences(this).getString("area_id", "");
        HttpUtil.sendHttpRequest(WeatherApiUtil.getWeatherUrl(areaId, "forecast_v"), new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                //处理服务器返回的天气信息
                Utility.handleWeatherResponse(AutoUpdateService.this, response);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
