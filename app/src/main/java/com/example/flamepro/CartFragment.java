package com.example.flamepro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.List;
import java.util.Locale;

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
            btnCheckout.setOnClickListener(v -> 
                Toast.makeText(getContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show());
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
                CartManager.getInstance().removeProduct(item.getProduct());
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
