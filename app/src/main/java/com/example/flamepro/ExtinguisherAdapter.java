package com.example.flamepro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ExtinguisherAdapter extends RecyclerView.Adapter<ExtinguisherAdapter.ViewHolder> {

    private final List<Integer> items;

    public ExtinguisherAdapter(List<Integer> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_extinguisher, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ivExtinguisher.setImageResource(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivExtinguisher;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivExtinguisher = itemView.findViewById(R.id.ivExtinguisher);
        }
    }
}
