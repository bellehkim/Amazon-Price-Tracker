package com.example.amazonpricetracker.backend.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

// Blueprint for Retrofit API calls
public interface CanopyApi {
//    @GET("amazon/products")
//    Call<List<ProductRequest>> getProducts();

    @GET("amazon/product")
    Call<ProductRequest> getProductByAsin(
            @Header("API-KEY") String apiKey,   // Send API key
            @Query("asin") String asin          // send asin
    );
}
