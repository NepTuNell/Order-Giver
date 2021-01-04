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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ordergiver.R;
import com.example.ordergiver.entity.Order;
import com.example.ordergiver.java.OrderAdapter;
import com.example.ordergiver.manager.OrderManager;
import com.example.ordergiver.manager.VerbManager;
import com.example.ordergiver.service.OrderNormalizer;
import com.example.ordergiver.service.VerbsCreate;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static java.util.Objects.*;


public class OrderTab extends Fragment
{
    //****************************
    // Attributes
    //****************************

    // Recycler View
    private ArrayList<Order> mOrdersList;
    private OrderAdapter mOrderAdapter;

    private LinearLayout mLinearLayoutIsSync;
    private VerbManager mVerbManager;
    private OrderManager mOrderManager;
    private OrderNormalizer mOrderNormalizer;
    private Dialog mDialog;
    private String mOrderName;
    private ViewGroup mRotationView;
    private VerbsCreate mVerbsCreate;
    private TextView mTextInstallProgress;
    private Button mButtonSync;
    private Button mButtonAddOrder;

    // control
    private ArrayList<Button> mButtons;
    private boolean mIsTouchingButton;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        initAllAttributes(inflater, container);
        initRecyclerView();
        initButtonsArray();
        return mRotationView;
    }

    //****************************
    // Accessors
    //****************************

    /* Getters */
    public ViewGroup getRotationView() { return mRotationView; }
    public LinearLayout getLinearLayoutIsSync() { return mLinearLayoutIsSync; }
    public VerbsCreate getVerbsCreate() { return mVerbsCreate; }
    public VerbManager getVerbManager() { return mVerbManager; }
    public OrderManager getOrderManager() { return mOrderManager; }
    public OrderNormalizer getOrderNormalizer() { return mOrderNormalizer; }
    public OrderAdapter getOrderAdapter() { return mOrderAdapter; }
    public ArrayList<Order> getOrdersList() { return mOrdersList; }
    public ArrayList<Button> getButtons() { return mButtons; }
    public Dialog getDialog() { return mDialog; }
    public String getOrderName() { return mOrderName; }
    public TextView getTextInstallProgress() { return mTextInstallProgress; }
    public Button getButtonAddOrder() { return mButtonAddOrder; }
    public Button getButtonSync() { return mButtonSync; }
    public boolean getIsTouchingButton() { return  mIsTouchingButton; }

    /* Setters */
    private void setOrderName(String order) { mOrderName = order; }
    private void setIsTouchingButton(boolean touching) { mIsTouchingButton = touching; }

    //****************************
    // Initialization methods
    //****************************

    /**
     * Init all variables
     */
    private void initAllAttributes(LayoutInflater inflater, ViewGroup container)
    {
        // View group
        mRotationView = (ViewGroup) inflater.inflate(R.layout.order_tab, container, false);

        // Elements
        mDialog = new Dialog(requireNonNull(getActivity()));
        mLinearLayoutIsSync = getRotationView().findViewById(R.id.linear_is_sync);
        mTextInstallProgress = getLinearLayoutIsSync().findViewById(R.id.install_progress_text);
        mIsTouchingButton = false;

        // Services
        mVerbsCreate = new VerbsCreate(getContext());
        mOrderNormalizer = new OrderNormalizer();
        mVerbManager = new VerbManager(getContext());
        mOrderManager = new OrderManager(getActivity());

        // Buttons
        mButtonAddOrder = getRotationView().findViewById(R.id.btn_add);
        mButtonSync = getRotationView().findViewById(R.id.btn_sync);
    }

    /**
     * Init the recycler view
     */
    private void initRecyclerView()
    {
        // Create the recycler
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView orderRecyclerView = getRotationView().findViewById(R.id.recyclerView);
        orderRecyclerView.setLayoutManager(layoutManager);
        orderRecyclerView.setHasFixedSize(true);

        // Adapt the recycler
        mOrdersList = getOrderManager().getOrders();
        mOrderAdapter = new OrderAdapter(mOrdersList);
        orderRecyclerView.setAdapter(mOrderAdapter);

        addEventListeners();
    }

    /**
     * Init button's array in order to enable
     * and disable buttons on the page easier
     */
    private void initButtonsArray()
    {
        mButtons = new ArrayList<>();
        mButtons.add(getButtonAddOrder());
        mButtons.add(getButtonSync());
    }

    /**
     * Listeners
     */
    private void addEventListeners()
    {
        getButtonAddOrder().setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {

                if (0 == getVerbManager().countEntries()) {
                    printMessage("Veuillez installer les données.", false);
                    return;
                }

                showPopup(-1);
            }
        });

        // Install data
        getButtonSync().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // If no data found in the database
                if (0 != getVerbManager().countEntries()) {
                    printMessage("Données déjà installées.", false);
                    return;
                }

                disabledScreen();
                getLinearLayoutIsSync().setVisibility(View.VISIBLE);

                // Create new thread for processing data in the database (that avoid to get "app not responding" message)
                new Thread( new Runnable() {
                    public void run() {
                        execCreateVerbs();

                        // The handler allows to display a message when installation complete for 5 seconds
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable(){
                            @Override
                            public void run() {
                                getLinearLayoutIsSync().setVisibility(View.INVISIBLE);
                                getTextInstallProgress().setText(R.string.install_progress);
                                enabledScreen();
                            }
                        }, 5000);
                    }
                }).start();
            }

            // Create verbs in the database
            private void execCreateVerbs()
            {
                String str;

                if (getVerbsCreate().createVerbs()) {
                    str = "Installation terminée !";
                } else {
                    str = "Erreur lors de l'installation.";
                }

                getTextInstallProgress().setText(str);
            }
        });

        getOrderAdapter().setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int position)
            {
                showPopup(position);
            }
            @Override
            public void onDeleteClick(int position)
            {
                removeItemRecycler(position);
            }
        });
    }

    //****************************
    // Methods
    //****************************

    /**
     * Enable screen touching and buttons
     */
    private void enabledScreen()
    {
        requireNonNull(getActivity()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        for (Button button: getButtons()) {
            button.setEnabled(true);
        }
    }

    /**
     * Disable screen touching and buttons
     */
    private void disabledScreen()
    {
        requireNonNull(getActivity()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        for (Button button: getButtons()) {
            button.setEnabled(false);
        }
    }

    /**
     * Add new item in the recycler view
     */
    public void addItemRecycler(@NotNull Order order)
    {
        getOrdersList().add(order);
        getOrderAdapter().notifyItemInserted(getOrdersList().size());
    }

    /**
     * Update an item in the recycler view
     */
    public void updateItemRecycler(@NotNull Order order, int position)
    {
        getOrdersList().set(position, order);
        getOrderAdapter().notifyItemChanged(position);
    }

    /**
     * Delete item in recycler view and database
     */
    public void removeItemRecycler (int position)
    {
        Order order = getOrderManager().getOrder(getOrderAdapter().getElemByPosition(position));
        getOrderManager().delete(order);
        getOrdersList().remove(position);
        getOrderAdapter().notifyItemRemoved(position);
    }

    /**
     * Display the dialog popup
     */
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

        // Get clicked item
        if (-1 != position) {
            order = getOrderManager().getOrder(getOrderAdapter().getElemByPosition(position));
            txtOrder.setText(order.getOrderMessage());
            orderId = order.getOrderId();
        } else {
            order = new Order();
            orderId = -1;
        }

        // Dismiss dialog popup
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                getDialog().dismiss();
            }
        });

        // Create or update order by calling editOrder method
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (!getIsTouchingButton()) {
                    setIsTouchingButton(true);
                    editOrder(order, txtOrder, orderId, position);
                }
            }
        });

        getDialog().show();
    }

    /**
     * Create or update an order
     */
    private void editOrder(Order order, @NotNull EditText txtOrder, int orderId, int position)
    {
        if (txtOrder.getText().toString().trim().equals("")) {
            printMessage("Veuillez saisir l'ordre .", false);
            return;
        }

        String message;
        setOrderName(getOrderNormalizer().subStringOrder(txtOrder.getText().toString()));
        order.setOrderMessage(getOrderName());

        // Check if verb exist in verb's table
        if (!getVerbManager().checkInfinitiveExist(order.getOrderMessage(), -1)) {
            printMessage("Veuillez saisir un verbe à l'infinitif que vous utiliserez vocalement, conjugué ou non, pour donner un ordre.", false);
            return;
        }

        // Check if verb already exists in order's table
        if (getOrderManager().checkOrderExist(getOrderName(), orderId)) {
            printMessage("Cet ordre existe déjà.", false);
            return;
        }

        if (-1 == position) {
            // Create new order
            getOrderManager().create(order);
            addItemRecycler(order);
            message = "Ordre créé.";
        } else {
            // Update order
            getOrderManager().update(order);
            updateItemRecycler(order, position);
            message = "Ordre modifié.";
        }

        printMessage(message, true);
    }

    /**
     * Display toast messages with the possibility of dismiss the dialog popup
     */
    private void printMessage(final String str, final boolean close)
    {
        Toast.makeText(getContext(), str, Toast.LENGTH_LONG).show();
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
}