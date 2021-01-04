package com.example.ordergiver.entity;

public class Order
{
    //****************************
    // Attributes
    //****************************

    private int mOrderId;
    private String mOrderMessage;

    // Constructor
    public Order(int orderId, String orderMessage) {
        mOrderId = orderId;
        mOrderMessage = orderMessage;
    }

    public Order() {}

    //****************************
    // Accessors
    //****************************

    // Getters
    public int getOrderId() { return mOrderId; }
    public String getOrderMessage() { return mOrderMessage; }

    // Setters
    public void setOrderId(int orderId) { mOrderId = orderId; }
    public void setOrderMessage(String orderMessage) { mOrderMessage = orderMessage; }
}
