package com.bigfat.areadataexecutor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2014/12/16
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

    public static final String TAG = "CoolWeatherOpenHelper";

    /**
     * Province表建表语句
     */
    public static final String CREATE_AREA = "create table Area ("
            + "id integer primary key autoincrement,"
            + "AREAID text,"
            + "NAMEEN text,"
            + "NAMECN text,"
            + "DISTRICTEN text,"
            + "DISTRICTCN text,"
            + "PROVEN text,"
            + "PROVCN text,"
            + "NATIONEN text,"
            + "NATIONCN text)";

    public CoolWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //建表
        db.execSQL(CREATE_AREA);
        //插入地区数据
        saveAreas(db);
        //将省市县数据分别写入三个文件
        loadProvince(db);
        loadCity(db);
        loadCountry(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 将数据存入数据库
     */
    public void saveAreas(SQLiteDatabase db) {
        try {
            JSONArray jsonArray = new JSONArray(getRawString(MyApplication.getContext(), R.raw.area));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put("AREAID", jsonObject.getString("AREAID"));
                values.put("NAMEEN", jsonObject.getString("NAMEEN"));
                values.put("NAMECN", jsonObject.getString("NAMECN"));
                values.put("DISTRICTEN", jsonObject.getString("DISTRICTEN"));
                values.put("DISTRICTCN", jsonObject.getString("DISTRICTCN"));
                values.put("PROVEN", jsonObject.getString("PROVEN"));
                values.put("PROVCN", jsonObject.getString("PROVCN"));
                values.put("NATIONEN", jsonObject.getString("NATIONEN"));
                values.put("NATIONCN", jsonObject.getString("NATIONCN"));
                db.insert("Area", null, values);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 将raw文件夹中的文件读取为String字符串
     *
     * @param rawId raw文件夹中的文件id
     */
    public String getRawString(Context context, int rawId) {
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

    public void loadProvince(SQLiteDatabase db) {
        Cursor cursor = db.query(true, "Area", new String[]{"PROVCN"}, null, null, null, null, null, null);
        try {
            JSONArray jsonArray = new JSONArray();
            if (cursor.moveToFirst()) {
                do {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("PROVCN", cursor.getString(cursor.getColumnIndex("PROVCN")));
                    jsonArray.put(jsonObject);
                } while (cursor.moveToNext());
            }
//            Log.d(TAG, "jsonArray--->" + jsonArray.toString());

            FileOutputStream out = null;
            BufferedWriter writer = null;
            out = MyApplication.getContext().openFileOutput("province", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(jsonArray.toString());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cursor.close();
    }

    public void loadCity(SQLiteDatabase db) {
        Cursor cursor = db.query(true, "Area", new String[]{"DISTRICTCN", "PROVCN"}, null, null, null, null, null, null);
        try {
            JSONArray jsonArray = new JSONArray();
            if (cursor.moveToFirst()) {
                do {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("DISTRICTCN", cursor.getString(cursor.getColumnIndex("DISTRICTCN")));
                    jsonObject.put("PROVCN", cursor.getString(cursor.getColumnIndex("PROVCN")));
                    jsonArray.put(jsonObject);
                } while (cursor.moveToNext());
            }
//            Log.d(TAG, "jsonArray--->" + jsonArray.toString());

            FileOutputStream out = null;
            BufferedWriter writer = null;
            out = MyApplication.getContext().openFileOutput("city", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(jsonArray.toString());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cursor.close();
    }

    public void loadCountry(SQLiteDatabase db) {
        Cursor cursor = db.query(true, "Area", new String[]{"AREAID", "NAMECN", "DISTRICTCN"}, null, null, null, null, null, null);
        try {
            JSONArray jsonArray = new JSONArray();
            if (cursor.moveToFirst()) {
                do {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("AREAID", cursor.getString(cursor.getColumnIndex("AREAID")));
                    jsonObject.put("NAMECN", cursor.getString(cursor.getColumnIndex("NAMECN")));
                    jsonObject.put("DISTRICTCN", cursor.getString(cursor.getColumnIndex("DISTRICTCN")));
                    jsonArray.put(jsonObject);
                } while (cursor.moveToNext());
            }
//            Log.d(TAG, "jsonArray--->" + jsonArray.toString());

            FileOutputStream out = null;
            BufferedWriter writer = null;
            out = MyApplication.getContext().openFileOutput("country", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(jsonArray.toString());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cursor.close();
    }
}
