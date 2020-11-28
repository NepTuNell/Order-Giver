package com.example.ordergiver.entity;

public class Order {

    private int orderId;
    private String orderMessage;

    public Order(int orderId, String orderMessage) {
        this.orderId = orderId;
        this.orderMessage = orderMessage;
    }

    public int getOrderId() {
        return this.orderId;
    }

    public void setOrderId(int orderId)
    {
        this.orderId = orderId;
    }

    public String getOrderMessage()
    {
        return this.orderMessage;
    }

    public void setOrderMessage(String orderMessage)
    {
        this.orderMessage = orderMessage;
    }
}
