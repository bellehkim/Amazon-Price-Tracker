package com.example.amazonpricetracker.backend.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.amazonpricetracker.backend.model.Product;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Data;

@Data
public class FavoriteProductManager {

    private static final String TAG = "FavoriteProductManager";
    private static final String PREFS_NAME = "favoritePrefs";
    private static final String KEY_FAVORITES_ASINS = "favorites_asins";

    private static FavoriteProductManager INSTANCE;
    private final SharedPreferences sharedPreferences;
    private final Set<String> favoritesAsins = new HashSet<>();


    private FavoriteProductManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        loadFavorite();
    }

    public static synchronized FavoriteProductManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new FavoriteProductManager(context.getApplicationContext());
        }
        return INSTANCE;
    }

    public void loadFavorite() {
        Set<String> savedAsins = sharedPreferences.
                getStringSet(KEY_FAVORITES_ASINS, new HashSet<>());
        favoritesAsins.clear();
        favoritesAsins.addAll(savedAsins);
        Log.d(TAG, "Loaded favorites: " + favoritesAsins.size());
    }

    private void saveFavorites() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_FAVORITES_ASINS, favoritesAsins);
        editor.apply();
        Log.d(TAG, "Saved favorites: " + favoritesAsins.size());
    }

    public void addFavorites(String asin) {
        if (asin != null && favoritesAsins.add(asin)) {
            saveFavorites();
            Log.d(TAG, "Added favorite: " + asin);

        }
    }
    public void removeFavorites(String asin) {
        if (asin != null && favoritesAsins.remove(asin)) {
            saveFavorites();
            Log.d(TAG, "Removed favorite: " + asin);
        }
    }

    public void toggleFavorite(String asin) {
        if (asin == null) {
            return;
        }
        if (isFavorite(asin)) {
            removeFavorites(asin);
        } else {
            addFavorites(asin);
        }
    }

    public boolean isFavorite(String asin) {
        return asin != null && favoritesAsins.contains(asin);
    }

}
