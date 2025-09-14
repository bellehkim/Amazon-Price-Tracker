package com.example.amazonpricetracker.frontend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amazonpricetracker.R;
import com.example.amazonpricetracker.backend.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    // Store data for the adpater
    public final List<Product> productList;
    public final Context context;

    // Constructor to pass data to the adapter
    public ProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        // Turns XML layouts into actual View objects
        this.context = context;
    }
    // ViewHolder class to hold the views for each item in the RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView discountBadge, productName, currentPrice, originalPrice, priceChangePercent;
        public ImageView productImage, favoriteIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            discountBadge = itemView.findViewById(R.id.discountBadge);
            productName = itemView.findViewById(R.id.productName);
            currentPrice = itemView.findViewById(R.id.currentPrice);
            originalPrice = itemView.findViewById(R.id.originalPrice);
            priceChangePercent = itemView.findViewById(R.id.priceChangePercent);
         //   productImage = itemView.findViewById(R.id.productImage);
            favoriteIcon = itemView.findViewById(R.id.favoriteIcon);
            if (favoriteIcon != null) {
                favoriteIcon.setOnClickListener(v -> {
                    favoriteIcon.setSelected(!favoriteIcon.isSelected());
                });
            } else {
                // Log an error or handle the case where the ImageView is not found
                Log.e("ProductAdapter", "ImageView not found!");
            }
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate the layer
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item_layout, parent, false);
        return new ViewHolder(view);
    }
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder.productName == null) {
            // Log an error or handle the case where the TextView is not found
            Log.e("ProductAdapter", "ProductName not found!");
            return;
        }
        // 1. Get the data model for the current item based on position
        // 'position' is the index of the item in the list
        Product currentProduct = productList.get(position);
        // 2. Bind the data from the 'currentProduct' object to the views in the 'holder'
        // Set the text for the product name and price
//        holder.productNameTextView.setText(currentProduct.getName());
//        holder.productPriceTextView.setText(currentProduct.getPrice());

        holder.productName.setText(currentProduct.name);

        // Current Price
        holder.productName.setText(currentProduct.name != null
                ? currentProduct.name : "(no name");

        @SuppressLint("DefaultLocale") String formatedCurrentPrice =
                String.format("%.2f", currentProduct.currentPrice);
        holder.currentPrice.setText(formatedCurrentPrice);

        // Original Price
        if (currentProduct.originalPrice > 0
                && currentProduct.originalPrice < currentProduct.currentPrice) {
            holder.originalPrice.setVisibility(View.VISIBLE);
            @SuppressLint("DefaultLocale") String formattedOriginalPrice =
                    String.format("%.2f", currentProduct.originalPrice);
            holder.currentPrice.setText(formattedOriginalPrice);
        } else {
            holder.originalPrice.setVisibility(View.GONE);
        }

        // Discount Badge
        if (currentProduct.discount != null && !currentProduct.discount.isEmpty()) {
            holder.discountBadge.setVisibility(View.VISIBLE);
            holder.discountBadge.setText(currentProduct.discount);
        } else {
            holder.discountBadge.setVisibility(View.GONE);

        }

        // Price Change
        if (currentProduct.priceChange != null && !currentProduct.priceChange.isEmpty()) {
            holder.priceChangePercent.setVisibility(View.VISIBLE);
            holder.priceChangePercent.setText(currentProduct.priceChange);
        } else {
            holder.priceChangePercent.setVisibility(View.GONE);
        }

        // Favorite Icon
        holder.favoriteIcon.setSelected(currentProduct.isFavorite);
    }

    public int getItemCount() {
        return (productList != null) ? productList.size() : 0;
    }

}
