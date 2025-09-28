package com.example.amazonpricetracker.backend.dao;

import android.content.res.AssetManager;
import android.util.Log;

import com.example.amazonpricetracker.backend.model.Product;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {
    public List<Product> loadProductsFromCSV(AssetManager assetManager) {
        InputStream inputStream = null;
        BufferedReader reader = null;
        List<Product> productList = new ArrayList<>();

        try {
            inputStream = assetManager.open("products.csv");
            reader = new BufferedReader(new InputStreamReader(inputStream));
            boolean isFirstLine = true;

            String cvsLine;
            while ((cvsLine = reader.readLine()) != null) {

                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] row = cvsLine.split(",");

                if (row.length == 7) {
                    try {
                        String name = row[0].trim();
                        double currentPrice = parseDouble(row[1].trim());
                        double originalPrice = parseDouble(row[2].trim());
                        String priceChange = row[3].trim();
                        String discount = row[4].trim();
                        boolean isFavorite = parseBoolean(row[6].trim());
//
//                        productList.add(new Product(
//                                asin,
//                                name,
//                                currentPrice,
//                                0.0,
//                                "",
//                                "",
//                                false
//                        ));
                    } catch (NumberFormatException e) {
                        Log.e("MainActivity", "Error parsing CSV line: " + cvsLine, e);
                    }
                } else {
                    Log.w("MainActivity", "Skipping invalid CSV line: " + cvsLine);
                }
            }
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading CSV file", e);
        } finally {
            try {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        Log.e("MainActivity", "Error closing input stream", e);
                    }
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                Log.e("MainActivity", "Error closing reader", e);
            }
        }

        return productList;
    }

    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private static boolean parseBoolean(String value) {
        try {
            return Boolean.parseBoolean(value);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
