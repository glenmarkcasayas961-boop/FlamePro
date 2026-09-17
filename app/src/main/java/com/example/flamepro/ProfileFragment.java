package com.example.flamepro;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Fetch numbers from OrderManager dynamically
        int pendingCount = OrderManager.getInstance().getOrdersByStatus(Order.OrderStatus.PENDING).size();
        int shippedCount = OrderManager.getInstance().getOrdersByStatus(Order.OrderStatus.SHIPPED).size();
        int deliveredCount = OrderManager.getInstance().getOrdersByStatus(Order.OrderStatus.DELIVERED).size();
        int cancelledCount = OrderManager.getInstance().getOrdersByStatus(Order.OrderStatus.CANCELLED).size();
        
        // Active orders excludes cancelled ones
        int totalOrdersCount = pendingCount + shippedCount + deliveredCount;
        int onDeliveryCount = pendingCount + shippedCount;

        TextView tvOrdersCount = view.findViewById(R.id.tvOrdersCount);
        TextView tvDeliveryCount = view.findViewById(R.id.tvDeliveryCount);
        TextView tvReceiveCount = view.findViewById(R.id.tvReceiveCount);
        TextView tvCancelledCount = view.findViewById(R.id.tvCancelledCount);
        
        TextView tvProfileName = view.findViewById(R.id.tvProfileName);
        TextView tvProfileEmail = view.findViewById(R.id.tvProfileEmail);

        if (tvOrdersCount != null) tvOrdersCount.setText(String.valueOf(totalOrdersCount));
        if (tvDeliveryCount != null) tvDeliveryCount.setText(String.valueOf(onDeliveryCount));
        if (tvReceiveCount != null) tvReceiveCount.setText(String.valueOf(deliveredCount));
        if (tvCancelledCount != null) tvCancelledCount.setText(String.valueOf(cancelledCount));
        
        UserManager um = UserManager.getInstance();
        if (tvProfileName != null) tvProfileName.setText(um.getFullName());
        if (tvProfileEmail != null) tvProfileEmail.setText(um.getEmailOrUsername());

        // Click actions for the interactive counters boxes
        View.OnClickListener openOrdersListener = v -> {
            if (getActivity() instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) getActivity();
                BottomNavigationView nav = mainActivity.findViewById(R.id.bottomNavigation);
                if (nav != null) nav.setSelectedItemId(R.id.nav_order);
                mainActivity.loadFragment(new MyOrdersFragment());
            }
        };

        view.findViewById(R.id.boxOrdersClick).setOnClickListener(openOrdersListener);
        view.findViewById(R.id.boxDeliveryClick).setOnClickListener(openOrdersListener);
        view.findViewById(R.id.boxReceiveClick).setOnClickListener(openOrdersListener);
        view.findViewById(R.id.boxCancelledClick).setOnClickListener(openOrdersListener);

        // Shipping Grid Row boxes click handlers
        view.findViewById(R.id.btnToPay).setOnClickListener(v -> Toast.makeText(getContext(), "To Pay clicked", Toast.LENGTH_SHORT).show());
        view.findViewById(R.id.btnToShip).setOnClickListener(v -> Toast.makeText(getContext(), "To Ship clicked", Toast.LENGTH_SHORT).show());
        view.findViewById(R.id.btnToReceive).setOnClickListener(v -> Toast.makeText(getContext(), "To Receive clicked", Toast.LENGTH_SHORT).show());
        view.findViewById(R.id.btnToReview).setOnClickListener(v -> Toast.makeText(getContext(), "To Review clicked", Toast.LENGTH_SHORT).show());
        view.findViewById(R.id.btnReturns).setOnClickListener(v -> Toast.makeText(getContext(), "Returns clicked", Toast.LENGTH_SHORT).show());

        // Header and standard control links
        view.findViewById(R.id.btnManageProfile).setOnClickListener(v -> {
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), SetupProfileActivity.class);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.btnTrackOrderTop).setOnClickListener(openOrdersListener);
        view.findViewById(R.id.btnRecentOrderTop).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).loadFragment(new RecentOrdersFragment());
            }
        });
        view.findViewById(R.id.btnHelpSupport).setOnClickListener(v -> Toast.makeText(getContext(), "Help & Support clicked", Toast.LENGTH_SHORT).show());
        view.findViewById(R.id.btnReturnPolicy).setOnClickListener(v -> Toast.makeText(getContext(), "Return to policy clicked", Toast.LENGTH_SHORT).show());

        // Hover & click log out operation button redirect
        view.findViewById(R.id.btnSignOut).setOnClickListener(v -> {
            Toast.makeText(getContext(), "Logging out...", Toast.LENGTH_SHORT).show();
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}
