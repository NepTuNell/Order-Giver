package com.example.ordergiver.java;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ordergiver.R;
import com.example.ordergiver.entity.Order;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ExampleViewHolder>
{
    //****************************
    // Attributes
    //****************************

    private ArrayList<Order> mOrdersList;
    private OnItemClickListener mListener;

    //****************************
    // Getters
    //****************************

    public ArrayList<Order> getOrdersList() { return mOrdersList; }
    public OnItemClickListener getListener() { return mListener; }

    // Constructor
    public OrderAdapter(ArrayList<Order> ordersList)
    {
        mOrdersList = ordersList;
    }

    // Interface
    public interface OnItemClickListener
    {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    //****************************
    // Methods
    //****************************

    public void setOnItemClickListener (OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v, getListener());
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position)
    {
        Order currentItem = mOrdersList.get(position);
        holder.getTextView().setText(currentItem.getOrderMessage());
    }

    @Override
    public int getItemCount()
    {
        return getOrdersList().size();
    }

    /**
     * Return an order at a position
     */
    public Order getElemByPosition(int position)
    {
        return getOrdersList().get(position);
    }

    /**
     * Inner class
     */
    public static class ExampleViewHolder extends RecyclerView.ViewHolder
    {
        //****************************
        // Attributes
        //****************************

        public TextView mTextView;
        public ImageView mDeleteImage;
        public ImageView mEditImage;

        //****************************
        // Getters
        //****************************

        public TextView getTextView()
        {
            return mTextView;
        }

        public ImageView getEditImage()
        {
            return mEditImage;

        }
        public ImageView getDeleteImage()
        {
            return mDeleteImage;
        }

        // Constructor
        public ExampleViewHolder(@NonNull View itemView, final OnItemClickListener listener)
        {
            super(itemView);
            mTextView = itemView.findViewById(R.id.texViewListOrder);
            mEditImage = itemView.findViewById(R.id.image_edit);
            mDeleteImage = itemView.findViewById(R.id.image_delete);
            addEventListeners(listener);
        }

        // Listeners
        private void addEventListeners(final OnItemClickListener listener)
        {
            getEditImage().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onEditClick(position);
                        }
                    }
                }
            });

            getDeleteImage().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

}
