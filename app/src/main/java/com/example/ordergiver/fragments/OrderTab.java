package com.example.ordergiver.fragments;


import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ordergiver.R;
import com.example.ordergiver.entity.Order;
import com.example.ordergiver.java.OrderAdapter;
import com.example.ordergiver.manager.OrderManager;

import java.text.Normalizer;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class OrderTab extends Fragment
{
    // Recycler View
    private ArrayList<Order> ordersList;
    private RecyclerView orderRecyclerView;
    private OrderAdapter orderAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private OrderManager orderManager;
    private Dialog dialog;
    private Button addOrder;
    private String orderName;
    private ViewGroup rotationView;

    // control
    boolean isTouchingButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        initAllAttributes(inflater, container);
        initRecyclerView();
        return rotationView;
    }

    /* Getters */
    public OrderManager getOrderManager() { return orderManager; }
    public Button getAddOrder() { return addOrder; }
    public OrderAdapter getOrderAdapter() { return orderAdapter; }
    public ArrayList<Order> getOrdersList() { return ordersList; }
    public Dialog getDialog() { return dialog; }
    public String getOrderName() { return orderName; }
    public ViewGroup getRotationView() { return rotationView; }
    public boolean getIsTouchingButton() { return  isTouchingButton; }

    /* Setters */
    private void initAllAttributes(LayoutInflater inflater, ViewGroup container)
    {
        dialog = new Dialog(getActivity());
        rotationView = (ViewGroup) inflater.inflate(R.layout.order_tab, container, false);
        orderManager = new OrderManager(getActivity());
        addOrder = rotationView.findViewById(R.id.btn_add);
        // Recycler view
        mLayoutManager = new LinearLayoutManager(getContext());
        orderRecyclerView = getRotationView().findViewById(R.id.recyclerView);
        orderRecyclerView.setLayoutManager(mLayoutManager);
        orderRecyclerView.setHasFixedSize(true);
        isTouchingButton = false;
    }

    private void initRecyclerView()
    {
        ordersList = getOrderManager().getOrders();
        orderAdapter = new OrderAdapter(ordersList);
        orderRecyclerView.setAdapter(orderAdapter);

        addEventListeners();
    }

    private void setOrderName(String order) { orderName = order; }
    private void setIsTouchingButton(boolean touching) { isTouchingButton = touching; }

    /* Listeners */

    private void addEventListeners()
    {
        getAddOrder().setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {
                showPopup(-1);
            }
        });

        getOrderAdapter().setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int position) {
                showPopup(position);
            }
            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });
    }

    /* Methods */

    public void removeItem (int position)
    {
        Order order = getOrderManager().getOrder(getOrderAdapter().getElemByPosition(position));
        getOrderManager().delete(order);
        getOrdersList().remove(position);
        getOrderAdapter().notifyItemRemoved(position);
    }

    private void showPopup(final int position)
    {
        Button btnClose, btnAccept;
        final EditText txtOrder;
        final Order order;
        final int orderId;

        getDialog().setContentView(R.layout.add_order_popup);
        btnClose = getDialog().findViewById(R.id.btn_close);
        btnAccept = getDialog().findViewById(R.id.btn_valider);
        txtOrder = getDialog().findViewById(R.id.txt_order);

        // Récupération text item cliqué
        if (-1 != position) {
            order =  getOrderManager().getOrder(getOrderAdapter().getElemByPosition(position));
            txtOrder.setText(order.getOrderMessage());
            orderId = order.getOrderId();
        } else {
            // Instanciation d'un nouvel ordre
            order = new Order();
            orderId = -1;
        }

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                getDialog().dismiss();
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (!getIsTouchingButton()) {
                    setIsTouchingButton(true);
                    editOrder(order, txtOrder, orderId, position);
                    initRecyclerView();
                }
            }
        });

        getDialog().show();
    }

    public void editOrder(Order order, EditText txtOrder, int orderId, int position)
    {
        if (txtOrder.getText().toString().trim().equals("")) {
            finalEditAction("Veuillez saisir l'ordre .", false);
            return;
        }

        setOrderName(normalize(txtOrder.getText().toString()));
        order.setOrderMessage(getOrderName());

        if (getOrderManager().checkOrderExist(getOrderName(), orderId)) {
            finalEditAction("Cet ordre existe déjà .", false);
            return;
        }

        String message = "";

        if (-1 == position) {
            // Création d'un nouvel ordre
            getOrderManager().create(order);
            message = "Ordre créé.";
        } else {
            // Edition d'un ordre
            getOrderManager().update(order);
            message = "Ordre modifié.";
        }

        finalEditAction(message, true);
    }

    public void finalEditAction(final String str, final boolean close)
    {
        Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setIsTouchingButton(false);

                if (close) {
                    getDialog().dismiss();
                }
            }
        }, 3000);
    }

    public String normalize(String str)
    {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return str.trim().toLowerCase();
    }
}