package com.example.ordergiver.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ordergiver.manager.OrderManager;

public class DatabaseSQLLite extends SQLiteOpenHelper {
    private static DatabaseSQLLite sInstance;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "order-giver.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE "+ OrderManager.FeedOrder.TABLE_NAME+" ("+
                    OrderManager.FeedOrder.KEY_ID_ORDER+" INTEGER PRIMARY KEY, "+
                    OrderManager.FeedOrder.KEY_NOM_ORDER+" TEXT)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + OrderManager.FeedOrder.TABLE_NAME;

    public static synchronized DatabaseSQLLite getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseSQLLite(context);
        }

        return sInstance;
    }

    public DatabaseSQLLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}