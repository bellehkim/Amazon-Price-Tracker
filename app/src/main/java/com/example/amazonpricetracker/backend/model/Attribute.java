package com.example.amazonpricetracker.backend.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Attribute {
    @SerializedName("name")
    private String name;
    @SerializedName("value")
    private String value;
}
