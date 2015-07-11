package com.acktos.easylaundry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Acktos on 11/06/15.
 */
public class LaundryDbHelper extends SQLiteOpenHelper {


    // Database Name
    public static final String DATABASE_NAME = "laundry.db";

    // Database Version
    public static final int DATABASE_VERSION = 1;

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_CATEGORIES =
            "CREATE TABLE " + LaundryContract.CategoriesTable.TABLE_NAME + " (" +
                    LaundryContract.CategoriesTable._ID + " INTEGER PRIMARY KEY," +
                    LaundryContract.CategoriesTable.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    LaundryContract.CategoriesTable.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    LaundryContract.CategoriesTable.COLUMN_NAME_THUMBNAIL + TEXT_TYPE + " )";

    private static final String SQL_DELETE_CATEGORIES =
            "DROP TABLE IF EXISTS " + LaundryContract.CategoriesTable.TABLE_NAME;

    private static final String SQL_CREATE_CLOTHES =
            "CREATE TABLE " + LaundryContract.ClothesTable.TABLE_NAME + " (" +
                    LaundryContract.ClothesTable._ID + " INTEGER PRIMARY KEY," +
                    LaundryContract.ClothesTable.COLUMN_NAME_CATEGORY_ID + INTEGER_TYPE + COMMA_SEP +
                    LaundryContract.ClothesTable.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    LaundryContract.ClothesTable.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    LaundryContract.ClothesTable.COLUMN_NAME_PRICE + TEXT_TYPE + COMMA_SEP +
                    LaundryContract.ClothesTable.COLUMN_NAME_THUMBNAIL + TEXT_TYPE+")";

    private static final String SQL_DELETE_CLOTHES =
            "DROP TABLE IF EXISTS " + LaundryContract.ClothesTable.TABLE_NAME;

    public LaundryDbHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CATEGORIES);
        db.execSQL(SQL_CREATE_CLOTHES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(SQL_DELETE_CATEGORIES);
        db.execSQL(SQL_DELETE_CLOTHES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
