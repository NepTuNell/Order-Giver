package com.example.ordergiver.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ordergiver.manager.VerbManager;
import com.example.ordergiver.manager.OrderManager;

public class DatabaseSQLLite extends SQLiteOpenHelper
{
    //****************************
    // Attributes
    //****************************

    private static DatabaseSQLLite sInstance;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "order-giver.db";

    // Create table statement
    private static final String SQL_CREATE_ENTRIES_ORDER =
            "CREATE TABLE "+ OrderManager.FeedOrder.TABLE_NAME+" ("+
                    OrderManager.FeedOrder.KEY_ID_ORDER+" INTEGER PRIMARY KEY, "+
                    OrderManager.FeedOrder.KEY_NOM_ORDER+" TEXT)";

    private static final String SQL_CREATE_ENTRIES_VERB =
            "CREATE TABLE "+ VerbManager.FeedOrder.TABLE_NAME+
                " ("+
                    VerbManager.FeedOrder.KEY_ID_VERB+" INTEGER PRIMARY KEY, "+
                    VerbManager.FeedOrder.KEY_VERB_INFINITIVE+" TEXT NOT NULL, "+
                    VerbManager.FeedOrder.KEY_VERB_IMPERATIVE_FIRST+" TEXT NOT NULL, "+
                    VerbManager.FeedOrder.KEY_VERB_IMPERATIVE_SECOND+" TEXT NOT NULL, "+
                    VerbManager.FeedOrder.KEY_VERB_IMPERATIVE_THIRD+" TEXT NOT NULL" +
                ")";

    // Delete table statement
    private static final String SQL_DELETE_ENTRIES_ORDER = "DROP TABLE IF EXISTS " + OrderManager.FeedOrder.TABLE_NAME;
    private static final String SQL_DELETE_ENTRIES_VERB = "DROP TABLE IF EXISTS " + VerbManager.FeedOrder.TABLE_NAME;

    // Constructor
    public DatabaseSQLLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //****************************
    // Methods
    //****************************

    /**
     * Save the instance
     */
    public static synchronized DatabaseSQLLite getInstance(Context context)
    {
        if (sInstance == null) {
            sInstance = new DatabaseSQLLite(context);
        }

        return sInstance;
    }

    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_ENTRIES_ORDER);
        db.execSQL(SQL_CREATE_ENTRIES_VERB);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(SQL_DELETE_ENTRIES_ORDER);
        db.execSQL(SQL_DELETE_ENTRIES_VERB);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onUpgrade(db, oldVersion, newVersion);
    }
}