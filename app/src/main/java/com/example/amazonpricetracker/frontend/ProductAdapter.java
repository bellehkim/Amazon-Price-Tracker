package com.example.amazonpricetracker.frontend;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amazonpricetracker.R;
import com.example.amazonpricetracker.backend.managers.FavoriteProductManager;
import com.example.amazonpricetracker.backend.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    // Store data for the adapter
    private final List<Product> productList;
    private final Context context;
    private final FavoriteProductManager favoriteProductManager;

    // Constructor to pass data to the adapter
    public ProductAdapter(List<Product> productList, Context context,
                          FavoriteProductManager favoriteProductManager) {
        this.productList = productList;
        // Turns XML layouts into actual View objects
        this.context = context;
        this.favoriteProductManager = favoriteProductManager;

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
            if (favoriteIcon == null) {
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
                .inflate(R.layout.item_product_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 1. Get the data model for the current item based on position
        // 'position' is the index of the item in the list
        Product currentProduct = productList.get(position);

        Log.d("ProductAdapter", "Binding position=" + position
                + " name=" + currentProduct.name);

        if (holder.productName == null) {
            // Log an error or handle the case where the TextView is not found
            Log.e("ProductAdapter", "ProductName not found!");
            return;
        }

        displayName(holder, currentProduct);
        displayPrice(holder, currentProduct);
        displayOriginalPrice(holder, currentProduct);
        displayDiscount(holder, currentProduct);
        displayPriceChange(holder, currentProduct);
        displayFavorite(holder, currentProduct);

    }

    @Override
    public int getItemCount() {
        return (productList != null) ? productList.size() : 0;
    }

    // ==== Helpers ====

    private void displayName(ViewHolder holder, Product product) {
        holder.productName.setText(product.name);
    }

    private void displayPrice(ViewHolder holder, Product product) {
        String formattedCurrentPriceText = context.getString(R.string.usd_currency,
                product.currentPrice);
        holder.currentPrice.setText(formattedCurrentPriceText);
    }

    private void displayOriginalPrice(ViewHolder holder, Product product) {
        if (product.originalPrice > 0
                && product.originalPrice > product.currentPrice) {
            holder.originalPrice.setVisibility(View.VISIBLE);
            String formattedOriginalPriceText = context.getString(R.string.usd_currency,
                    product.originalPrice);
            holder.originalPrice.setText(formattedOriginalPriceText);
            // Add strikethrough
            holder.originalPrice.setPaintFlags(
                    holder.originalPrice.getPaintFlags()
                            | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.originalPrice.setVisibility(View.GONE);
            // Clear strikethrough so recycled views don’t keep it
            holder.originalPrice.setPaintFlags(
                    holder.originalPrice.getPaintFlags()
                            & ~android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    private void displayDiscount(ViewHolder holder, Product product) {
        if (product.discount != null && !product.discount.isEmpty()) {
            holder.discountBadge.setVisibility(View.VISIBLE);
            holder.discountBadge.setText(product.discount);
        } else {
            holder.discountBadge.setVisibility(View.GONE);

        }
    }

    private void displayPriceChange(ViewHolder holder, Product product) {
        if (product.priceChange != null && !product.priceChange.isEmpty()) {
            holder.priceChangePercent.setVisibility(View.VISIBLE);
            holder.priceChangePercent.setText(product.priceChange);
        } else {
            holder.priceChangePercent.setVisibility(View.GONE);
        }

    }

    private void displayFavorite(ViewHolder holder, Product product) {
        holder.favoriteIcon.setImageResource(product.isFavorite ?
                R.drawable.ic_favorite : R.drawable.ic_favorite_border);
        // Favorite icon visual state
        holder.favoriteIcon.setOnClickListener(v -> {
            updateFavoriteStatus(holder, product);
            // Sync with manager
            favoriteProductManager.toggleFavorite(product, product.isFavorite);
        });
    }

    // Helper method to update the favorite status of a product
    private void updateFavoriteStatus(ViewHolder holder, Product product) {
        product.isFavorite = !product.isFavorite;
        holder.favoriteIcon.setImageResource(product.isFavorite ?
                R.drawable.ic_favorite : R.drawable.ic_favorite_border);
        // TODO: Update the database with the new favorite state
    }
}
