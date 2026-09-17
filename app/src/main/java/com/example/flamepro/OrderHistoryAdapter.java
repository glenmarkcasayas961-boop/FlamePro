package com.example.flamepro;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    public interface OnOrderCancelListener {
        void onOrderCancelled(Order order);
    }

    private List<Order> orders;
    private OnOrderCancelListener cancelListener;

    public OrderHistoryAdapter(List<Order> orders, OnOrderCancelListener cancelListener) {
        this.orders = orders;
        this.cancelListener = cancelListener;
    }

    public void updateOrders(List<Order> newOrders) {
        this.orders = new ArrayList<>(newOrders);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.bind(order, cancelListener);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvStatus, tvTitle, tvDate, tvEstDelivery, tvPrice, btnCancelOrder;
        ImageView ivProduct;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderNumber);
            tvStatus = itemView.findViewById(R.id.tvStatusBadge);
            tvTitle = itemView.findViewById(R.id.tvOrderTitle);
            tvDate = itemView.findViewById(R.id.tvOrderDate);
            tvEstDelivery = itemView.findViewById(R.id.tvItemEstDelivery);
            tvPrice = itemView.findViewById(R.id.tvOrderPrice);
            btnCancelOrder = itemView.findViewById(R.id.btnCancelOrder);
            ivProduct = itemView.findViewById(R.id.ivProductImage);
        }

        public void bind(Order order, OnOrderCancelListener cancelListener) {
            tvOrderId.setText(order.getOrderId());
            tvDate.setText(order.getOrderDate());
            tvEstDelivery.setText(order.getEstDelivery());
            tvPrice.setText(order.getTotalPrice());

            if (!order.getItems().isEmpty()) {
                CartItem firstItem = order.getItems().get(0);
                String title = firstItem.getProduct().getName() + " x " + firstItem.getQuantity();
                tvTitle.setText(title);
                ivProduct.setImageResource(firstItem.getProduct().getImageResource());
            }

            setStatusBadge(order.getStatus());

            if (order.getStatus() == Order.OrderStatus.PENDING) {
                btnCancelOrder.setVisibility(View.VISIBLE);
                btnCancelOrder.setOnClickListener(v -> {
                    if (cancelListener != null) {
                        cancelListener.onOrderCancelled(order);
                    }
                });
            } else {
                btnCancelOrder.setVisibility(View.GONE);
            }
        }

        private void setStatusBadge(Order.OrderStatus status) {
            tvStatus.setText(status.getLabel());
            switch (status) {
                case SHIPPED:
                    tvStatus.setBackgroundResource(R.drawable.badge_shipped);
                    tvStatus.setTextColor(Color.parseColor("#E65100"));
                    break;
                case DELIVERED:
                    tvStatus.setBackgroundResource(R.drawable.badge_delivered);
                    tvStatus.setTextColor(Color.parseColor("#2E7D32"));
                    break;
                case CANCELLED:
                    tvStatus.setBackgroundResource(R.drawable.badge_cancelled);
                    tvStatus.setTextColor(Color.parseColor("#C62828"));
                    break;
                case PENDING:
                default:
                    tvStatus.setBackgroundResource(R.drawable.badge_pending);
                    tvStatus.setTextColor(Color.parseColor("#757575"));
                    break;
            }
        }
    }
}
