package com.example.amazonpricetracker.frontend;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amazonpricetracker.R;
import com.example.amazonpricetracker.backend.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    // Store data for the adpater
    private final List<Product> productList;
    // Constructor to pass data to the adapter
    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    // ViewHolder class to hold the views for each item in the RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView productNameTextView;
        public TextView productPriceTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.textViewProductName);
            productPriceTextView = itemView.findViewById(R.id.textViewCurrentPrice);

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
        // 1. Get the data model for the current item based on position
        // 'position' is the index of the item in the list
        Product currentProduct = productList.get(position);
        // 2. Bind the data from the 'currentProduct' object to the views in the 'holder'
        // Set the text for the product name and price
//        holder.productNameTextView.setText(currentProduct.getName());
//        holder.productPriceTextView.setText(currentProduct.getPrice());
        holder.productNameTextView.setText(currentProduct.name != null
                ? currentProduct.name : "(no name");

        @SuppressLint("DefaultLocale") String formattedPrice = String.format("%.2f", currentProduct.price);
        holder.productPriceTextView.setText(formattedPrice);

        String priceText = "$" + currentProduct.price; // Assuming getPrice() returns a double/float/int
        holder.productPriceTextView.setText(priceText);

    }

    public int getItemCount() {
        return (productList != null) ? productList.size() : 0;
    }

}
