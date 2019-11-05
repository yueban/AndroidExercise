package com.yueban.contactmimetype;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class ViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        Log.d("view activity", getIntent().getData().toString());
        Cursor cursor = getContentResolver().query(getIntent().getData(), null, null, null, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String data1 = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DATA1));
            Log.d("view activity", "name: " + name);
            Log.d("view activity", "data1: " + data1);

            Bundle extras = cursor.getExtras();
            for (String key : extras.keySet()) {
                Log.d("view activity", String.format("%s : %s", key, extras.get(key)));
            }
        }

//        Log.d("123", getIntent().toString());
//        Bundle extras = getIntent().getExtras();
//        for (String key : extras.keySet()) {
//            Log.d("123123", String.format("%s : %s", key, extras.get(key)));
//        }
    }
}
