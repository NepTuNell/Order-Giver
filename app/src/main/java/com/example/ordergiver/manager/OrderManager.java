package com.example.ordergiver.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.example.ordergiver.entity.Order;
import com.example.ordergiver.database.DatabaseSQLLite;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OrderManager
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
        public static final String TABLE_NAME = "t_order";
        public static final String KEY_ID_ORDER = "id_order";
        public static final String KEY_NOM_ORDER = "order_message";
    }

    // Constructor
    public OrderManager(Context context) {
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
     * Create new order
     */
    public void create(@NotNull Order order)
    {
        openWriteMod();

        ContentValues values = new ContentValues();
        values.put(FeedOrder.KEY_NOM_ORDER, order.getOrderMessage());
        getManager().insert(FeedOrder.TABLE_NAME,null,values);

        close();
    }

    /**
     * Update order
     */
    public void update(@NotNull Order order)
    {
        openWriteMod();

        ContentValues values = new ContentValues();
        values.put(FeedOrder.KEY_NOM_ORDER, order.getOrderMessage());
        String where = FeedOrder.KEY_ID_ORDER+" = ?";
        String[] whereArgs = {""+order.getOrderId()};
        getManager().update(FeedOrder.TABLE_NAME, values, where, whereArgs);

        close();
    }

    /**
     * Delete order
     */
    public void delete(@NotNull Order order)
    {
        openWriteMod();

        String where = FeedOrder.KEY_NOM_ORDER+" = ?";
        String[] whereArgs = {""+ order.getOrderMessage()};
        getManager().delete(FeedOrder.TABLE_NAME, where, whereArgs);

        close();
    }

    /**
     * Fetch an order
     */
    public Order getOrder(String orderMessage)
    {
        openReadMod();
        Order order = new Order(0,"");

        Cursor c = getManager().rawQuery("SELECT * FROM "+FeedOrder.TABLE_NAME+" WHERE "+FeedOrder.KEY_NOM_ORDER+"="+DatabaseUtils.sqlEscapeString(orderMessage), null);
        if (c.moveToFirst()) {
            order.setOrderId(c.getInt(c.getColumnIndex(FeedOrder.KEY_ID_ORDER)));
            order.setOrderMessage(c.getString(c.getColumnIndex(FeedOrder.KEY_NOM_ORDER)));
        }

        c.close();
        close();

        return order;
    }

    /**
     * Fetch all orders
     */
    public ArrayList<Order> getOrders()
    {
        openReadMod();
        ArrayList<Order> ordersItems = new ArrayList<Order>();
        Cursor cursor = getManager().rawQuery("SELECT * FROM " + FeedOrder.TABLE_NAME, null);

        while (cursor.moveToNext()) {
            Order order = new Order();
            order.setOrderId(cursor.getInt(cursor.getColumnIndex(FeedOrder.KEY_ID_ORDER)));
            order.setOrderMessage(cursor.getString(cursor.getColumnIndex(FeedOrder.KEY_NOM_ORDER)));
            ordersItems.add(order);
        }

        cursor.close();
        close();

        return ordersItems;
    }

    /**
     * Check if the order already exists
     */
    public boolean checkOrderExist(String orderName, int orderId) {
        openReadMod();

        boolean exist = false;
        String query = "SELECT * FROM "+FeedOrder.TABLE_NAME+" WHERE "+FeedOrder.KEY_NOM_ORDER+" = "+DatabaseUtils.sqlEscapeString(orderName);

        if (-1 != orderId) {
            query += " AND "+FeedOrder.KEY_ID_ORDER+" <> "+orderId;
        }

        Cursor cursor = getManager().rawQuery(query, null);

        if (cursor.moveToFirst()) {
            exist = true;
        }

        cursor.close();
        close();

        return exist;
    }
}
