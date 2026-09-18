package com.example.flamepro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecentOrdersAdapter extends RecyclerView.Adapter<RecentOrdersAdapter.RecentViewHolder> {

    public interface OnRecentOrderActionListener {
        void onBuyAgain(Product product);
        void onAddToCart(Product product);
        void onDeleteOrder(int position);
    }

    private final List<RecentOrderProduct> items;
    private final OnRecentOrderActionListener listener;

    public RecentOrdersAdapter(List<RecentOrderProduct> items, OnRecentOrderActionListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_order_card, parent, false);
        return new RecentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentViewHolder holder, int position) {
        RecentOrderProduct model = items.get(position);
        holder.bind(model, listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class RecentViewHolder extends RecyclerView.ViewHolder {
        TextView tvShopName, tvSubStatusTitle, tvProductTitle, tvProductSubtitle, tvPrice, tvTotalPrice;
        ImageView ivProduct;
        View btnAddToCart, btnBuyAgain, btnReview, btnDelete;

        public RecentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvShopName = itemView.findViewById(R.id.tvRecentShopName);
            tvSubStatusTitle = itemView.findViewById(R.id.tvRecentSubStatusTitle);
            tvProductTitle = itemView.findViewById(R.id.tvRecentProductTitle);
            tvProductSubtitle = itemView.findViewById(R.id.tvRecentProductSubtitle);
            tvPrice = itemView.findViewById(R.id.tvRecentProductPrice);
            tvTotalPrice = itemView.findViewById(R.id.tvRecentTotalCalculatedPrice);
            ivProduct = itemView.findViewById(R.id.ivRecentProductImage);
            
            btnAddToCart = itemView.findViewById(R.id.btnRecentAddToCartBubble);
            btnBuyAgain = itemView.findViewById(R.id.btnRecentBuyAgain);
            btnReview = itemView.findViewById(R.id.btnRecentWriteReview);
            btnDelete = itemView.findViewById(R.id.btnRecentDelete);
        }

        public void bind(RecentOrderProduct model, OnRecentOrderActionListener listener) {
            Product p = model.getProduct();
            tvShopName.setText(model.getShopName());
            tvSubStatusTitle.setText(model.getSubStatusTime());
            tvProductTitle.setText(p.getName());
            tvProductSubtitle.setText(p.getWeight() + " • " + p.getType());
            tvPrice.setText(p.getPrice());
            tvTotalPrice.setText(p.getPrice());
            ivProduct.setImageResource(p.getImageResource());

            btnAddToCart.setOnClickListener(v -> {
                if (listener != null) listener.onAddToCart(p);
            });

            btnBuyAgain.setOnClickListener(v -> {
                if (listener != null) listener.onBuyAgain(p);
            });

            btnDelete.setOnClickListener(v -> {
                int currentPos = getBindingAdapterPosition();
                if (listener != null && currentPos != RecyclerView.NO_POSITION) {
                    listener.onDeleteOrder(currentPos);
                }
            });

            btnReview.setOnClickListener(v -> 
                Toast.makeText(itemView.getContext(), "Opening ratings configuration panel...", Toast.LENGTH_SHORT).show());
        }
    }
}
