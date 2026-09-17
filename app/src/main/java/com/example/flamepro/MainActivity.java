package com.example.flamepro;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigationrail.NavigationRailView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private NavigationRailView navigationRail;
    private View cartNotificationBar;
    private TextView tvTotalPrice;

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
        setupCartNotificationBar();

        // Load default fragment
        if (savedInstanceState == null) {
            String userName = getIntent().getStringExtra("USER_NAME");
            loadFragment(DashboardFragment.newInstance(userName));
        }
    }

    private void initializeViews() {
        bottomNavigation = findViewById(R.id.bottomNavigation);
        navigationRail = findViewById(R.id.navigationRail);
        cartNotificationBar = findViewById(R.id.cartNotificationBar);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
    }

    private void setupCartNotificationBar() {
        if (cartNotificationBar == null) return;

        CartManager.getInstance().addListener(totalItems -> {
            updateCartNotificationVisibility();
            if (tvTotalPrice != null) {
                tvTotalPrice.setText(String.format(Locale.getDefault(), "₱ %.2f", CartManager.getInstance().getTotalPrice()));
            }
        });

        findViewById(R.id.btnCheckout).setOnClickListener(v -> loadFragment(new CartFragment()));
    }

    private void updateCartNotificationVisibility() {
        if (cartNotificationBar == null) return;
        cartNotificationBar.setVisibility(View.GONE);
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
        badge.setBackgroundColor(getColor(R.color.brand_red));
        badge.setBadgeTextColor(getColor(R.color.white));

        CartManager.getInstance().addListener(totalItems -> {
            badge.setNumber(totalItems);
            badge.setVisible(totalItems > 0);
        });

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Fragment fragment = null;
            if (itemId == R.id.nav_home) {
                String userName = getIntent().getStringExtra("USER_NAME");
                fragment = DashboardFragment.newInstance(userName);
            } else if (itemId == R.id.nav_shop) {
                fragment = new ShopFragment();
            } else if (itemId == R.id.nav_cart) {
                fragment = new CartFragment();
            } else if (itemId == R.id.nav_order) {
                fragment = new MyOrdersFragment();
            } else if (itemId == R.id.nav_profile) {
                fragment = new ProfileFragment();
            }
            
            if (fragment != null) {
                // Top level fragments don't add to backstack and use different animation
                loadTopLevelFragment(fragment);
                return true;
            }
            return false;
        });
    }

    private void setupNavigationRail() {
        BadgeDrawable badge = navigationRail.getOrCreateBadge(R.id.nav_cart);
        badge.setBackgroundColor(getColor(R.color.brand_red));
        badge.setBadgeTextColor(getColor(R.color.white));

        CartManager.getInstance().addListener(totalItems -> {
            badge.setNumber(totalItems);
            badge.setVisible(totalItems > 0);
        });

        navigationRail.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Fragment fragment = null;
            if (itemId == R.id.nav_home) {
                String userName = getIntent().getStringExtra("USER_NAME");
                fragment = DashboardFragment.newInstance(userName);
            } else if (itemId == R.id.nav_shop) {
                fragment = new ShopFragment();
            } else if (itemId == R.id.nav_cart) {
                fragment = new CartFragment();
            } else if (itemId == R.id.nav_order) {
                fragment = new MyOrdersFragment();
            } else if (itemId == R.id.nav_profile) {
                fragment = new ProfileFragment();
            }
            
            if (fragment != null) {
                loadTopLevelFragment(fragment);
                return true;
            }
            return false;
        });
    }

    private void loadTopLevelFragment(Fragment fragment) {
        // Clear backstack when switching top level tabs
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.nav_host_fragment, fragment)
                .commit();
        
        getWindow().getDecorView().post(this::updateCartNotificationVisibility);
    }

    public void loadFragment(Fragment fragment) {
        loadFragment(fragment, false);
    }

    public void loadFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                )
                .replace(R.id.nav_host_fragment, fragment);
        
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        
        transaction.commit();
        
        // Use post to ensure the fragment transaction is completed before checking visibility
        getWindow().getDecorView().post(this::updateCartNotificationVisibility);
    }

    // Removed the problematic onBackPressed override, will use default activity behavior 
    // which works well with FragmentManager's backstack when addToBackStack is used correctly.


    public void setBottomNavigationVisibility(int visibility) {
        if (bottomNavigation != null) {
            bottomNavigation.setVisibility(visibility);
        }
        View navDivider = findViewById(R.id.navDivider);
        if (navDivider != null) {
            navDivider.setVisibility(visibility);
        }
    }
}
