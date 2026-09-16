package com.example.flamepro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;

public class MyOrdersFragment extends Fragment {

    private RecyclerView rvOrders;
    private OrderHistoryAdapter adapter;
    private TextView tvOrderCountSub;
    private TextView btnTabAll, btnTabPending, btnTabShipped, btnTabDelivered, btnTabCancelled;
    
    private enum TabFilter { ALL, PENDING, SHIPPED, DELIVERED, CANCELLED }
    private TabFilter currentFilter = TabFilter.ALL;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvOrderCountSub = view.findViewById(R.id.tvOrderCountSub);
        rvOrders = view.findViewById(R.id.rvOrders);

        btnTabAll = view.findViewById(R.id.btnTabAll);
        btnTabPending = view.findViewById(R.id.btnTabPending);
        btnTabShipped = view.findViewById(R.id.btnTabShipped);
        btnTabDelivered = view.findViewById(R.id.btnTabDelivered);
        btnTabCancelled = view.findViewById(R.id.btnTabCancelled);

        // Click listeners for filtering tabs
        btnTabAll.setOnClickListener(v -> selectTab(TabFilter.ALL));
        btnTabPending.setOnClickListener(v -> selectTab(TabFilter.PENDING));
        btnTabShipped.setOnClickListener(v -> selectTab(TabFilter.SHIPPED));
        btnTabDelivered.setOnClickListener(v -> selectTab(TabFilter.DELIVERED));
        btnTabCancelled.setOnClickListener(v -> selectTab(TabFilter.CANCELLED));

        setupRecyclerView();
        filterAndPopulateList();
    }

    private void setupRecyclerView() {
        adapter = new OrderHistoryAdapter(OrderManager.getInstance().getOrders());
        rvOrders.setAdapter(adapter);
    }

    private void selectTab(TabFilter filter) {
        currentFilter = filter;
        updateTabUI();
        filterAndPopulateList();
    }

    private void updateTabUI() {
        // Reset all tabs to unselected style
        resetTabStyle(btnTabAll, "All");
        resetTabStyle(btnTabPending, "Pending");
        resetTabStyle(btnTabShipped, "Shipped");
        resetTabStyle(btnTabDelivered, "Delivered");
        resetTabStyle(btnTabCancelled, "Cancelled");

        // Highlight active tab
        switch (currentFilter) {
            case ALL: setTabSelectedStyle(btnTabAll); break;
            case PENDING: setTabSelectedStyle(btnTabPending); break;
            case SHIPPED: setTabSelectedStyle(btnTabShipped); break;
            case DELIVERED: setTabSelectedStyle(btnTabDelivered); break;
            case CANCELLED: setTabSelectedStyle(btnTabCancelled); break;
        }
    }

    private void resetTabStyle(TextView tab, String text) {
        tab.setBackgroundResource(R.drawable.chip_unselected_bg);
        tab.setTextColor(getResources().getColor(R.color.black, null));
        tab.setPadding(convertDpToPx(16), convertDpToPx(8), convertDpToPx(16), convertDpToPx(8));
    }

    private void setTabSelectedStyle(TextView tab) {
        tab.setBackgroundResource(R.drawable.chip_selected_bg);
        tab.setTextColor(getResources().getColor(R.color.white, null));
        tab.setPadding(convertDpToPx(24), convertDpToPx(8), convertDpToPx(24), convertDpToPx(8));
    }

    private int convertDpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    private void filterAndPopulateList() {
        List<Order> displayList;
        if (currentFilter == TabFilter.ALL) {
            displayList = OrderManager.getInstance().getOrders();
        } else {
            Order.OrderStatus statusMatch;
            switch (currentFilter) {
                case PENDING: statusMatch = Order.OrderStatus.PENDING; break;
                case SHIPPED: statusMatch = Order.OrderStatus.SHIPPED; break;
                case DELIVERED: statusMatch = Order.OrderStatus.DELIVERED; break;
                case CANCELLED: statusMatch = Order.OrderStatus.CANCELLED; break;
                default: statusMatch = Order.OrderStatus.PENDING;
            }
            displayList = OrderManager.getInstance().getOrdersByStatus(statusMatch);
        }

        adapter.updateOrders(displayList);
        
        // Update subtitle order count text
        String countText = String.format(Locale.getDefault(), "%d orders placed", displayList.size());
        tvOrderCountSub.setText(countText);
    }
}
