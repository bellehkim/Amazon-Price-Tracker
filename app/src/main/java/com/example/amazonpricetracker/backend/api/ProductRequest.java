package com.example.amazonpricetracker.backend.api;

import lombok.Data;

@Data
public class ProductRequest {
    private String asin;
    private String name;
    private String image;
    private double price;
    private String category;
}

