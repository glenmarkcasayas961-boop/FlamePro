package com.example.flamepro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

public class CartFragment extends Fragment {

    private RecyclerView rvCart;
    private TextView tvTotalPrice, tvItemCount;
    private View llEmptyCart, clBottomBar;
    private ImageView cbAll;
    private CartAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Header and Status
        tvItemCount = view.findViewById(R.id.tvTitle);
        
        // Lists and States
        rvCart = view.findViewById(R.id.rvCart);
        llEmptyCart = view.findViewById(R.id.llEmptyCart);
        clBottomBar = view.findViewById(R.id.clBottomBar);
        cbAll = view.findViewById(R.id.cbAll);
        
        // Summary
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);

        View ivBack = view.findViewById(R.id.ivBack);
        if (ivBack != null) {
            ivBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        }

        View btnBrowse = view.findViewById(R.id.btnBrowseProducts);
        if (btnBrowse != null) {
            btnBrowse.setOnClickListener(v -> {
                if (getActivity() instanceof MainActivity) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    BottomNavigationView nav = mainActivity.findViewById(R.id.bottomNavigation);
                    if (nav != null) {
                        nav.setSelectedItemId(R.id.nav_shop);
                    }
                }
            });
        }

        View btnCheckout = view.findViewById(R.id.btnCheckout);
        if (btnCheckout != null) {
            btnCheckout.setOnClickListener(v -> {
                double total = CartManager.getInstance().getTotalPrice();
                if (total <= 0) return;
                
                String totalStr = String.format(Locale.getDefault(), "₱ %.2f", total);
                String estDelivery = OrderSuccessFragment.calculateDeliveryDateString();
                
                // Get selected items to save in order history
                List<CartItem> orderedItems = new ArrayList<>();
                for (CartItem item : CartManager.getInstance().getCartItems()) {
                    if (item.isSelected()) {
                        orderedItems.add(item);
                    }
                }
                
                if (!orderedItems.isEmpty()) {
                    TimeZone tz = TimeZone.getTimeZone("Asia/Manila");
                    Calendar cal = Calendar.getInstance(tz);
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy • h:mm a", Locale.ENGLISH);
                    sdf.setTimeZone(tz);
                    String orderDateStr = sdf.format(cal.getTime());
                    
                    String orderId = "ORD - " + (4000 + new Random().nextInt(5000));
                    Order newOrder = new Order(orderId, orderedItems, orderDateStr, estDelivery, totalStr, Order.OrderStatus.PENDING);
                    OrderManager.getInstance().addOrder(newOrder);
                }
                
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).loadFragment(OrderSuccessFragment.newInstance(totalStr, estDelivery));
                }
            });
        }

        if (cbAll != null) {
            cbAll.setOnClickListener(v -> {
                boolean allSelected = true;
                List<CartItem> cartItems = CartManager.getInstance().getCartItems();
                for (CartItem item : cartItems) {
                    if (!item.isSelected()) {
                        allSelected = false;
                        break;
                    }
                }
                boolean targetSelected = !allSelected;
                for (CartItem item : cartItems) {
                    item.setSelected(targetSelected);
                }
                adapter.notifyDataSetChanged();
                updateUI();
            });
        }

        setupRecyclerView();
        updateUI();
    }

    private void setupRecyclerView() {
        adapter = new CartAdapter(CartManager.getInstance().getCartItems(), new CartAdapter.OnCartItemChangeListener() {
            @Override
            public void onQuantityChanged(CartItem item, int newQuantity) {
                CartManager.getInstance().updateQuantity(item.getProduct(), newQuantity);
                updateUI();
            }

            @Override
            public void onRemoveItem(CartItem item) {
                List<CartItem> cartItems = CartManager.getInstance().getCartItems();
                int position = cartItems.indexOf(item);
                CartManager.getInstance().removeProduct(item.getProduct());
                if (position != -1) {
                    adapter.removeItem(position);
                }
                updateUI();
            }
        });
        rvCart.setAdapter(adapter);
    }

    private void updateUI() {
        if (getView() == null) return;

        List<CartItem> items = CartManager.getInstance().getCartItems();
        int totalItemsCount = CartManager.getInstance().getTotalItems();
        boolean isEmpty = items.isEmpty();

        // Safe visibility updates
        if (llEmptyCart != null) llEmptyCart.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        if (rvCart != null) rvCart.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        if (clBottomBar != null) clBottomBar.setVisibility(isEmpty ? View.GONE : View.VISIBLE);

        View clTopBar = getView().findViewById(R.id.clTopBar);
        if (clTopBar != null) {
            clTopBar.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        }

        if (!isEmpty && adapter != null) {
            adapter.updateItems(items);
            
            if (tvItemCount != null) {
                String title = String.format(Locale.getDefault(), "Shopping cart (%d)", totalItemsCount);
                tvItemCount.setText(title);
            }
            
            double total = CartManager.getInstance().getTotalPrice();
            if (tvTotalPrice != null) tvTotalPrice.setText(String.format(Locale.getDefault(), "%.2f", total));

            if (cbAll != null) {
                boolean allSelected = true;
                for (CartItem item : items) {
                    if (!item.isSelected()) {
                        allSelected = false;
                        break;
                    }
                }
                boolean isChecked = allSelected && !items.isEmpty();
                cbAll.setImageResource(isChecked ? R.drawable.custom_checked_circle : R.drawable.custom_unchecked_circle);
            }
        }
    }
}
