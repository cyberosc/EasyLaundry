package com.acktos.easylaundry.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.acktos.easylaundry.LaundryContract;
import com.acktos.easylaundry.LaundryDbHelper;

/**
 * Created by Acktos on 12/06/15.
 */
public class EasyLaundryProvider extends ContentProvider {

    private LaundryDbHelper database;

    public static final String AUTHORITY="com.acktos.easylaudry.provider";
    public static final Uri URI_CATEGORIES=Uri.parse("content://"+AUTHORITY+"/"+ LaundryContract.CategoriesTable.TABLE_NAME);
    public static final Uri URI_CLOTHES=Uri.parse("content://"+AUTHORITY+"/"+ LaundryContract.ClothesTable.TABLE_NAME);

    public static final int ID_URI_ALL_CATEGORIES=10;
    public static final int ID_URI_ALL_CLOTHES=30;
    public static final int ID_URI_CLOTHES_PER_CATEGORY=20;


    // Creates a UriMatcher object.
    private static final UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, LaundryContract.CategoriesTable.TABLE_NAME, ID_URI_ALL_CATEGORIES);
        uriMatcher.addURI(AUTHORITY, LaundryContract.ClothesTable.TABLE_NAME, ID_URI_ALL_CLOTHES);
        uriMatcher.addURI(AUTHORITY,
                LaundryContract.ClothesTable.TABLE_NAME+"/"+ LaundryContract.CategoriesTable.TABLE_NAME+"/#",
                ID_URI_CLOTHES_PER_CATEGORY);
    }

    @Override
    public boolean onCreate() {

        database=new LaundryDbHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // Using SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();


        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case ID_URI_ALL_CATEGORIES:

                // Set categories table
                queryBuilder.setTables(LaundryContract.CategoriesTable.TABLE_NAME);
                break;

            case ID_URI_CLOTHES_PER_CATEGORY:
                //set clothes table
                queryBuilder.setTables(LaundryContract.ClothesTable.TABLE_NAME);
                queryBuilder.appendWhere(LaundryContract.ClothesTable.COLUMN_NAME_CATEGORY_ID+"="+
                        uri.getLastPathSegment());
                break;


            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        Log.i("query builder:",queryBuilder.toString());
        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        int uriType = uriMatcher.match(uri);
        SQLiteDatabase db = database.getWritableDatabase();
        String tableName;

        long id;
        switch (uriType) {

            case ID_URI_ALL_CATEGORIES:
                tableName=LaundryContract.CategoriesTable.TABLE_NAME;
                id = db.insert(tableName, null, values);
                break;

            case ID_URI_ALL_CLOTHES:
                tableName=LaundryContract.ClothesTable.TABLE_NAME;
                id = db.insert(tableName, null, values);
                break;

            case ID_URI_CLOTHES_PER_CATEGORY:

                tableName=LaundryContract.ClothesTable.TABLE_NAME;
                id = db.insert(tableName, null, values);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(tableName + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int uriType = uriMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted;

        switch (uriType) {

            case ID_URI_ALL_CATEGORIES:
                rowsDeleted = sqlDB.delete(LaundryContract.CategoriesTable.TABLE_NAME, selection, selectionArgs);
                break;

            case ID_URI_ALL_CLOTHES:
                rowsDeleted = sqlDB.delete(LaundryContract.ClothesTable.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
