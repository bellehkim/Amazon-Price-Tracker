package com.example.amazonpricetracker.backend.api;

import lombok.Data;

/**
 * Response from the Canopy API when getting product details.
 */
@Data
public class ProductResponse {
    private String asin;
    private String name;
    private String image;
    private Double price;
//    private String category;
}
