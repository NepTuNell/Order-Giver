package com.example.ordergiver.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.example.ordergiver.entity.Order;
import com.example.ordergiver.helper.OrderHelper;

public class OrderManager {

    private OrderHelper db;
    private SQLiteDatabase manager;

    /* Inner class that defines the table contents */
    public static class FeedOrder implements BaseColumns {
        public static final String TABLE_NAME = "t_order";
        public static final String KEY_ID_ORDER = "id_order";
        public static final String KEY_NOM_ORDER = "order_message";
    }

    // Constructeur
    public OrderManager(Context context) {
        this.db = OrderHelper.getInstance(context);
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

    public long create(Order order) {
        ContentValues values = new ContentValues();
        values.put(FeedOrder.KEY_NOM_ORDER, order.getOrderMessage());

        return manager.insert(FeedOrder.TABLE_NAME,null,values);
    }

    public int update(Order order) {
        ContentValues values = new ContentValues();
        values.put(FeedOrder.KEY_NOM_ORDER, order.getOrderMessage());
        String where = FeedOrder.KEY_ID_ORDER+" = ?";
        String[] whereArgs = {""+order.getOrderId()};

        return manager.update(FeedOrder.TABLE_NAME, values, where, whereArgs);
    }

    public int delete(Order order) {
        String where = FeedOrder.KEY_ID_ORDER+" = ?";
        String[] whereArgs = {""+order.getOrderId()};

        return manager.delete(FeedOrder.TABLE_NAME, where, whereArgs);
    }

    public Order getOrder(int id) {
        Order order=new Order(0,"");

        Cursor c = manager.rawQuery("SELECT * FROM "+FeedOrder.TABLE_NAME+" WHERE "+FeedOrder.KEY_ID_ORDER+"="+id, null);
        if (c.moveToFirst()) {
            order.setOrderId(c.getInt(c.getColumnIndex(FeedOrder.KEY_ID_ORDER)));
            order.setOrderMessage(c.getString(c.getColumnIndex(FeedOrder.KEY_NOM_ORDER)));
            c.close();
        }

        return order;
    }

    public Cursor getOrders() {
        return manager.rawQuery("SELECT * FROM "+FeedOrder.TABLE_NAME,null);
    }
}
