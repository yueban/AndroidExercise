package com.bigfat.databasetest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2014/12/8
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_BOOK = "create table Book ("
            + "id integer primary key autoincrement,"
            + "author text,"
            + "price real,"
            + "pages integer,"
            + "name text,"
            + "category_id integer)";

    public static final String CREATE_CATEGORY = "create table Category ("
            + "id integer primary key autoincrement,"
            + "category_name text,"
            + "category_code integer)";


    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);
        db.execSQL(CREATE_CATEGORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL(CREATE_CATEGORY);

            case 2:
                db.execSQL("alter table Book add column category_id integer");

            default:
        }
    }
}
