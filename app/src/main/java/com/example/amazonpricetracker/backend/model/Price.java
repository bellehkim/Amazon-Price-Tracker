package com.example.amazonpricetracker.backend.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Model class for Amazon product price data returned from the Canopy API.
 */
@Data
public class Price {
    @SerializedName("symbol")
    private String symbol;

    @SerializedName("value")
    private double value;

    @SerializedName("currency")
    private String currency;

    @SerializedName("display")
    private String display;
}
