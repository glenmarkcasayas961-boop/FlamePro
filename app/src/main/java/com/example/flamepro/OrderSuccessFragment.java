package com.example.flamepro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class OrderSuccessFragment extends Fragment {

    private String totalPrice;
    private String estDelivery;

    public static OrderSuccessFragment newInstance(String totalPrice) {
        OrderSuccessFragment fragment = new OrderSuccessFragment();
        Bundle args = new Bundle();
        args.putString("total_price", totalPrice);
        fragment.setArguments(args);
        return fragment;
    }

    public static OrderSuccessFragment newInstance(String totalPrice, String estDelivery) {
        OrderSuccessFragment fragment = new OrderSuccessFragment();
        Bundle args = new Bundle();
        args.putString("total_price", totalPrice);
        args.putString("est_delivery", estDelivery);
        fragment.setArguments(args);
        return fragment;
    }

    public static String calculateDeliveryDateString() {
        // Use Philippines Timezone (Asia/Manila)
        TimeZone tz = TimeZone.getTimeZone("Asia/Manila");
        Calendar now = Calendar.getInstance(tz);

        int startDays;
        int endDays;

        // Rule: Before 5:00 PM, 2-3 days. At or after, 3-4 days.
        int hour = now.get(Calendar.HOUR_OF_DAY);
        if (hour < 17) {
            startDays = 2;
            endDays = 3;
        } else {
            startDays = 3;
            endDays = 4;
        }

        Calendar startDate = (Calendar) now.clone();
        startDate.add(Calendar.DAY_OF_YEAR, startDays);

        Calendar endDate = (Calendar) now.clone();
        endDate.add(Calendar.DAY_OF_YEAR, endDays);

        SimpleDateFormat dayFormat = new SimpleDateFormat("MMM d", Locale.ENGLISH);
        dayFormat.setTimeZone(tz);
        
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        yearFormat.setTimeZone(tz);

        String dateRange = dayFormat.format(startDate.getTime()) + " – " + 
                          (startDate.get(Calendar.MONTH) == endDate.get(Calendar.MONTH) ? 
                           endDate.get(Calendar.DAY_OF_MONTH) : dayFormat.format(endDate.getTime())) + 
                          ", " + yearFormat.format(endDate.getTime());

        return "Est. delivery: " + dateRange;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            totalPrice = getArguments().getString("total_price");
            estDelivery = getArguments().getString("est_delivery");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_success, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Hide bottom nav for success screen
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setBottomNavigationVisibility(View.GONE);
        }

        TextView tvTotal = view.findViewById(R.id.tvTotalValue);
        TextView tvEstDelivery = view.findViewById(R.id.tvEstDelivery);

        if (totalPrice != null) {
            tvTotal.setText(totalPrice);
        }

        if (estDelivery != null) {
            tvEstDelivery.setText(estDelivery);
        } else {
            tvEstDelivery.setText(calculateDeliveryDateString());
        }

        view.findViewById(R.id.btnContinueShopping).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                MainActivity activity = (MainActivity) getActivity();
                activity.setBottomNavigationVisibility(View.VISIBLE);
                BottomNavigationView nav = activity.findViewById(R.id.bottomNavigation);
                if (nav != null) {
                    nav.setSelectedItemId(R.id.nav_home);
                }
            }
        });
        
        view.findViewById(R.id.btnTrackOrder).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                MainActivity activity = (MainActivity) getActivity();
                activity.setBottomNavigationVisibility(View.VISIBLE);
                BottomNavigationView nav = activity.findViewById(R.id.bottomNavigation);
                if (nav != null) {
                    nav.setSelectedItemId(R.id.nav_order);
                }
                activity.loadFragment(new MyOrdersFragment());
            }
        });
    }
}
