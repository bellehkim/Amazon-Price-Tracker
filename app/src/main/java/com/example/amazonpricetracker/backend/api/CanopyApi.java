package com.example.amazonpricetracker.backend.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Retrofit interface for the Canopy API.
 */
public interface CanopyApi {

    /**
     * Fetches product details from the Canopy API by ASIN and domain.
     * @param apiKey API key for the Canopy API
     * @param asin   Amazon Standard Identification Number (ASIN) of the product
     * @param domain Domain of the product (e.g., "US")
     * @return a {@link Call} that can be executed or enqueued to receive an
     *         {@link ApiProductDataResponse}
     */
    @GET("api/amazon/product")
    Call<ApiProductDataResponse> getProduct(
            @Header("API-KEY") String apiKey,
            @Query("asin") String asin,
            @Query("domain") String domain
//            @Query("url") String url
        );
}
