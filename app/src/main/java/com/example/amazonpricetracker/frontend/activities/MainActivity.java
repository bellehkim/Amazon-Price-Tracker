package com.example.amazonpricetracker.frontend.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amazonpricetracker.R;
import com.example.amazonpricetracker.backend.dao.ProductDao;
import com.example.amazonpricetracker.backend.managers.FavoriteProductManager;
import com.example.amazonpricetracker.backend.model.Product;
import com.example.amazonpricetracker.frontend.ProductAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BottomNavigationView bottomNavigationView;
    private ProductAdapter allProductAdapter;
    private ProductAdapter favAdapter;

    private ProductDao productDao;
    private FavoriteProductManager favoriteProductManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Calls the onCreate method of the parent class (AppCompatActivity)
        // AppCompatActivity itself performs essential setup for the activity, including restoring
        // it's previous state if it's being recreated (which is what the parameter for)
        super.onCreate(savedInstanceState);
        // App's content can draw under the system bars
        // (e.g. status bar at the top and navigation bar at the bottom)
        EdgeToEdge.enable(this);
        // Makes the UI defined in XML visible on the scree
        setContentView(R.layout.activity_main);

        applyWindowInsets();

        recyclerView = findViewById(R.id.recyclerView);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        setupRecyclerView();

        // ---Data / Manager---
        favoriteProductManager = new FavoriteProductManager();

        productDao = new ProductDao();

        List<Product> productList = loadProductsFromCSV();
        favoriteProductManager.setProducts(productList);

        // ---Adapter---
        allProductAdapter = new ProductAdapter(productList, this, favoriteProductManager);
        favAdapter = new ProductAdapter(favoriteProductManager.getFavorites(),
                this, favoriteProductManager);

        // Default = Home (all products)
        recyclerView.setAdapter(allProductAdapter);

        // Bottom navigation
        setupBottomNavigation();

        // Add button
        FloatingActionButton fabOpenAddProductScreen;

        fabOpenAddProductScreen = findViewById(R.id.fab_open_add_product_screen);
        fabOpenAddProductScreen.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
            startActivity(intent);
        });

        // TODO: Optional: set default selected tab explicitly  ?????
        if (bottomNavigationView.getSelectedItemId() == 0) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
        // TODO: ADD API call logic here
    }

    private void applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),
                (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                });
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
    }

    private List<Product> loadProductsFromCSV() {
        List<Product> productList = productDao.loadProductsFromCSV(getAssets());

        if (productList == null || productList.isEmpty()) {
            Log.e("MainActivity", "No products loaded from CSV");
            return new ArrayList<>();
        } else {
            Log.d("MainActivity", "Product list size" + productList.size());
        }

        return productList;
    }

    private void setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                recyclerView.setAdapter(allProductAdapter);
                return true;
            } else if (id == R.id.nav_favorites) {
                recyclerView.setAdapter(favAdapter);
                return true;
            }
            return false;
        });
    }
}
