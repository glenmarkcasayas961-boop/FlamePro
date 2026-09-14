package com.example.flamepro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment {

    private RecyclerView rvProducts;
    private View cartIconLayout, notificationLayout;
    private TextView tvCartBadge;
    private ImageView ivFilter;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_shop, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvProducts = view.findViewById(R.id.rvProducts);
        cartIconLayout = view.findViewById(R.id.cartIconLayout);
        notificationLayout = view.findViewById(R.id.notificationLayout);
        tvCartBadge = view.findViewById(R.id.tvCartBadge);
        ivFilter = view.findViewById(R.id.ivFilter);

        setupProducts();
        setupCart();
        setupNotifications();
        setupFilter();
    }

    private void setupFilter() {
        if (ivFilter != null) {
            ivFilter.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(requireContext(), v);
                popup.getMenu().add(getString(R.string.cat_fire_extinguishers));
                popup.getMenu().add(getString(R.string.cat_fire_sprinklers));
                popup.getMenu().add(getString(R.string.cat_fireman_equipments));
                popup.getMenu().add(getString(R.string.cat_fire_hose));

                popup.setOnMenuItemClickListener(item -> {
                    Toast.makeText(getContext(), "Category: " + item.getTitle(), Toast.LENGTH_SHORT).show();
                    return true;
                });
                popup.show();
            });
        }
    }

    private void setupNotifications() {
        if (notificationLayout != null) {
            notificationLayout.setOnClickListener(v -> 
                Toast.makeText(getContext(), "Fire safety alerts & updates will appear here!", Toast.LENGTH_SHORT).show());
        }
    }

    private void setupCart() {
        if (cartIconLayout != null) {
            cartIconLayout.setOnClickListener(v -> getParentFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, new CartFragment())
                    .addToBackStack(null)
                    .commit());
        }

        if (tvCartBadge != null) {
            CartManager.getInstance().addListener(totalItems -> {
                tvCartBadge.setText(String.valueOf(totalItems));
                tvCartBadge.setVisibility(totalItems > 0 ? View.VISIBLE : View.GONE);
            });
        }
    }

    private void setupProducts() {
        List<Product> products = new ArrayList<>();
        List<Integer> carousel = new ArrayList<>();
        carousel.add(R.drawable.logo);
        carousel.add(R.drawable.logo);
        carousel.add(R.drawable.logo);

        List<String> features = new ArrayList<>();
        features.add("UL Listed & Certified");
        features.add("Standard Safety Compliance");
        features.add("1-Year Warranty");

        // Adding 20 different products
        products.add(new Product("ABC Dry Powder 5 lb", "₱ 45.00", "₱ 55.00", "18% OFF", 4.8f, 24, R.drawable.logo, carousel, "5 lb", "Dry Powder", "10-15 ft", features, "Fire Extinguishers", true));
        products.add(new Product("CO2 Extinguisher 10 lb", "₱ 89.99", "₱ 110.00", "18% OFF", 4.7f, 15, R.drawable.logo, carousel, "10 lb", "CO2", "8-12 ft", features, "Fire Extinguishers", true));
        products.add(new Product("Foam Extinguisher 9 L", "₱ 59.50", "₱ 75.00", "20% OFF", 4.5f, 10, R.drawable.logo, carousel, "9L", "Foam", "12-18 ft", features, "Fire Extinguishers", true));
        products.add(new Product("Automatic Sprinkler Head", "₱ 24.99", "₱ 30.00", "16% OFF", 4.9f, 42, R.drawable.logo, carousel, "0.5 lb", "Automatic", "200 sq ft", features, "Fire Sprinklers", true));
        products.add(new Product("Fireman Helmet (Pro)", "₱ 120.00", "₱ 150.00", "20% OFF", 5.0f, 8, R.drawable.logo, carousel, "3 lb", "Protective", "N/A", features, "Fireman Equipments", true));
        products.add(new Product("Fire Hose Reel 30 m", "₱ 199.99", "₱ 250.00", "20% OFF", 4.6f, 5, R.drawable.logo, carousel, "15 lb", "Manual", "30m", features, "Fire Hose", true));
        products.add(new Product("Fire Blanket 1.2 x 1.2 m", "₱ 15.99", "₱ 20.00", "20% OFF", 4.8f, 56, R.drawable.logo, carousel, "1 lb", "Fiberglass", "1.2x1.2m", features, "Fire Extinguishers", true));
        products.add(new Product("Smoke Detector (Battery)", "₱ 12.50", "₱ 18.00", "30% OFF", 4.4f, 120, R.drawable.logo, carousel, "0.3 lb", "Ionization", "Room", features, "Others", true));
        products.add(new Product("Fire Exit Sign (LED)", "₱ 35.00", "₱ 45.00", "22% OFF", 4.7f, 30, R.drawable.logo, carousel, "2 lb", "LED Emergency", "Visual", features, "Others", true));
        products.add(new Product("Fireman Suit (Standard)", "₱ 450.00", "₱ 550.00", "18% OFF", 4.9f, 3, R.drawable.logo, carousel, "12 lb", "Heat Resistant", "Body", features, "Fireman Equipments", true));
        products.add(new Product("Fire Hose Nozzle (Brass)", "₱ 42.00", "₱ 55.00", "23% OFF", 4.5f, 18, R.drawable.logo, carousel, "2 lb", "Adjustable", "Variable", features, "Fire Hose", true));
        products.add(new Product("Water Extinguisher 6 L", "₱ 39.99", "₱ 50.00", "20% OFF", 4.3f, 12, R.drawable.logo, carousel, "6L", "Water", "15-20 ft", features, "Fire Extinguishers", true));
        products.add(new Product("Wet Chemical Extinguisher", "₱ 75.00", "₱ 95.00", "21% OFF", 4.8f, 9, R.drawable.logo, carousel, "6L", "Wet Chemical", "10-12 ft", features, "Fire Extinguishers", true));
        products.add(new Product("Sprinkler Pipe 2 m", "₱ 18.50", "₱ 25.00", "26% OFF", 4.2f, 20, R.drawable.logo, carousel, "5 lb", "Steel", "2m", features, "Fire Sprinklers", true));
        products.add(new Product("Fireman Boots", "₱ 85.00", "₱ 110.00", "22% OFF", 4.7f, 14, R.drawable.logo, carousel, "4 lb", "Waterproof", "Feet", features, "Fireman Equipments", true));
        products.add(new Product("Fire Axe (Heavy Duty)", "₱ 55.00", "₱ 70.00", "21% OFF", 4.6f, 22, R.drawable.logo, carousel, "6 lb", "Steel", "N/A", features, "Fireman Equipments", true));
        products.add(new Product("Fire Hose Cabinet", "₱ 145.00", "₱ 180.00", "19% OFF", 4.5f, 7, R.drawable.logo, carousel, "20 lb", "Metal", "Standard", features, "Fire Hose", true));
        products.add(new Product("First Aid Kit (Large)", "₱ 65.00", "₱ 85.00", "23% OFF", 4.9f, 45, R.drawable.logo, carousel, "5 lb", "Emergency", "Medical", features, "Others", true));
        products.add(new Product("Fire Whistle", "₱ 5.99", "₱ 10.00", "40% OFF", 4.0f, 80, R.drawable.logo, carousel, "0.1 lb", "Alert", "Audible", features, "Fireman Equipments", true));
        products.add(new Product("Gas Mask (Single Filter)", "₱ 95.00", "₱ 120.00", "20% OFF", 4.8f, 11, R.drawable.logo, carousel, "2 lb", "Air Purifying", "Head", features, "Fireman Equipments", true));

        ProductAdapter adapter = new ProductAdapter(products, product -> {
            CartManager.getInstance().addProduct(product, 1);
            performCartAnimation(rootView.findViewById(R.id.flCartAnim));
        }, product -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, ProductDetailsFragment.newInstance(product))
                    .addToBackStack(null)
                    .commit();
        }, product -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, ProductDetailsFragment.newInstance(product))
                    .addToBackStack(null)
                    .commit();
        });
        rvProducts.setAdapter(adapter);
    }

    private void performCartAnimation(View animView) {
        if (animView == null) return;

        animView.setVisibility(View.VISIBLE);
        animView.setScaleX(0f);
        animView.setScaleY(0f);
        animView.setAlpha(1f);

        animView.animate()
                .scaleX(1.2f)
                .scaleY(1.2f)
                .setDuration(400)
                .setInterpolator(new OvershootInterpolator())
                .withEndAction(() -> {
                    animView.animate()
                            .scaleX(1.8f)
                            .scaleY(1.8f)
                            .alpha(0f)
                            .setDuration(500)
                            .setInterpolator(new AccelerateInterpolator())
                            .withEndAction(() -> animView.setVisibility(View.GONE))
                            .start();
                })
                .start();
    }
}
