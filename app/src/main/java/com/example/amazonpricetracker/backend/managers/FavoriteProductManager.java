package com.example.amazonpricetracker.backend.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

/**
 * Singleton class for managing favorite products.
 */
@Data
public class FavoriteProductManager {

    private static final String TAG = "FavoriteProductManager";
    private static final String PREFS_NAME = "favoritePrefs";
    private static final String KEY_FAVORITES_ASINS = "favorites_asins";

    private static FavoriteProductManager INSTANCE;
    private final SharedPreferences sharedPreferences;
    private final Set<String> favoritesAsins = new HashSet<>();


    /**
     * Private constructor to prevent instantiation from outside the class.
     * @param context the application context
     */
    private FavoriteProductManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        loadFavorite();
    }

    /**
     * Get the singleton instance of the FavoriteProductManager.
     * @param context the application context
     * @return the singleton instance
     */
    public static synchronized FavoriteProductManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new FavoriteProductManager(context.getApplicationContext());
        }
        return INSTANCE;
    }

    /**
     * Loads the favorite products from the shared preferences.
     * <p>
     *     Clears the current list of favorites and adds the ASINs from the shared preferences.
     * </p>
     */
    public void loadFavorite() {
        Set<String> savedAsins = sharedPreferences.
                getStringSet(KEY_FAVORITES_ASINS, new HashSet<>());
        favoritesAsins.clear();
        favoritesAsins.addAll(savedAsins);
        Log.d(TAG, "Loaded favorites: " + favoritesAsins.size());
    }

    /**
     * Saves the favorite products to the shared preferences.
     */
    private void saveFavorites() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_FAVORITES_ASINS, favoritesAsins);
        editor.apply();
        Log.d(TAG, "Saved favorites: " + favoritesAsins.size());
    }

    /**
     * Adds a product to the list of favorites.
     * @param asin the ASIN of the product
     */
    public void addFavorites(String asin) {
        if (asin != null && favoritesAsins.add(asin)) {
            saveFavorites();
            Log.d(TAG, "Added favorite: " + asin);

        }
    }

    /**
     * Removes a product from the list of favorites.
     * @param asin the ASIN of the product
     */
    public void removeFavorites(String asin) {
        if (asin != null && favoritesAsins.remove(asin)) {
            saveFavorites();
            Log.d(TAG, "Removed favorite: " + asin);
        }
    }

    /**
     * Toggles the favorite status of a product.
     * <ul>
     *     <li>If currently a favorite, removes it</li>
     *     <li>If not a favorite, adds it</li>
     * </ul>
     *
     * @param asin the ASIN of the product
     */
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

    /**
     * Checks if a product is a favorite.
     * @param asin the ASIN of the product
     * @return true if the product is a favorite, false otherwise
     */
    public boolean isFavorite(String asin) {
        return asin != null && favoritesAsins.contains(asin);
    }

}
