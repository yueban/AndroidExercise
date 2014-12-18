package com.bigfat.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2014/12/16
 */
public class Utility {

    public static final String TAG = "Utility";

    /**
     * 解析服务器返回的天气json数据，并将解析出的数据存到本地
     */
    public static void handleWeatherResponse(Context context, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject c = jsonObject.getJSONObject("c");
            String cityName = c.getString("c3");
            String weatherCode = c.getString("c1");
            JSONObject f = jsonObject.getJSONObject("f");
            String publishTime = f.getString("f0");
            JSONObject f1_1 = f.getJSONArray("f1").getJSONObject(0);
            String temp1 = f1_1.getString("fc");
            String temp2 = f1_1.getString("fd");
            String weatherDesp = WeatherApiUtil.getWeatherById(f1_1.getString("fa")) + WeatherApiUtil.getWeatherById(f1_1.getString("fb"));
            saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将服务器返回的天气信息存储到SharedPreferences中
     *
     * @param cityName    城市名
     * @param weatherCode 城市id
     * @param temp1       白天温度
     * @param temp2       夜晚温度
     * @param weatherDesp 气象
     * @param publishTime 发布时间
     */
    public static void saveWeatherInfo(Context context, String cityName, String weatherCode, String temp1, String temp2, String weatherDesp, String publishTime) {
        try {
            //用于格式化当前日期
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
            //用于将服务器返回的发布时间格式化成可以阅读的时间
            SimpleDateFormat pSdf = new SimpleDateFormat("yyyyMMddHHmm", Locale.CHINA);
            SimpleDateFormat fSdf = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.CHINA);
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
            editor.putBoolean("city_selected", true);
            editor.putString("city_name", cityName);
            editor.putString("weather_code", weatherCode);
            editor.putString("temp1", temp1);
            editor.putString("temp2", temp2);
            editor.putString("weather_desp", weatherDesp);
            editor.putString("publish_time", fSdf.format(pSdf.parse(publishTime)));
            editor.putString("current_date", sdf.format(new Date()));
            editor.apply();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将raw文件夹中的文件读取为String字符串
     *
     * @param rawId raw文件夹中的文件id
     */
    public static String getRawString(Context context, int rawId) {
        StringBuilder jsonData = new StringBuilder();
        InputStream in = context.getResources().openRawResource(rawId);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                jsonData.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonData.toString();
    }
}
