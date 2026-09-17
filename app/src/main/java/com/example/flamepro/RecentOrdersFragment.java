package com.example.flamepro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class RecentOrdersFragment extends Fragment implements RecentOrdersAdapter.OnRecentOrderActionListener {

    private View flCartAnim;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recent_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.ivRecentBack).setOnClickListener(v -> getParentFragmentManager().popBackStack());
        flCartAnim = view.findViewById(R.id.flCartAnim);

        RecyclerView rv = view.findViewById(R.id.rvRecentOrders);
        
        List<RecentOrderProduct> sampleList = new ArrayList<>();
        
        // Define real products to enable add to cart functionality
        Product p1 = new Product("HALO Smart Smoke Detector", "₱ 2,499.00", "", "", 4.8f, 100, R.drawable.ic_check_circle, null, "White", "Smart Sensor", "", null, "", true);
        Product p2 = new Product("UGREEN PD 20W Fast Charger", "₱ 703.48", "", "", 4.7f, 50, R.drawable.ic_lightning, null, "White + 1M Cable", "Charger", "", null, "", true);
        Product p3 = new Product("Industrial Safety Helmet", "₱ 980.00", "", "", 4.9f, 30, R.drawable.ic_fire_type, null, "Yellow", "PPE", "", null, "", true);
        Product p4 = new Product("Heavy-Duty Rescue Rope", "₱ 2,150.00", "", "", 4.6f, 20, R.drawable.ic_coverage, null, "30m", "Safety Gear", "", null, "", true);

        sampleList.add(new RecentOrderProduct("HaloSafetyShop", "1:17 PM Delivered", p1));
        sampleList.add(new RecentOrderProduct("UgreenOfficialShop", "1:17 PM Delivered", p2));
        sampleList.add(new RecentOrderProduct("ArmorGuard Off", "Seller preparing package", p3));
        sampleList.add(new RecentOrderProduct("ClimbSafe Gear", "In Transit", p4));

        RecentOrdersAdapter adapter = new RecentOrdersAdapter(sampleList, this);
        rv.setAdapter(adapter);
    }

    @Override
    public void onBuyAgain(Product product) {
        // Open the checkout summary dialog directly instead of just adding to cart
        if (getParentFragmentManager() != null) {
            CheckoutBottomSheet.newInstance(product).show(getParentFragmentManager(), "checkout_history");
        }
    }

    @Override
    public void onAddToCart(Product product) {
        CartManager.getInstance().addProduct(product, 1);
        performCartAnimation(flCartAnim);
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
