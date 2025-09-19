package com.example.amazonpricetracker.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@AllArgsConstructor
public class Product {
    // TODO: I need target price variable later
    private String name;
    private double currentPrice;
    private double originalPrice;
    private String discount;
    private String priceChange;
    private boolean isFavorite;
  //  public final String image;
}
