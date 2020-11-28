package com.example.ordergiver.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ordergiver.R;
import com.example.ordergiver.entity.Order;
import com.example.ordergiver.manager.OrderManager;

public class OrderTab extends Fragment
{
    private OrderManager orderManager;
    private Dialog dialog;
    private Activity activity;
    private Button addOrder;
    private String orderName;

    public OrderTab(Activity activity)
    {
        this.orderManager = new OrderManager(getContext());
        this.activity = activity;
        dialog = new Dialog(this.activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rotationView = (ViewGroup) inflater.inflate(R.layout.order_tab, container, false);

        addOrder = rotationView.findViewById(R.id.btn_add);

        addOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showPopup(view);
            }
        });

        return rotationView;
    }

    private void showPopup(View view)
    {
        Button btnClose, btnValider;
        final EditText txt_order;

        dialog.setContentView(R.layout.add_order_popup);

        btnClose = dialog.findViewById(R.id.btn_close);
        btnValider = dialog.findViewById(R.id.btn_valider);
        txt_order = dialog.findViewById(R.id.txt_order);

        btnClose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });

        btnValider.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                orderName = txt_order.getText().toString();
                Toast.makeText(activity, orderName, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}