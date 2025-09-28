package com.example.amazonpricetracker.backend.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class AmazonProduct {

    @SerializedName("title")
    private String title;

    @SerializedName("subtitle")
    private String subtitle;

    @SerializedName("brand")
    private String brand;

    @SerializedName("url")
    private String url;

    @SerializedName("asin")
    private String asin; // This is crucial for matching

    @SerializedName("isPrime")
    private boolean isPrime;

    @SerializedName("isNew")
    private boolean isNew;

    @SerializedName("price")
    private Price price;

    @SerializedName("mainImageUrl")
    private String mainImageUrl;

    @SerializedName("imageUrls")
    private List<String> imageUrls; // List of image URL strings

    @SerializedName("rating")
    private double rating;

    @SerializedName("ratingsTotal")
    private int ratingsTotal;

    @SerializedName("featureBullets")
    private List<String> featureBullets;
}
