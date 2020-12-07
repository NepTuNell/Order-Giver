package com.example.ordergiver.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.example.ordergiver.entity.Order;
import com.example.ordergiver.database.DatabaseSQLLite;

import java.util.ArrayList;

public class OrderManager {

    private DatabaseSQLLite db;
    private SQLiteDatabase manager;

    /*****************************************
     *         Création de l'instance
     *****************************************/

    /* Inner class that defines the table contents */
    public static class FeedOrder implements BaseColumns {
        public static final String TABLE_NAME = "t_order";
        public static final String KEY_ID_ORDER = "id_order";
        public static final String KEY_NOM_ORDER = "order_message";
    }

    // Constructeur
    public OrderManager(Context context) {
        this.db = DatabaseSQLLite.getInstance(context);
    }

    /*****************************************
     *     Gestion de la base de données
     *****************************************/

    public SQLiteDatabase getManager()
    {
        return this.manager;
    }

    public void openWriteMod()
    {
        this.manager = this.db.getWritableDatabase();
    }

    public void openReadMod()
    {
        this.manager = this.db.getReadableDatabase();
    }

    public void close()
    {
        this.manager.close();
    }

    /*****************************************
     *           Méthodes du CRUD
     *****************************************/

    public void create(Order order) {
        openWriteMod();

        ContentValues values = new ContentValues();
        values.put(FeedOrder.KEY_NOM_ORDER, order.getOrderMessage());
        getManager().insert(FeedOrder.TABLE_NAME,null,values);

        close();
    }

    public void update(Order order) {
        openWriteMod();

        ContentValues values = new ContentValues();
        values.put(FeedOrder.KEY_NOM_ORDER, order.getOrderMessage());
        String where = FeedOrder.KEY_ID_ORDER+" = ?";
        String[] whereArgs = {""+order.getOrderId()};
        getManager().update(FeedOrder.TABLE_NAME, values, where, whereArgs);

        close();
    }

    public void delete(Order order) {
        openWriteMod();

        String where = FeedOrder.KEY_ID_ORDER+" = ?";
        String[] whereArgs = {""+order.getOrderId()};
        getManager().delete(FeedOrder.TABLE_NAME, where, whereArgs);

        close();
    }

    public Order getOrder(int id) {
        openReadMod();
        Order order=new Order(0,"");

        Cursor c = manager.rawQuery("SELECT * FROM "+FeedOrder.TABLE_NAME+" WHERE "+FeedOrder.KEY_ID_ORDER+"="+id, null);
        if (c.moveToFirst()) {
            order.setOrderId(c.getInt(c.getColumnIndex(FeedOrder.KEY_ID_ORDER)));
            order.setOrderMessage(c.getString(c.getColumnIndex(FeedOrder.KEY_NOM_ORDER)));
        }

        c.close();
        close();

        return order;
    }

    public ArrayList<Order> getOrders() {
        openReadMod();
        ArrayList<Order> ordersItems = new ArrayList<Order>();
        Cursor cursor = manager.rawQuery("SELECT * FROM " + FeedOrder.TABLE_NAME, null);

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
}
