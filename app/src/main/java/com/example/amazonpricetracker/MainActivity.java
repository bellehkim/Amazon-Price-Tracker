package com.example.amazonpricetracker;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amazonpricetracker.backend.dao.ProductDao;
import com.example.amazonpricetracker.backend.managers.FavoriteProductManager;
import com.example.amazonpricetracker.backend.model.Product;
import com.example.amazonpricetracker.frontend.ProductAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public ProductAdapter allProductAdapter;
    public ProductAdapter favAdapter;

    public ProductDao productDao;
    FavoriteProductManager favoriteProductManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Calls the onCreate method of the parent class (AppCompatActivity)
        // AppCompatActivity itself performs essential setup for the activity, including restoring
        // it's previous state if it's being recreated (which is what the parameter for)
        super.onCreate(savedInstanceState);
        // App's content can draw under the system bars (e.g. status bar at the top and navigation bar at the bottom)
        EdgeToEdge.enable(this);
        // Makes the UI defined in XML visible on the scree
        setContentView(R.layout.activity_main);

        favoriteProductManager = new FavoriteProductManager();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerViewProducts = findViewById(R.id.recyclerView);
        if (recyclerViewProducts == null) {
            Log.e("MainActivity", "RecyclerView not found!");
            return;
        }
        productDao = new ProductDao();
        List<Product> productList = productDao.loadProductsFromCSV(getAssets());


        if (productList == null || productList.isEmpty()) {
            Log.e("MainActivity", "No products loaded from CSV");
            productList = new ArrayList<>();
        } else {
            Log.d("MainActivity", "Product list size" + productList.size());
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewProducts.setLayoutManager(layoutManager);
        recyclerViewProducts.setHasFixedSize(false);

        allProductAdapter = new ProductAdapter(productList, this, favoriteProductManager);
        favAdapter = new ProductAdapter(favoriteProductManager.getFavorites(),
                this, favoriteProductManager);

        recyclerViewProducts.setAdapter(allProductAdapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                recyclerViewProducts.setAdapter(allProductAdapter);
                return true;
            } else if (id == R.id.nav_favorites) {
                recyclerViewProducts.setAdapter(favAdapter);
                return true;
            }
            return false;
        });
        // TODO: ADD API call logic here
    }
}
