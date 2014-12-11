package com.bigfat.databasetest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2014/12/8
 */
public class DatabaseProvider extends ContentProvider {

    public static final int BOOK_DIR = 0;
    public static final int BOOK_ITEM = 1;
    public static final int CATEGORY_DIR = 2;
    public static final int CATEGORY_ITEM = 3;

    public static final String AUTHORITY = "com.bigfat.databasetest.provider";

    private static UriMatcher uriMatcher;
    private MyDatabaseHelper dbHelper;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "book", BOOK_DIR);
        uriMatcher.addURI(AUTHORITY, "book/#", BOOK_ITEM);
        uriMatcher.addURI(AUTHORITY, "category", CATEGORY_DIR);
        uriMatcher.addURI(AUTHORITY, "category/#", CATEGORY_ITEM);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new MyDatabaseHelper(getContext(), "BookStore.db", null, 2);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                cursor = db.query("Book", projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                cursor = db.query("Book", projection, "id = ?", new String[]{bookId}, null, null, sortOrder);
                break;

            case CATEGORY_DIR:
                cursor = db.query("Category", projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                cursor = db.query("Category", projection, "id = ?", new String[]{categoryId}, null, null, sortOrder);
                break;

            default:
                break;
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd.com.bigfat.databasetest.provider.book";

            case BOOK_ITEM:
                return "vnd.android.cursor.item/vnd.com.bigfat.databasetest.provider.book";

            case CATEGORY_DIR:
                return "vnd.android.cursor.dir/vnd.com.bigfat.databasetest.provider.category";

            case CATEGORY_ITEM:
                return "vnd.android.cursor.item/vnd.com.bigfat.databasetest.provider.category";

            default:
                break;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
            case BOOK_ITEM:
                long newBookId = db.insert("Book", null, values);
                return Uri.parse("content://" + AUTHORITY + "/book/" + newBookId);

            case CATEGORY_DIR:
            case CATEGORY_ITEM:
                long newCategoryId = db.insert("Category", null, values);
                return Uri.parse("content://" + AUTHORITY + "/category/" + newCategoryId);

            default:
                break;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deletedRows = 0;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                deletedRows = db.delete("Book", selection, selectionArgs);
                break;

            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                deletedRows = db.delete("Book", "id = ?", new String[]{bookId});
                break;

            case CATEGORY_DIR:
                deletedRows = db.delete("Category", selection, selectionArgs);
                break;

            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                deletedRows = db.delete("Category", "id = ?", new String[]{categoryId});
                break;

            default:
                break;
        }
        return deletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updatedRows = 0;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                updatedRows = db.update("Book", values, selection, selectionArgs);
                break;

            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                updatedRows = db.update("Book", values, "id = ?", new String[]{bookId});
                break;

            case CATEGORY_DIR:
                updatedRows = db.update("Category", values, selection, selectionArgs);
                break;

            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                updatedRows = db.update("Category", values, "id = ?", new String[]{categoryId});
                break;

            default:
                break;
        }
        return updatedRows;
    }
}
