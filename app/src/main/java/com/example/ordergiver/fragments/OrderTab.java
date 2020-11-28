package com.example.ordergiver.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ordergiver.R;
import com.example.ordergiver.entity.Order;
import com.example.ordergiver.manager.OrderManager;

public class OrderTab extends Fragment
{
    private OrderManager orderManager;

    public OrderTab()
    {
        this.orderManager = new OrderManager(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rotationView = (ViewGroup) inflater.inflate(R.layout.order_tab, container, false);

        return rotationView;
    }
}