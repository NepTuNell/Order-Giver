package com.example.ordergiver.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.example.ordergiver.database.DatabaseSQLLite;
import com.example.ordergiver.entity.Verb;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class VerbManager
{

    //****************************
    // Attributes
    //****************************
    private DatabaseSQLLite mDB;
    private SQLiteDatabase mManager;


    /**
     *  Inner class that defines the table contents
     */
    public static class FeedOrder implements BaseColumns
    {
        public static final String TABLE_NAME = "t_verb";
        public static final String KEY_ID_VERB = "id_verb";
        public static final String KEY_VERB_INFINITIVE = "infinitive_verb";
        public static final String KEY_VERB_IMPERATIVE_FIRST = "imperative_verb_first";
        public static final String KEY_VERB_IMPERATIVE_SECOND = "imperative_verb_second";
        public static final String KEY_VERB_IMPERATIVE_THIRD = "imperative_verb_third";
    }

    // Constructor
    public VerbManager(Context context) {
        mDB = DatabaseSQLLite.getInstance(context);
    }

    //****************************
    // Methods which manage DB
    //****************************

    /**
     * Return the manager
     */
    public SQLiteDatabase getManager()
    {
        return mManager;
    }

    /**
     * Open database in write mod
     */
    public void openWriteMod()
    {
        mManager = mDB.getWritableDatabase();
    }

    /**
     * Open database in read mod
     */
    public void openReadMod()
    {
        mManager = mDB.getReadableDatabase();
    }

    /**
     * Close the database
     */
    public void close()
    {
        mManager.close();
    }

    //****************************
    // Methods of CRUD and others
    //****************************

    /**
     * Count all verbs of the table
     */
    public int countEntries()
    {
        openReadMod();
        int entries = 0;

        String query = "SELECT COUNT(*) FROM "+FeedOrder.TABLE_NAME;
        Cursor cursor = getManager().rawQuery(query, null);

        if (cursor.moveToFirst()) {
            entries = cursor.getInt(0);
        }

        cursor.close();
        close();

        return entries;
    }

    /**
     * Check if an verb exists
     */
    public boolean checkInfinitiveExist(@NotNull String infinitive, int verbID)
    {
        openReadMod();

        boolean exist = true;
        String query = "SELECT * FROM "+ FeedOrder.TABLE_NAME+" WHERE "+ FeedOrder.KEY_VERB_INFINITIVE+"="+DatabaseUtils.sqlEscapeString(infinitive);

        if (-1 != verbID) {
            query += " AND "+ FeedOrder.KEY_ID_VERB+" <> "+verbID;
        }

        Cursor cursor = getManager().rawQuery(query, null);

        if (!cursor.moveToFirst()) {
            exist = false;
        }

        cursor.close();
        close();

        return exist;
    }

    /**
     * Create new verb
     */
    public void create(@NotNull Verb verb)
    {
        openWriteMod();

        ContentValues values = new ContentValues();
        values.put(FeedOrder.KEY_VERB_INFINITIVE, verb.getInfinitiveVerb());
        values.put(FeedOrder.KEY_VERB_IMPERATIVE_FIRST, verb.getImperativeVerbFirst());
        values.put(FeedOrder.KEY_VERB_IMPERATIVE_SECOND, verb.getImperativeVerbSecond());
        values.put(FeedOrder.KEY_VERB_IMPERATIVE_THIRD, verb.getImperativeVerbThird());
        getManager().insert(FeedOrder.TABLE_NAME,null, values);

        close();
    }

    /**
     * Fetch a verb by using imperative
     */
    public String getInfinitiveVerbByString(@NotNull String verbToFind)
    {
        openReadMod();

        String query = "SELECT * FROM "+ FeedOrder.TABLE_NAME+" WHERE "+ FeedOrder.KEY_VERB_INFINITIVE+"="+DatabaseUtils.sqlEscapeString(verbToFind);
        Cursor cursor = getManager().rawQuery(query, null);

        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(FeedOrder.KEY_VERB_INFINITIVE));
        }

        cursor.close();

        query = "SELECT * FROM "+ FeedOrder.TABLE_NAME+" WHERE "+ FeedOrder.KEY_VERB_IMPERATIVE_FIRST+"="+DatabaseUtils.sqlEscapeString(verbToFind);
        cursor = getManager().rawQuery(query, null);

        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(FeedOrder.KEY_VERB_INFINITIVE));
        }

        cursor.close();

        query = "SELECT * FROM "+ FeedOrder.TABLE_NAME+" WHERE "+ FeedOrder.KEY_VERB_IMPERATIVE_SECOND+"="+DatabaseUtils.sqlEscapeString(verbToFind);
        cursor = getManager().rawQuery(query, null);

        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(FeedOrder.KEY_VERB_INFINITIVE));
        }

        cursor.close();

        query = "SELECT * FROM "+ FeedOrder.TABLE_NAME+" WHERE "+ FeedOrder.KEY_VERB_IMPERATIVE_THIRD+"="+DatabaseUtils.sqlEscapeString(verbToFind);
        cursor = getManager().rawQuery(query, null);

        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(FeedOrder.KEY_VERB_INFINITIVE));
        }

        cursor.close();

        close();
        return "";
    }

    /**
     * Fetch all verbs
     */
    public ArrayList<Verb> getVerbs()
    {
        openReadMod();
        ArrayList<Verb> verbItems = new ArrayList<Verb>();
        Cursor cursor = getManager().rawQuery("SELECT * FROM " + FeedOrder.TABLE_NAME, null);

        while (cursor.moveToNext()) {
            Verb verb = new Verb();
            verb.setVerbId(cursor.getInt(cursor.getColumnIndex(FeedOrder.KEY_ID_VERB)));
            verb.setInfinitiveVerb(cursor.getString(cursor.getColumnIndex(FeedOrder.KEY_VERB_INFINITIVE)));
            verb.setImperativeVerbFirst(cursor.getString(cursor.getColumnIndex(FeedOrder.KEY_VERB_IMPERATIVE_FIRST)));
            verb.setImperativeVerbSecond(cursor.getString(cursor.getColumnIndex(FeedOrder.KEY_VERB_IMPERATIVE_SECOND)));
            verb.setImperativeVerbThird(cursor.getString(cursor.getColumnIndex(FeedOrder.KEY_VERB_IMPERATIVE_THIRD)));
            verbItems.add(verb);
        }

        cursor.close();
        close();

        return verbItems;
    }
}
