package com.example.amazonpricetracker.backend.model;

public class Product {
    // TODO: I need target price variable later
    public final String name;
    public final double currentPrice;
    public final double originalPrice;
    public final String discount;
    public final String priceChange;
    public final boolean isFavorite;
  //  public final String image;

    public Product(final String name, final double currentPrice, final double originalPrice,
                   final String discount, final String priceChange, final boolean isFavorite) {
        this.name = name;
        this.currentPrice = currentPrice;
        this.originalPrice = originalPrice;
        this.discount = discount;
        this.priceChange = priceChange;
        this.isFavorite = isFavorite;
      //  this.image = image;
    }
}
