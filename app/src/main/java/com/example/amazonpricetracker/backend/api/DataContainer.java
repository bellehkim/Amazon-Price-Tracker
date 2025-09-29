package com.example.amazonpricetracker.backend.api;

import com.example.amazonpricetracker.backend.model.AmazonProduct;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Container for the Amazon product data.
 */
@Data
public class DataContainer {

    /**
     * Product details returned from the API
     * <p>
     *     Includes fields such as ASIN, name, price, image URL etc.
     * </p>
     */
    @SerializedName("amazonProduct")
    private AmazonProduct amazonProduct;
}
