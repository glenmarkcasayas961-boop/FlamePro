package com.example.flamepro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

public class CheckoutBottomSheet extends BottomSheetDialogFragment {

    private Product product;
    private int quantity = 1;
    private double deliveryFee = 9.99;

    public static CheckoutBottomSheet newInstance(Product product) {
        CheckoutBottomSheet fragment = new CheckoutBottomSheet();
        Bundle args = new Bundle();
        args.putSerializable("product", product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = (Product) getArguments().getSerializable("product");
        }
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            view.post(() -> {
                View parent = (View) view.getParent();
                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(parent);
                
                // Keep it steady at the top
                behavior.setFitToContents(false);
                behavior.setExpandedOffset(0);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setSkipCollapsed(true);
                
                // Force parent to take full screen height
                ViewGroup.LayoutParams layoutParams = parent.getLayoutParams();
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                parent.setLayoutParams(layoutParams);
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_checkout_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView ivProduct = view.findViewById(R.id.ivProduct);
        TextView tvProductName = view.findViewById(R.id.tvProductName);
        TextView tvVariant = view.findViewById(R.id.tvVariant);
        TextView tvPrice = view.findViewById(R.id.tvPrice);
        TextView tvQty = view.findViewById(R.id.tvQty);
        TextView tvSubtotal = view.findViewById(R.id.tvSubtotalValue);
        TextView tvTotal = view.findViewById(R.id.tvTotalValue);
        TextView tvItemCount = view.findViewById(R.id.tvItemCount);
        MaterialButton btnPlaceOrder = view.findViewById(R.id.btnPlaceOrder);

        if (product != null) {
            ivProduct.setImageResource(product.getImageResource());
            tvProductName.setText(product.getName());
            tvVariant.setText(product.getWeight() + " • " + product.getType());
            updatePriceUI(tvQty, tvPrice, tvSubtotal, tvTotal, btnPlaceOrder, tvItemCount);
        }

        view.findViewById(R.id.btnPlus).setOnClickListener(v -> {
            quantity++;
            updatePriceUI(tvQty, tvPrice, tvSubtotal, tvTotal, btnPlaceOrder, tvItemCount);
        });

        view.findViewById(R.id.btnMinus).setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updatePriceUI(tvQty, tvPrice, tvSubtotal, tvTotal, btnPlaceOrder, tvItemCount);
            }
        });

        view.findViewById(R.id.btnPlaceOrder).setOnClickListener(v -> {
            String totalStr = tvTotal.getText().toString();
            String estDelivery = OrderSuccessFragment.calculateDeliveryDateString();
            
            if (product != null) {
                List<CartItem> orderedItems = new ArrayList<>();
                orderedItems.add(new CartItem(product, quantity));
                
                TimeZone tz = TimeZone.getTimeZone("Asia/Manila");
                Calendar cal = Calendar.getInstance(tz);
                SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy • h:mm a", Locale.ENGLISH);
                sdf.setTimeZone(tz);
                String orderDateStr = sdf.format(cal.getTime());
                
                String orderId = "ORD - " + (4000 + new Random().nextInt(5000));
                Order newOrder = new Order(orderId, orderedItems, orderDateStr, estDelivery, totalStr, Order.OrderStatus.PENDING);
                OrderManager.getInstance().addOrder(newOrder);
            }

            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).loadFragment(OrderSuccessFragment.newInstance(totalStr, estDelivery));
                dismiss();
            }
        });

        // Toggle radio button on row click
        View codRow = view.findViewById(R.id.codRow);
        RadioButton rbCod = view.findViewById(R.id.rbCod);
        if (codRow != null && rbCod != null) {
            View.OnClickListener toggleListener = v -> rbCod.setChecked(!rbCod.isChecked());
            codRow.setOnClickListener(toggleListener);
            rbCod.setOnClickListener(toggleListener);
        }

        view.findViewById(R.id.btnCancel).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.btnBack).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.btnClearCart).setOnClickListener(v -> dismiss());
    }

    private void updatePriceUI(TextView tvQty, TextView tvPrice, TextView tvSubtotal, 
                              TextView tvTotal, MaterialButton btnPlaceOrder, TextView tvItemCount) {
        
        String cleanPrice = product.getPrice().replaceAll("[^0-9.]", "");
        double unitPrice = Double.parseDouble(cleanPrice);
        double subtotal = unitPrice * quantity;
        double total = subtotal + deliveryFee;

        tvQty.setText(String.valueOf(quantity));
        tvPrice.setText(String.format(Locale.getDefault(), "₱ %.2f", subtotal));
        tvSubtotal.setText(String.format(Locale.getDefault(), "₱ %.2f", subtotal));
        tvTotal.setText(String.format(Locale.getDefault(), "₱ %.2f", total));
        
        String itemText = quantity == 1 ? "1 item" : quantity + " items";
        tvItemCount.setText(itemText);
        
        btnPlaceOrder.setText(String.format(Locale.getDefault(), "Place Order • ₱ %.2f", total));
    }
}
