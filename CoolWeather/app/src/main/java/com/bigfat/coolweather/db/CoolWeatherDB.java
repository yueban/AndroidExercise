package com.bigfat.coolweather.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bigfat.coolweather.model.City;
import com.bigfat.coolweather.model.Country;
import com.bigfat.coolweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2014/12/16
 */
public class CoolWeatherDB {

    public static final String TAG = "CoolWeatherDB";

    /**
     * 数据库名
     */
    public static final String DB_NAME = "cool_weather";

    /**
     * 数据库版本
     */
    public static final int VERSION = 1;

    private static CoolWeatherDB coolWeatherDB;

    private SQLiteDatabase db;

    /**
     * 将构造方法私有化（单例模式）
     */
    private CoolWeatherDB(Context context) {
        CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 获取CoolWeatherDB的实例
     */
    public synchronized static CoolWeatherDB getInstance(Context context) {
        if (coolWeatherDB == null) {
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    /**
     * 从数据库读取所有省份的数据
     */
    public List<Province> loadProvinces() {
        List<Province> list = new ArrayList<>();
        Cursor cursor = db.query("Province", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setProvCn(cursor.getString(cursor.getColumnIndex("PROVCN")));
                list.add(province);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    /**
     * 从数据库读取某省下所有城市的数据
     *
     * @param provCn 省份拼音（PROVEN字段）
     */
    public List<City> loadCities(String provCn) {
        List<City> list = new ArrayList<>();
        Cursor cursor = db.query("City", null, "PROVCN=?", new String[]{provCn}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setDistrictCn(cursor.getString(cursor.getColumnIndex("DISTRICTCN")));
                city.setProvCn(cursor.getString(cursor.getColumnIndex("PROVCN")));
                list.add(city);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    /**
     * 从数据库读取某城市下的所有县的数据
     *
     * @param districtCn 城市拼音（DISTRICTEN字段）
     */
    public List<Country> loadCountries(String districtCn) {
        List<Country> list = new ArrayList<>();
        Cursor cursor = db.query("Country", null, "DISTRICTCN=?", new String[]{districtCn}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Country country = new Country();
                country.setAreaId(cursor.getString(cursor.getColumnIndex("AREAID")));
                country.setNameCn(cursor.getString(cursor.getColumnIndex("NAMECN")));
                country.setDistrictCn(cursor.getString(cursor.getColumnIndex("DISTRICTCN")));
                list.add(country);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
