package com.example.amazonpricetracker.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
    // TODO: I need target price variable later
    private String asin;
    private String name;
    private double currentPrice;
    private double originalPrice;
//    private String priceChange;
//    private String discount;
//    private String category;
    private String image;
    private boolean isFavorite;
}
