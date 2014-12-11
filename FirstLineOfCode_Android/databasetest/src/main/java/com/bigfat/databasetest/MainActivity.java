package com.bigfat.databasetest;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity implements View.OnClickListener {

    public static final String TAG = "MainActivity";

    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new MyDatabaseHelper(MainActivity.this, "BookStore.db", null, 3);
        Button createDatabase = (Button) findViewById(R.id.create_database);
        Button addData = (Button) findViewById(R.id.add_data);
        Button updateData = (Button) findViewById(R.id.update_data);
        Button deleteData = (Button) findViewById(R.id.delete_data);
        Button queryData = (Button) findViewById(R.id.query_data);
        Button replaceData = (Button) findViewById(R.id.replace_data);

        createDatabase.setOnClickListener(this);
        addData.setOnClickListener(this);
        updateData.setOnClickListener(this);
        deleteData.setOnClickListener(this);
        queryData.setOnClickListener(this);
        replaceData.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_database:
                dbHelper.getWritableDatabase();
                break;

            case R.id.add_data:
                SQLiteDatabase dbAdd = dbHelper.getWritableDatabase();
                ContentValues valuesAdd = new ContentValues();
                valuesAdd.put("name", "The Da Vinci Code");
                valuesAdd.put("author", "Dan Brown");
                valuesAdd.put("pages", 454);
                valuesAdd.put("price", 16.96);
                dbAdd.insert("Book", null, valuesAdd);

                valuesAdd.clear();
                valuesAdd.put("name", "The Lost Symbol");
                valuesAdd.put("author", "Dan Brown");
                valuesAdd.put("pages", 510);
                valuesAdd.put("price", 19.95);
                dbAdd.insert("Book", null, valuesAdd);
                break;

            case R.id.update_data:
                SQLiteDatabase dbUpdate = dbHelper.getWritableDatabase();
                ContentValues valuesUpdate = new ContentValues();
                valuesUpdate.put("price", 10.99);
                dbUpdate.update("Book", valuesUpdate, "name=?", new String[]{"The Da Vinci Code"});
                break;

            case R.id.delete_data:
                SQLiteDatabase dbDelete = dbHelper.getWritableDatabase();
                dbDelete.delete("Book", "pages > ?", new String[]{"500"});
                break;

            case R.id.query_data:
                SQLiteDatabase dbQuery = dbHelper.getReadableDatabase();
                Cursor cursor = dbQuery.query(null, null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.d(TAG, "book name is " + name);
                        Log.d(TAG, "book author is " + author);
                        Log.d(TAG, "book pages is " + pages);
                        Log.d(TAG, "book price is " + price);
                    } while (cursor.moveToNext());
                }
                break;

            case R.id.replace_data:
                SQLiteDatabase dbReplace = dbHelper.getWritableDatabase();
                dbReplace.beginTransaction();
                try {
                    dbReplace.delete("Book", null, null);
//                    if (true) {
//                        throw new NullPointerException();
//                    }
                    ContentValues valuesReplace = new ContentValues();
                    valuesReplace.put("name", "Game of Thrones");
                    valuesReplace.put("author", "George Martin");
                    valuesReplace.put("pages", 720);
                    valuesReplace.put("price", 20.85);
                    dbReplace.insert("Book", null, valuesReplace);
                    dbReplace.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    dbReplace.endTransaction();
                }
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
