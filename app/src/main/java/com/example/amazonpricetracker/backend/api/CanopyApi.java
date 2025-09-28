package com.example.amazonpricetracker.backend.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

// Blueprint for Retrofit API calls
public interface CanopyApi {
    @GET("api/amazon/product")
    Call<ApiProductDataResponse> getProduct(
            @Header("API-KEY") String apiKey,
            @Query("asin") String asin,
            @Query("domain") String domain
//            @Query("url") String url
        );

}
