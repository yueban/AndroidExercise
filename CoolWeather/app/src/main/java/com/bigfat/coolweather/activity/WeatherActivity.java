package com.bigfat.coolweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigfat.coolweather.R;
import com.bigfat.coolweather.service.AutoUpdateService;
import com.bigfat.coolweather.util.HttpCallbackListener;
import com.bigfat.coolweather.util.HttpUtil;
import com.bigfat.coolweather.util.Utility;
import com.bigfat.coolweather.util.WeatherApiUtil;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2014/12/18
 */
public class WeatherActivity extends Activity implements View.OnClickListener {

    private LinearLayout weatherInfoLayout;

    /**
     * 显示城市名
     */
    private TextView cityNameText;

    /**
     * 显示发布时间
     */
    private TextView publishText;

    /**
     * 显示天气描述信息（气象）
     */
    private TextView weatherDespText;

    /**
     * 显示白天气温
     */
    private TextView temp1Text;

    /**
     * 显示夜晚气温
     */
    private TextView temp2Text;

    /**
     * 显示当前日期
     */
    private TextView currentDateText;

    /**
     * 切换城市按钮
     */
    private Button switchCity;

    /**
     * 更新天气按钮
     */
    private Button refreshWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);

        //绑定控件
        weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
        cityNameText = (TextView) findViewById(R.id.city_name);
        publishText = (TextView) findViewById(R.id.publish_text);
        weatherDespText = (TextView) findViewById(R.id.weather_desp);
        temp1Text = (TextView) findViewById(R.id.temp1);
        temp2Text = (TextView) findViewById(R.id.temp2);
        currentDateText = (TextView) findViewById(R.id.current_date);
        switchCity = (Button) findViewById(R.id.switch_city);
        refreshWeather = (Button) findViewById(R.id.refresh_weather);

        //绑定监听器
        switchCity.setOnClickListener(this);
        refreshWeather.setOnClickListener(this);

        String areaId = getIntent().getStringExtra("area_id");
        if (!TextUtils.isEmpty(areaId)) {//有地区码则联网请求天气
            publishText.setText("同步中...");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            queryFromServer(areaId);
        } else {//没有地区码则直接显示本地天气
            showWeather();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_city:
                Intent intent = new Intent(this, ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity", true);
                startActivity(intent);
                finish();
                break;

            case R.id.refresh_weather:
                publishText.setText("同步中...");
                String areaId = PreferenceManager.getDefaultSharedPreferences(this).getString("area_id", "");
                if (!TextUtils.isEmpty(areaId)) {
                    queryFromServer(areaId);
                }
                break;
        }
    }

    /**
     * 从服务器获取天气信息
     *
     * @param areaId 地区码
     */
    private void queryFromServer(String areaId) {
        HttpUtil.sendHttpRequest(WeatherApiUtil.getWeatherUrl(areaId, "forecast_v"), new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                //处理服务器返回的天气信息
                Utility.handleWeatherResponse(WeatherActivity.this, response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeather();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishText.setText("同步失败");
                    }
                });
            }
        });
    }

    /**
     * 从SharedPreferences文件中读取天气信息，并显示到界面上
     */
    private void showWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        cityNameText.setText(prefs.getString("city_name", ""));
        temp1Text.setText(prefs.getString("temp1", ""));
        temp2Text.setText(prefs.getString("temp2", ""));
        weatherDespText.setText(prefs.getString("weather_desp", ""));
        publishText.setText(prefs.getString("publish_time", "") + "发布");
        currentDateText.setText(prefs.getString("current_date", ""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);
        //启动天气自动更新Service
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }
}
