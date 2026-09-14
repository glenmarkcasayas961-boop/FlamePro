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

    private RecyclerView rvShopRow1, rvShopRow2, rvServices;
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
        setupServicesRecyclerView();
        setupClickListeners();
        setupTabletStats(view);
        setupCartBadge();
    }

    private void initializeViews(View view) {
        rvShopRow1 = view.findViewById(R.id.rvShopRow1);
        rvShopRow2 = view.findViewById(R.id.rvShopRow2);
        rvServices = view.findViewById(R.id.rvServices);
        actvBranch = view.findViewById(R.id.actvBranch);
        notificationLayout = view.findViewById(R.id.notificationLayout);
        cartIconLayout = view.findViewById(R.id.cartIconLayout);
        tvCartBadge = view.findViewById(R.id.tvCartBadge);
        tvNotificationBadge = view.findViewById(R.id.tvNotificationBadge);
    }

    private void setupServicesRecyclerView() {
        List<Service> services = new ArrayList<>();
        services.add(new Service("Refilling", R.drawable.ic_cat_powder));
        services.add(new Service("Maintenance", R.drawable.ic_coverage));
        services.add(new Service("Installation", R.drawable.ic_cat_foam));
        services.add(new Service("Inspection", R.drawable.ic_check_circle));
        services.add(new Service("Delivery", R.drawable.ic_delivery));

        ServiceAdapter adapter = new ServiceAdapter(services);
        if (rvServices != null) {
            rvServices.setAdapter(adapter);
        }
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
        List<Product> products = getDummyProducts();

        ExtinguisherAdapter.OnProductClickListener listener = product -> 
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, ProductDetailsFragment.newInstance(product))
                    .addToBackStack(null)
                    .commit();

        ExtinguisherAdapter adapter1 = new ExtinguisherAdapter(products, listener);
        ExtinguisherAdapter adapter2 = new ExtinguisherAdapter(products, listener);

        if (rvShopRow1 != null) {
            rvShopRow1.setAdapter(adapter1);
        }
        if (rvShopRow2 != null) {
            rvShopRow2.setAdapter(adapter2);
        }
    }

    private List<Product> getDummyProducts() {
        List<Product> products = new ArrayList<>();
        List<Integer> carousel = new ArrayList<>();
        carousel.add(R.drawable.logo);
        
        List<String> features = new ArrayList<>();
        features.add("Certified Safety");

        products.add(new Product("ABC Dry Powder", "₱ 45.00", "₱ 55.00", "18% OFF", 4.8f, 24, R.drawable.logo, carousel, "5 lb", "Dry Powder", "10-15 ft", features, "Extinguishers", true));
        products.add(new Product("CO2 Extinguisher", "₱ 89.99", "₱ 110.00", "18% OFF", 4.7f, 15, R.drawable.logo, carousel, "10 lb", "CO2", "8-12 ft", features, "Extinguishers", true));
        products.add(new Product("Foam Extinguisher", "₱ 59.50", "₱ 75.00", "20% OFF", 4.5f, 10, R.drawable.logo, carousel, "9L", "Foam", "12-18 ft", features, "Extinguishers", true));
        products.add(new Product("Sprinkler Head", "₱ 24.99", "₱ 30.00", "16% OFF", 4.9f, 42, R.drawable.logo, carousel, "0.5 lb", "Automatic", "200 sq ft", features, "Sprinklers", true));
        products.add(new Product("Fire Blanket", "₱ 15.00", "₱ 25.00", "40% OFF", 4.8f, 56, R.drawable.logo, carousel, "1 lb", "Safety", "1.2x1.2m", features, "Safety", true));
        products.add(new Product("Smoke Detector", "₱ 12.00", "₱ 18.00", "33% OFF", 4.4f, 120, R.drawable.logo, carousel, "0.3 lb", "Electronics", "Room", features, "Safety", true));
        products.add(new Product("Emergency Light", "₱ 35.00", "₱ 45.00", "22% OFF", 4.7f, 30, R.drawable.logo, carousel, "2 lb", "Lighting", "Visual", features, "Safety", true));
        products.add(new Product("First Aid Kit", "₱ 65.00", "₱ 85.00", "23% OFF", 4.9f, 45, R.drawable.logo, carousel, "5 lb", "Medical", "Body", features, "Safety", true));
        
        return products;
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
