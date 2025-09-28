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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private static final String TAG = "ProductAdapter";
    // Store data for the adapter
    private List<Product> productList;
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

        displayName(holder, currentProduct);
        displayPrice(holder, currentProduct);
        displayOriginalPrice(holder, currentProduct);
//        displayDiscount(holder, currentProduct);
//        displayPriceChange(holder, currentProduct);


        Log.d("ProductAdapter", "Binding position=" + position
                + " name=" + currentProduct.getName());

        if (holder.productName == null) {
            // Log an error or handle the case where the TextView is not found
            Log.e("ProductAdapter", "ProductName not found!");
            return;
        }

        // --- Image Loading (Example with Glide, replace if using another library or none) ---
        // if (currentProduct.getImageUrl() != null && !currentProduct.getImageUrl().isEmpty()) {
        // Glide.with(context)
        // .load(currentProduct.getImageUrl())
        // .placeholder(R.drawable.ic_placeholder_image) // Optional placeholder
        // .error(R.drawable.ic_error_image) // Optional error image
        // .into(holder.imageViewProduct);
        // } else {
        // holder.imageViewProduct.setImageResource(R.drawable.ic_placeholder_image); // Default if no URL
        // }

        updateFavoriteIcon(holder, currentProduct.isFavorite());

        holder.favoriteIcon.setOnClickListener(v -> {
            boolean isCurrentlyFavorite = currentProduct.isFavorite();
            String asin = currentProduct.getAsin();

            // Toggle in the manager
            favoriteProductManager.toggleFavorite(asin);
            // Update the local product's favorite status
            currentProduct.setFavorite(!isCurrentlyFavorite);
            // Update the icon
            updateFavoriteIcon(holder, currentProduct.isFavorite());
            // Notify adapter
            notifyItemChanged(holder.getAdapterPosition());

            Log.d(TAG, "Favorite icon clicked for " + asin + ", new state: "
                    + currentProduct.isFavorite());
        });

    }

    @Override
    public int getItemCount() {
        return (productList != null) ? productList.size() : 0;
    }

    public void updateProductsData(List<Product> newProducts) {
        if (newProducts == null) {
            this.productList.clear();
        } else {
            this.productList = new ArrayList<>(newProducts);
        }
        notifyDataSetChanged(); //Refresh the entire list
        Log.d(TAG, "Updated product list with " + productList.size() + " products");
    }

    // ==== Helpers ====

    private void displayName(ViewHolder holder, Product product) {
        holder.productName.setText(product.getName());
    }

    private void displayPrice(ViewHolder holder, Product product) {
        String formattedCurrentPriceText = context.getString(R.string.usd_currency,
                product.getCurrentPrice());
        holder.currentPrice.setText(formattedCurrentPriceText);
    }

    private void displayOriginalPrice(ViewHolder holder, Product product) {
        if (product.getOriginalPrice() > 0
                && product.getOriginalPrice() > product.getCurrentPrice()) {
            holder.originalPrice.setVisibility(View.VISIBLE);
            String formattedOriginalPriceText = context.getString(R.string.usd_currency,
                    product.getOriginalPrice());
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

//    private void displayDiscount(ViewHolder holder, Product product) {
//        if (product.getDiscount() != null && !product.getDiscount().isEmpty()) {
//            holder.discountBadge.setVisibility(View.VISIBLE);
//            holder.discountBadge.setText(product.getDiscount());
//        } else {
//            holder.discountBadge.setVisibility(View.GONE);
//
//        }
//    }

//    private void displayPriceChange(ViewHolder holder, Product product) {
//        if (product.getPriceChange() != null && !product.getPriceChange().isEmpty()) {
//            holder.priceChangePercent.setVisibility(View.VISIBLE);
//            holder.priceChangePercent.setText(product.getPriceChange());
//        } else {
//            holder.priceChangePercent.setVisibility(View.GONE);
//        }
//
//    }

//    private void displayFavorite(ViewHolder holder, Product product) {
//        holder.favoriteIcon.setImageResource(product.isFavorite() ?
//                R.drawable.ic_favorite : R.drawable.ic_favorite_border);
//        // Favorite icon visual state
//        holder.favoriteIcon.setOnClickListener(v -> {
//            updateFavoriteIcon(holder, product);
//            // Sync with manager
//            favoriteProductManager.toggleFavorite(product, product.isFavorite());
//        });
//    }

    // Helper method to update the favorite status of a product
    private void updateFavoriteIcon(ViewHolder holder, boolean isFavorite) {
        if (isFavorite) {
            holder.favoriteIcon.setImageResource(R.drawable.ic_favorite);
        } else {
            holder.favoriteIcon.setImageResource(R.drawable.ic_favorite_border);
        }
    }


}
