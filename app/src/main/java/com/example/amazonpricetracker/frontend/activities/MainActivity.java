package com.example.amazonpricetracker.frontend.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amazonpricetracker.R;
import com.example.amazonpricetracker.backend.api.CanopyApi;
import com.example.amazonpricetracker.backend.api.CanopyClient;
import com.example.amazonpricetracker.backend.managers.AllProductManager;
import com.example.amazonpricetracker.backend.managers.FavoriteProductManager;
import com.example.amazonpricetracker.backend.model.Product;
import com.example.amazonpricetracker.backend.utils.AsinExtractor;
import com.example.amazonpricetracker.frontend.ProductAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private BottomNavigationView bottomNavigationView;
    private ProductAdapter allProductAdapter;
    private ProductAdapter favAdapter;
    private ProgressBar loadingProgressBar;
    private FavoriteProductManager favoriteProductManager;
    private AllProductManager allProductManager;
    private CanopyApi canopyApi;
    private ActivityResultLauncher<Intent> addProductLauncher;
    private AsinExtractor asinExtractor;

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.
     *
     */
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

        // Initialize managers
        allProductManager = allProductManager.getInstance();
        favoriteProductManager = favoriteProductManager.getInstance(getApplicationContext());
        canopyApi = CanopyClient.getApi();

        // Initialize UI components
        recyclerView = findViewById(R.id.recyclerView);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);

        setupRecyclerView();

        // Initialize adapters with with FavoriteProductManager for toggling
        allProductAdapter = new ProductAdapter(new ArrayList<>(), this,
                favoriteProductManager);
        favAdapter = new ProductAdapter(new ArrayList<>(), this, favoriteProductManager);

        // Default = Home (all products)
        recyclerView.setAdapter(allProductAdapter);

        // Bottom navigation
        setupBottomNavigation();
        setupAddProductFAB();

        // ActivityResultLauncher when AddProductActivity returns
        addProductLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Log.d(TAG, "Returned from AddProductActivity with RESULT_OK");
                        updateAdapter();
                    }
                });
        updateAdapter();

    }

    /**
     * Applies system bar insets to the main layout.
     */
    private void applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),
                (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                });
    }

    /**
     * Sets up the RecyclerView with a LinearLayoutManager.
     */
    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
    }

//    private List<Product> loadProductsFromCSV() {
//        List<Product> productList = productDao.loadProductsFromCSV(getAssets());
//
//        if (productList == null || productList.isEmpty()) {
//            Log.e("MainActivity", "No products loaded from CSV");
//            return new ArrayList<>();
//        } else {
//            Log.d("MainActivity", "Product list size" + productList.size());
//        }
//
//        return productList;
//    }

    /**
     * Sets up the BottomNavigationView with item selection listener.
     */
    private void setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                recyclerView.setAdapter(allProductAdapter);
                Log.d(TAG, "Home screen selected");
                return true;
            } else if (id == R.id.nav_favorites) {
                recyclerView.setAdapter(favAdapter);
                Log.d(TAG, "Favorites screen selected");
                return true;
            }
            return false;
        });

        // Set default to home if nothings selected
        if (bottomNavigationView.getSelectedItemId() != R.id.nav_favorites
                && bottomNavigationView.getMenu().findItem(R.id.nav_home) != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
    }

    /**
     * Sets up the FloatingActionButton to open the AddProductActivity.
     */
    private void setupAddProductFAB() {
        FloatingActionButton fabOpenAddProductScreen
                = findViewById(R.id.fab_open_add_product_screen);
        fabOpenAddProductScreen.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
            addProductLauncher.launch(intent);
        });
    }

    /**
     * Updates the adapters with the latest data from the managers.
     * <ul>
     *     <li>Home screen: All products</li>
     *     <li>Favorites screen: Only favorite products</li>
     * </ul>
     */
    private void updateAdapter() {
        List<Product> masterProductsList = allProductManager.getAllProducts();
        List<Product> productsForHome = new ArrayList<>();
        List<Product> productsForFavorites = new ArrayList<>();

        Log.d(TAG, "Updating adapter with " + masterProductsList.size());

        for (Product masterProduct : masterProductsList) {
            Product displayProduct = new Product(
                    masterProduct.getAsin(),
                    masterProduct.getName(),
                    masterProduct.getCurrentPrice(),
//                    masterProduct.getOriginalPrice(),
                    0.0,
                    masterProduct.getImage(),
                    favoriteProductManager.isFavorite(masterProduct.getAsin())
            );

            productsForHome.add(displayProduct);
            if (displayProduct.isFavorite()) {
                productsForFavorites.add(displayProduct);
            }
        }

        allProductAdapter.updateProductsData(productsForHome);
        favAdapter.updateProductsData(productsForFavorites);

        Log.d(TAG, "Home screen products:  " + productsForHome.size()
                + ", Favorites screen products: " + productsForFavorites.size());

        int selectedItemId = bottomNavigationView.getSelectedItemId();
        if (selectedItemId == R.id.nav_home && recyclerView.getAdapter() != allProductAdapter) {
            recyclerView.setAdapter(allProductAdapter);
        } else if (selectedItemId == R.id.nav_favorites && recyclerView.getAdapter() != favAdapter) {
            recyclerView.setAdapter(favAdapter);
        }
    }

