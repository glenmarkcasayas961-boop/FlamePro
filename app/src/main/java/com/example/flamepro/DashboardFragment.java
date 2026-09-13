package com.example.flamepro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private RecyclerView rvShopRow1, rvShopRow2;
    private AutoCompleteTextView actvBranch;
    private View notificationLayout, cartIconLayout;
    private TextView tvCartBadge, tvNotificationBadge;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main_content, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);
        setupDropdowns();
        setupRecyclerViews();
        setupClickListeners();
        setupTabletStats(view);
        setupCartBadge();
    }

    private void initializeViews(View view) {
        rvShopRow1 = view.findViewById(R.id.rvShopRow1);
        rvShopRow2 = view.findViewById(R.id.rvShopRow2);
        actvBranch = view.findViewById(R.id.actvBranch);
        notificationLayout = view.findViewById(R.id.notificationLayout);
        cartIconLayout = view.findViewById(R.id.cartIconLayout);
        tvCartBadge = view.findViewById(R.id.tvCartBadge);
        tvNotificationBadge = view.findViewById(R.id.tvNotificationBadge);
    }

    private void setupClickListeners() {
        if (notificationLayout != null) {
            notificationLayout.setOnClickListener(v -> 
                Toast.makeText(getContext(), "You have new notifications!", Toast.LENGTH_SHORT).show());
        }
        if (cartIconLayout != null) {
            cartIconLayout.setOnClickListener(v -> {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, new CartFragment())
                        .addToBackStack(null)
                        .commit();
            });
        }
    }

    private void setupCartBadge() {
        if (tvCartBadge != null) {
            CartManager.getInstance().addListener(totalItems -> {
                tvCartBadge.setText(String.valueOf(totalItems));
                tvCartBadge.setVisibility(totalItems > 0 ? View.VISIBLE : View.GONE);
            });
        }
    }

    private void setupDropdowns() {
        String[] branches = {getString(R.string.cebu), getString(R.string.bohol), getString(R.string.dumaguete)};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, branches);
        actvBranch.setAdapter(adapter);

        View container = getView().findViewById(R.id.dropdownContainer);
        if (container != null) {
            container.setOnClickListener(v -> actvBranch.showDropDown());
        }
        actvBranch.setOnClickListener(v -> actvBranch.showDropDown());
    }

    private void setupRecyclerViews() {
        List<Integer> dummyExtinguishers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            dummyExtinguishers.add(R.drawable.ic_flamepro_logo);
        }

        ExtinguisherAdapter adapter1 = new ExtinguisherAdapter(dummyExtinguishers);
        ExtinguisherAdapter adapter2 = new ExtinguisherAdapter(dummyExtinguishers);

        if (rvShopRow1 != null) rvShopRow1.setAdapter(adapter1);
        if (rvShopRow2 != null) rvShopRow2.setAdapter(adapter2);
    }

    private void setupTabletStats(View view) {
        View statTotal = view.findViewById(R.id.statTotal);
        if (statTotal != null) {
            updateStatRow(statTotal, getString(R.string.total_orders), "4");
            updateStatRow(view.findViewById(R.id.statPending), getString(R.string.pending), "1");
            updateStatRow(view.findViewById(R.id.statTransit), getString(R.string.in_transit), "1");
            updateStatRow(view.findViewById(R.id.statDelivered), getString(R.string.delivered), "2");
        }
    }

    private void updateStatRow(View view, String label, String value) {
        if (view != null) {
            ((TextView) view.findViewById(R.id.tvStatLabel)).setText(label);
            ((TextView) view.findViewById(R.id.tvStatValue)).setText(value);
        }
    }
}
