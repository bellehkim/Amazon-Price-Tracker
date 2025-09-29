package com.example.amazonpricetracker.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Model class for a product.
 * <p>
 *     This class is used internally by the app for displaying products in the UI and
 *     managing product states such as favorites.
 * </p>
 */
@Data
@AllArgsConstructor
public class Product {
    // TODO: I need target price variable later
    /**
     * Unique identifier for the product.
     */
    private String asin;

    /**
     * Name of the product.
     */
    private String name;

    /**
     * Current price of the product.
     */
    private double currentPrice;

    /**
     * Original price of the product.
     */
    private double originalPrice;
//    private String priceChange;
//    private String discount;
//    private String category;

    /**
     * URL of the product's image.
     */
    private String image;

    /**
     * Whether the product is a favorite or not.
     */
    private boolean isFavorite;
}
