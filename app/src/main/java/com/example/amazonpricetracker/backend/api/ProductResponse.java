package com.example.amazonpricetracker.backend.api;

import lombok.Data;

@Data
public class ProductResponse {
    private String asin;
    private String name;
    private String image;
    private double price;
    private String category;
}
