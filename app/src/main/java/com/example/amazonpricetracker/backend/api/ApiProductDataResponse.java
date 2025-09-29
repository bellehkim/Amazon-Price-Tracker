package com.example.amazonpricetracker.backend.api;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Response from the Canopy API when getting product data.
 */
@Data
public class ApiProductDataResponse {

    @SerializedName("data")
    private DataContainer data;
}
