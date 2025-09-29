package com.example.amazonpricetracker.backend.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Singleton class for the Canopy API client.
 */
public class CanopyClient {
    private static final String BASE_URL = "https://rest.canopyapi.co";
    private static CanopyApi api;
    private static CanopyClient instance;

    /**
     * Private constructor to prevent instantiation from outside the class.
     */
    private CanopyClient() {
        // Private constructor to prevent instantiation
        Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        api = retrofit.create(CanopyApi.class);
    }

    /**
     * Get the singleton instance of the Canopy API client.
     * @return the singleton instance
     */
    public static synchronized CanopyClient getInstance() {
        if (instance == null) {
            instance = new CanopyClient();
        }
        return instance;
    }

    /**
     * Get the Canopy API client.
     * @return the Canopy API client
     */
    public static CanopyApi getApi() {
        if (api == null) {
            instance = new CanopyClient();
        }
        return api;
    }
}
