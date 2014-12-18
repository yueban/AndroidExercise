package com.bigfat.areadataexecutor;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CoolWeatherOpenHelper helper = new CoolWeatherOpenHelper(this, "area", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
    }
}
