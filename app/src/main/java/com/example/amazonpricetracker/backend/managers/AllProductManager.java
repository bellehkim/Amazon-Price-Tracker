package com.example.amazonpricetracker.backend.managers;

import android.util.Log;

import com.example.amazonpricetracker.backend.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.AllArgsConstructor;
import lombok.Data;

public class AllProductManager {
    private static final String TAG = "AllProductManager";
    private static AllProductManager INSTANCE;
    private final Map<String, Product> allProductsMap;

    private AllProductManager() {
        allProductsMap = new ConcurrentHashMap<>();
    }

    public static synchronized AllProductManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AllProductManager();
        }
        return INSTANCE;
    }

    public void addOrUpdateProduct(Product product) {
        if (product == null || product.getAsin() != null) {
            allProductsMap.put(product.getAsin(), product);
            Log.d(TAG, "Added or updated product: " + product.getAsin());
        } else {
            Log.w(TAG, "Attempted to add null product or product with null ASIN " + product);
        }
    }
    public void setAllProductsMap(List<Product> productList) {
        allProductsMap.clear();
        for (Product product : productList) {
            if (product != null && product.getAsin() != null) {
                allProductsMap.put(product.getAsin(), product);
            }
        }
    }

    public List<Product> getAllProducts() {
        return List.copyOf(new ArrayList<>(allProductsMap.values()));
    }

    public Product getProductByAsin(String asin) {
        return allProductsMap.get(asin);
    }


}
