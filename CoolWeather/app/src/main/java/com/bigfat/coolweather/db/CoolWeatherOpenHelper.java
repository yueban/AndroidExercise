package com.bigfat.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bigfat.coolweather.R;
import com.bigfat.coolweather.activity.MyApplication;
import com.bigfat.coolweather.util.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2014/12/16
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

    public static final String TAG = "CoolWeatherOpenHelper";

    /**
     * Province表建表语句
     */
    public static final String CREATE_PROVINCE = "create table Province ("
            + "id integer primary key autoincrement,"
            + "PROVCN text)";

    /**
     * City表建表语句
     */
    public static final String CREATE_CITY = "create table City ("
            + "id integer primary key autoincrement,"
            + "DISTRICTCN text,"
            + "PROVCN text)";

    /**
     * Country表建表语句
     */
    public static final String CREATE_COUNTRY = "create table Country ("
            + "id integer primary key autoincrement,"
            + "AREAID text,"
            + "NAMECN text,"
            + "DISTRICTCN text)";

    public CoolWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //建表
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTRY);
        //插入地区数据
        saveProvinces(db);
        saveCities(db);
        saveCountries(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 将省数据存入数据库
     */
    public void saveProvinces(SQLiteDatabase db) {
        try {
            JSONArray jsonArray = new JSONArray(Utility.getRawString(MyApplication.getContext(), R.raw.province));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put("PROVCN", jsonObject.getString("PROVCN"));
                db.insert("Province", null, values);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将市数据存入数据库
     */
    public void saveCities(SQLiteDatabase db) {
        try {
            JSONArray jsonArray = new JSONArray(Utility.getRawString(MyApplication.getContext(), R.raw.city));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put("DISTRICTCN", jsonObject.getString("DISTRICTCN"));
                values.put("PROVCN", jsonObject.getString("PROVCN"));
                db.insert("City", null, values);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将县数据存入数据库
     */
    public void saveCountries(SQLiteDatabase db) {
        try {
            JSONArray jsonArray = new JSONArray(Utility.getRawString(MyApplication.getContext(), R.raw.country));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put("AREAID", jsonObject.getString("AREAID"));
                values.put("NAMECN", jsonObject.getString("NAMECN"));
                values.put("DISTRICTCN", jsonObject.getString("DISTRICTCN"));
//                Log.d(TAG, "values--->" + values.toString());
                db.insert("Country", null, values);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
