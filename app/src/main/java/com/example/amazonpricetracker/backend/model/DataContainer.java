package com.example.amazonpricetracker.backend.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class DataContainer {
    @SerializedName("amazonProduct")
    private AmazonProduct amazonProduct;
}
