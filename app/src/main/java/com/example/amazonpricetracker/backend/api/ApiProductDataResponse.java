package com.example.amazonpricetracker.backend.api;

import com.example.amazonpricetracker.backend.model.DataContainer;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ApiProductDataResponse {

    @SerializedName("data")
    private DataContainer data;
}
