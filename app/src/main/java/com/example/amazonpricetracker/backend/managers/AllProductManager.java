package com.example.amazonpricetracker.backend.managers;

import android.util.Log;

import com.example.amazonpricetracker.backend.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton class for managing all products.
 */
public class AllProductManager {
    private static final String TAG = "AllProductManager";
    private static AllProductManager INSTANCE;
    private final Map<String, Product> allProductsMap;

    /**
     * Private constructor to prevent instantiation from outside the class.
     */
    private AllProductManager() {
        allProductsMap = new ConcurrentHashMap<>();
    }

    /**
     * Get the singleton instance of the AllProductManager.
     * @return the singleton instance
     */
    public static synchronized AllProductManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AllProductManager();
        }
        return INSTANCE;
    }

    /**
     * Adds a new product or updates an existing product in the map.
     * <p>
     *     Products are identified by their ASIN. If a product with the same ASIN already exists,
     *     it will be replaced. Otherwise, a new product will be added.
     * </p>
     *
     * @param product the product to add or update
     */
    public void addOrUpdateProduct(Product product) {
        if (product == null || product.getAsin() != null) {
            allProductsMap.put(product.getAsin(), product);
            Log.d(TAG, "Added or updated product: " + product.getAsin());
        } else {
            Log.w(TAG, "Attempted to add null product or product with null ASIN " + product);
        }
    }

    /**
     * Removes a product from the map.
     * @param productList the list of products to remove
     */
    public void setAllProductsMap(List<Product> productList) {
        allProductsMap.clear();
        for (Product product : productList) {
            if (product != null && product.getAsin() != null) {
                allProductsMap.put(product.getAsin(), product);
            }
        }
    }

    /**
     * Returns a list of all products.
     * @return a list of all products
     */
    public List<Product> getAllProducts() {
        return List.copyOf(new ArrayList<>(allProductsMap.values()));
    }

    /**
     * Returns a product by its ASIN.
     * @param asin the ASIN of the product
     * @return the product with the given ASIN, or null if not found
     */
    public Product getProductByAsin(String asin) {
        return allProductsMap.get(asin);
    }


}
