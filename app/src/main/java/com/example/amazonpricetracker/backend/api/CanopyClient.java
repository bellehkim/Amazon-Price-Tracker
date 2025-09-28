package com.example.amazonpricetracker.backend.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CanopyClient {
    private static final String BASE_URL = "https://rest.canopyapi.co";
    private static CanopyApi api;
    private static CanopyClient instance;

    private CanopyClient() {
        // Private constructor to prevent instantiation
        Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        api = retrofit.create(CanopyApi.class);
    }
    public static synchronized CanopyClient getInstance() {
        if (instance == null) {
            instance = new CanopyClient();
        }
        return instance;
    }
    public static CanopyApi getApi() {
        if (api == null) {
            instance = new CanopyClient();
        }
        return api;
    }
}
