package com.example.amazonpricetracker.backend.managers;

import com.example.amazonpricetracker.backend.model.Product;

import java.util.ArrayList;
import java.util.List;

public class FavoriteProductManager {

    private final List<Product> favorites = new ArrayList<>();
    private final List<Product> products = new ArrayList<>();

    public void setProducts(List<Product> products) {
        this.products.clear();
        this.products.addAll(products);
        rebuildFavorites();
    }

    public List<Product> getFavorites() {
        return favorites;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void toggleFavorite(Product product, boolean favorite) {
//        product. = favorite;

        if (favorite) {
            if (!favorites.contains(product)) {
                favorites.add(product);
            }
        } else {
            favorites.remove(product);
        }
    }

    private void rebuildFavorites() {
        favorites.clear();
        for (Product product : products) {
            if (product.isFavorite()) {
                favorites.add(product);
            }
        }
    }
}