//    private void fetchProductsFromApi() {
//        if (loadingProgressBar != null) {
//            loadingProgressBar.setVisibility(View.VISIBLE);
//        }
//        if (recyclerView != null) {
//            recyclerView.setVisibility(View.GONE);
//        }
//
//        String asin = asinExtractor.extractAsin(useroii)
//        String apiKey = BuildConfig.CANOPY_API_KEY;
//
//        canopyApi.getProduct(apiKey, )
//                .enqueue(new Callback<>() {
//            @Override
//            public void onResponse(@NonNull Call<List<Variant>> call,
//                                   @NonNull retrofit2.Response<List<Variant>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    List<Variant> variantsFromApi = response.body();
//                    Log.d(TAG, "API Success: " + variantsFromApi.size());
//
//                    List<Product> productsForAdapter = new ArrayList<>();
//                    for (Variant variant : variantsFromApi) {
//                        String name = variant.getName();
//                        double currentPrice = variant.getPrice();
//
//
//                        Product product = new Product(
//                                variant.getAsin(),
//                                name,
//                                currentPrice,
//                                0.0,
//                                false
//                        );
//                        productsForAdapter.add(product);
//                    }
//                    allProductManager.setAllProductsMap(productsForAdapter);
//                    updateAdapter();
//                } else {
//                    Log.e(TAG, "API Error: " + response.code());
//                    try (ResponseBody errorBody = response.errorBody()) {
//                        if (errorBody != null) {
//                            Log.e(TAG, "API Error Body: " + errorBody.string());
//                        }
//                    } catch (IOException e) {
//                        Log.e(TAG, "Error reading error body", e);
//                    }
//                    Toast.makeText(MainActivity.this, "Failed to load products",
//                            Toast.LENGTH_SHORT).show();
//                    allProductManager.setAllProductsMap(new ArrayList<>()); // clear on error
//                    updateAdapter();
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<List<Variant>> call, @NonNull Throwable t) {
//                if (loadingProgressBar != null) {
//                    loadingProgressBar.setVisibility(View.GONE);
//                }
//                if (recyclerView != null) {
//                    recyclerView.setVisibility(View.VISIBLE);
//                }
//                Log.e(TAG, "API Failure: " + t.getMessage(), t);
//                Toast.makeText(MainActivity.this,
//                        "Network error. Please check connection.", Toast.LENGTH_SHORT).show();
//                allProductManager.setAllProductsMap(new ArrayList<>());
//                updateAdapter();
//            }
//        });
//    }
}
