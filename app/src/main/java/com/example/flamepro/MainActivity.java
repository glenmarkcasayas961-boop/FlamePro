package com.example.flamepro;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigationrail.NavigationRailView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private NavigationRailView navigationRail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            int bottomPadding = (bottomNavigation != null) ? 0 : systemBars.bottom;
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomPadding); 
            return insets;
        });

        initializeViews();
        setupNavigation();

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(new DashboardFragment());
        }
    }

    private void initializeViews() {
        bottomNavigation = findViewById(R.id.bottomNavigation);
        navigationRail = findViewById(R.id.navigationRail);
    }

    private void setupNavigation() {
        if (bottomNavigation != null) {
            setupBottomNavigation();
        } else if (navigationRail != null) {
            setupNavigationRail();
        }
    }

    private void setupBottomNavigation() {
        BadgeDrawable badge = bottomNavigation.getOrCreateBadge(R.id.nav_cart);
        badge.setVisible(true);
        badge.setNumber(0);
        badge.setBackgroundColor(getColor(R.color.brand_red));
        badge.setBadgeTextColor(getColor(R.color.white));

        CartManager.getInstance().addListener(badge::setNumber);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                loadFragment(new DashboardFragment());
                return true;
            } else if (itemId == R.id.nav_shop) {
                loadFragment(new ShopFragment());
                return true;
            } else if (itemId == R.id.nav_cart) {
                loadFragment(new CartFragment());
                return true;
            }
            // Add other fragment switches here
            return false;
        });
    }

    private void setupNavigationRail() {
        BadgeDrawable badge = navigationRail.getOrCreateBadge(R.id.nav_cart);
        badge.setVisible(true);
        badge.setNumber(0);
        badge.setBackgroundColor(getColor(R.color.brand_red));
        badge.setBadgeTextColor(getColor(R.color.white));

        CartManager.getInstance().addListener(badge::setNumber);

        navigationRail.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                loadFragment(new DashboardFragment());
                return true;
            } else if (itemId == R.id.nav_shop) {
                loadFragment(new ShopFragment());
                return true;
            } else if (itemId == R.id.nav_cart) {
                loadFragment(new CartFragment());
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .commit();
    }
}
