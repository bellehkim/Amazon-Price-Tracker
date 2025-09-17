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
import androidx.recyclerview.widget.RecyclerView;

import com.example.amazonpricetracker.R;
import com.example.amazonpricetracker.backend.managers.FavoriteProductManager;
import com.example.amazonpricetracker.backend.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    // Store data for the adpater
    public final List<Product> productList;
    public final Context context;
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
                .inflate(R.layout.item_product_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
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

        Log.d("ProductAdapter", "Binding position=" + position + " name=" + currentProduct.name);


        holder.productName.setText(currentProduct.name);

        // Current Price
        holder.productName.setText(currentProduct.name != null
                ? currentProduct.name : "(no name)");

        String formattedCurrentPriceText = context.getString(R.string.usd_currency,
                currentProduct.currentPrice);
        holder.currentPrice.setText(formattedCurrentPriceText);

//        @SuppressLint("DefaultLocale") String formatedCurrentPrice =
//                String.format("%.2f", currentProduct.currentPrice);
//        holder.currentPrice.setText(formatedCurrentPrice);

        // Original Price
        if (currentProduct.originalPrice > 0
                && currentProduct.originalPrice > currentProduct.currentPrice) {
            holder.originalPrice.setVisibility(View.VISIBLE);
            String formattedOriginalPriceText = context.getString(R.string.usd_currency,
                    currentProduct.originalPrice);
            holder.originalPrice.setText(formattedOriginalPriceText);
            // Add strikethrough
            holder.originalPrice.setPaintFlags(
                    holder.originalPrice.getPaintFlags() |
                            android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.originalPrice.setVisibility(View.GONE);
            // Clear strikethrough so recycled views don’t keep it
            holder.originalPrice.setPaintFlags(
                    holder.originalPrice.getPaintFlags() &
                            ~android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
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

        // Update icon based on favorite status
        holder.favoriteIcon.setImageResource(currentProduct.isFavorite ?
                R.drawable.ic_favorite : R.drawable.ic_favorite_border);
        // Favorite icon visual state
        holder.favoriteIcon.setOnClickListener(v -> {
            updateFavoriteStatus(holder, currentProduct);
            // Sync with manager
            favoriteProductManager.toggleFavorite(currentProduct, currentProduct.isFavorite);
        });
    }

    @Override
    public int getItemCount() {
        return (productList != null) ? productList.size() : 0;
    }

    private void updateFavoriteStatus(ViewHolder holder, Product product) {
        product.isFavorite = !product.isFavorite;
        holder.favoriteIcon.setImageResource(product.isFavorite ?
                R.drawable.ic_favorite : R.drawable.ic_favorite_border);



        // TODO: Update the database with the new favorite state
    }

}
