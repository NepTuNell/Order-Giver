package com.example.ordergiver.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ordergiver.R;
import com.example.ordergiver.entity.Order;
import com.example.ordergiver.java.OrderAdapter;
import com.example.ordergiver.manager.OrderManager;

import java.util.ArrayList;

public class OrderTab extends Fragment
{
    // Recycler View
    private ArrayList<Order> ordersList;
    private RecyclerView orderRecyclerView;
    private OrderAdapter orderAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private OrderManager orderManager;
    private Dialog dialog;
    private Activity activity;
    private Button addOrder;
    private String orderName;

    private ViewGroup rotationView;


    /*****************************************
     *         Création de l'instance
     *****************************************/

    /**
     * Constructor
     */
    public OrderTab(Activity activity)
    {
        this.orderManager = new OrderManager(activity);
        this.activity = activity;
        dialog = new Dialog(this.activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        this.rotationView = (ViewGroup) inflater.inflate(R.layout.order_tab, container, false);
        addOrder = this.rotationView.findViewById(R.id.btn_add);
        addOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showPopup(-1);
            }
        });
        initRecyclerView(this.rotationView);
        return rotationView;
    }

    /*****************************************
     *           Accesseurs
     *****************************************/

    public OrderManager getOrderManager()
    {
        return this.orderManager;
    }

    /*****************************************
     *    Méthodes de la liste des ordres
     *****************************************/

    private void initRecyclerView (View view) {
        // Initialisation de la liste des ordres
        this.ordersList = getOrderManager().getOrders();
        // Création du recyclerView
        this.orderRecyclerView = view.findViewById(R.id.recyclerView);
        this.orderRecyclerView.setHasFixedSize(true);
        // Adaptation de la liste des ordres pour le recycler
        this.mLayoutManager = new LinearLayoutManager(activity.getBaseContext());
        this.orderAdapter = new OrderAdapter(ordersList);
        this.orderRecyclerView.setLayoutManager(mLayoutManager);
        this.orderRecyclerView.setAdapter(this.orderAdapter);

        // Ajout d'un listener
        this.orderAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
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

    public void removeItem (int position) {
        Order order = this.orderManager.getOrder(this.orderAdapter.getElemByPosition(position));
        this.orderManager.delete(order);
        this.ordersList.remove(position);
        this.orderAdapter.notifyItemRemoved(position);
    }

    /*****************************************
     *    Méthodes de la popup new/edit
     *****************************************/

    private void showPopup(final int position)
    {
        Button btnClose, btnAccept;
        final EditText txtOrder;
        final Order order;

        dialog.setContentView(R.layout.add_order_popup);
        btnClose = dialog.findViewById(R.id.btn_close);
        btnAccept = dialog.findViewById(R.id.btn_valider);

        // Input popup saisie ordre
        txtOrder = dialog.findViewById(R.id.txt_order);
        // Récupération text item cliqué
        if (-1 != position) {
            order = this.orderManager.getOrder(this.orderAdapter.getElemByPosition(position));
            txtOrder.setText(order.getOrderMessage());
        } else {
            // Instanciation d'un nouvel ordre
            order = new Order();
        }

        btnClose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                orderName = txtOrder.getText().toString();
                order.setOrderMessage(orderName);

                if (-1 == position) {
                    // Création d'un nouvel ordre
                    getOrderManager().create(order);
                } else {
                    // Edition d'un ordre
                    getOrderManager().update(order);
                }

                initRecyclerView(rotationView);
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}