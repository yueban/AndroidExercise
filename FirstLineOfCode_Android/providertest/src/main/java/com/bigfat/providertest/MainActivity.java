package com.bigfat.providertest;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity implements View.OnClickListener {

    public static final String TAG = "MainActivity";

    private String newId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addData = (Button) findViewById(R.id.add_data);
        Button queryData = (Button) findViewById(R.id.query_data);
        Button updateData = (Button) findViewById(R.id.update_data);
        Button deleteData = (Button) findViewById(R.id.delete_data);

        addData.setOnClickListener(this);
        queryData.setOnClickListener(this);
        updateData.setOnClickListener(this);
        deleteData.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_data:
                Log.d(TAG, "book price is " );
                Uri uriAdd = Uri.parse("content://com.bigfat.databasetest.provider/book");
                ContentValues valuesAdd = new ContentValues();
                valuesAdd.put("name", "A Clash of Kings");
                valuesAdd.put("author", "George Martin");
                valuesAdd.put("pages", 1040);
                valuesAdd.put("price", 22.85);
                Uri newUri = getContentResolver().insert(uriAdd, valuesAdd);
                newId = newUri.getPathSegments().get(1);
                break;

            case R.id.query_data:
                Uri uriQuery = Uri.parse("content://com.bigfat.databasetest.provider/book");
                Cursor cursor = getContentResolver().query(uriQuery, null, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));

                        Log.d(TAG, "book name is " + name);
                        Log.d(TAG, "book author is " + author);
                        Log.d(TAG, "book pages is " + pages);
                        Log.d(TAG, "book price is " + price);
                    }
                    cursor.close();
                }
                break;

            case R.id.update_data:
                Uri uriUpdate = Uri.parse("content://com.bigfat.databasetest.provider/book/" + newId);
                ContentValues valuesUpdate = new ContentValues();
                valuesUpdate.put("name", "A Storm of Swords");
                valuesUpdate.put("pages", 1216);
                valuesUpdate.put("price", 24.05);
                getContentResolver().update(uriUpdate, valuesUpdate, null, null);
                break;

            case R.id.delete_data:
                Uri uriDelete = Uri.parse("content://com.bigfat.databasetest.provider/book/" + newId);
                getContentResolver().delete(uriDelete, null, null);
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
