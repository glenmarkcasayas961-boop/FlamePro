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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.List;
import java.util.Locale;

public class CartFragment extends Fragment {

    private RecyclerView rvCart;
    private TextView tvTotalPrice;
    private View llEmptyCart;
    private View cvSummary;
    private CartAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvCart = view.findViewById(R.id.rvCart);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        llEmptyCart = view.findViewById(R.id.llEmptyCart);
        cvSummary = view.findViewById(R.id.cvSummary);

        view.findViewById(R.id.btnBrowseProducts).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) getActivity();
                BottomNavigationView nav = mainActivity.findViewById(R.id.bottomNavigation);
                if (nav != null) {
                    nav.setSelectedItemId(R.id.nav_shop);
                }
            }
        });

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
        if (getView() == null || tvTotalPrice == null) return;

        List<CartItem> items = CartManager.getInstance().getCartItems();
        boolean isEmpty = items.isEmpty();

        if (llEmptyCart != null) llEmptyCart.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        if (rvCart != null) rvCart.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        if (cvSummary != null) cvSummary.setVisibility(isEmpty ? View.GONE : View.VISIBLE);

        if (!isEmpty && adapter != null) {
            adapter.updateItems(items);
            double total = CartManager.getInstance().getTotalPrice();
            tvTotalPrice.setText(String.format(Locale.getDefault(), "$ %.2f", total));
        }
    }
}
