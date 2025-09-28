package com.example.amazonpricetracker.frontend.activities;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.amazonpricetracker.BuildConfig;
import com.example.amazonpricetracker.R;
import com.example.amazonpricetracker.backend.api.ApiProductDataResponse;
import com.example.amazonpricetracker.backend.api.CanopyApi;
import com.example.amazonpricetracker.backend.api.CanopyClient;
import com.example.amazonpricetracker.backend.model.DataContainer;
import com.example.amazonpricetracker.backend.managers.AllProductManager;
import com.example.amazonpricetracker.backend.managers.FavoriteProductManager;
import com.example.amazonpricetracker.backend.model.AmazonProduct;
import com.example.amazonpricetracker.backend.model.Product;
import com.example.amazonpricetracker.backend.utils.AsinExtractor;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity {
    private MaterialToolbar toolbarClose;
    private TextInputLayout inputLayout;
    private TextInputEditText inputEditText;
    private MaterialButton buttonAddProduct;
    private FavoriteProductManager favoriteProductManager;
    private AsinExtractor asinExtractor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

//        asinExtractor = new AsinExtractor();

        // Find views by their IDs
        toolbarClose = findViewById(R.id.button_close);
        inputLayout = findViewById(R.id.input_layout_product);
        inputEditText = findViewById(R.id.edit_text_product_input);
        buttonAddProduct = findViewById(R.id.btn_add_product);

        // Setup Toolbar
        toolbarClose.setNavigationOnClickListener(v -> finish());

        // Setup Add Button
        buttonAddProduct.setOnClickListener(v -> tapAddButton());

        favoriteProductManager = FavoriteProductManager.getInstance(this);

        // Update hint text for the input field in your XML (e.g., activity_add_product.xml)
        // From "Enter Product URL or ASIN" to "Enter Product ASIN"
        // Example:
        // <com.google.android.material.textfield.TextInputLayout
        //     ...
        //     android:hint="Enter Product ASIN">
        //     <com.google.android.material.textfield.TextInputEditText
        //         android:id="@+id/edit_text_product_input" ... />
        // </com.google.android.material.textfield.TextInputLayout>
    }

    private void tapAddButton() {
        String input = inputEditText.getText() != null
                ? this.inputEditText.getText().toString().trim() : "";

        if (TextUtils.isEmpty(input)) {
            inputLayout.setError("Please enter a URL or ASIN");
            Toast.makeText(this, "Please enter a URL or ASIN",
                    Toast.LENGTH_SHORT).show();
            return;
       }
//        else {
//            if (input.length() != 10 || !input.matches("^[A-Z0-9]{10}$")) {
//                inputLayout.setError("Invalid ASIN format (should be 10 alphanumeric characters)");
//                Toast.makeText(this, "Invalid ASIN format", Toast.LENGTH_SHORT).show();
//                return;
//            }
        inputLayout.setError(null); // Clear error if any

        String asinToProcess = null;

        // Distinguish ASIN
        if (input.length() == 10 && input.matches("^[A-Z0-9]{10}$")) {
            asinToProcess = input;
        }
        // 1) Extract ASIN from URL
        else if (input.toLowerCase().startsWith("http")
                || input.toLowerCase().contains("www")
                || input.toLowerCase().contains("amazon.")) {
            Log.d(TAG, "Extracting ASIN from URL: " + input);
            if (asinExtractor != null) {
                asinToProcess = asinExtractor.extractAsin(input);
            } else {
                Log.e(TAG, "AsinExtractor is not initialized");
                Toast.makeText(this, "Error: AsinExtractor is not available",
                        Toast.LENGTH_SHORT).show();
                return;
            }

        }

        Log.d(TAG, "Extracted ASIN for API call: " + asinToProcess);

        if (asinToProcess == null
                || asinToProcess.isEmpty()
                || !asinToProcess.matches("^[A-Z0-9]{10}$")) {
            inputLayout.setError("Invalid or unextracted ASIN");
            Toast.makeText(this, "Could not find a valid ASIN",
                    Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Invalid or unextracted ASIN: "
                    + asinToProcess + " from URL: " + input);
            return;
        }

        // If we have a valid ASIN, proceed with the API call

        // 2) Disable button to avoid double taps
        buttonAddProduct.setEnabled(false);

        // 3) Call Canopy REST
        CanopyApi api = CanopyClient.getApi();
        String apiKey = BuildConfig.CANOPY_API_KEY;
        String usDomain = "US";
//        B09DDD6YGJ

        api.getProduct(apiKey, asinToProcess, usDomain).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ApiProductDataResponse> call,
                                   @NonNull Response<ApiProductDataResponse> response) {
                buttonAddProduct.setEnabled(true);

                if (response.isSuccessful() || response.body() != null) {
                    ApiProductDataResponse apiResponse = response.body();
                    DataContainer dataContainer = apiResponse.getData();

                    if (dataContainer != null && dataContainer.getAmazonProduct() != null) {
                        AmazonProduct fetchedProductDetails = dataContainer.getAmazonProduct();

                        String productAsin = fetchedProductDetails.getAsin();
                        String productTitle = fetchedProductDetails.getTitle();
                        String productUrl = fetchedProductDetails.getUrl();
                        double productPrice = fetchedProductDetails.getPrice().getValue();
                        String productMainImageUrl = fetchedProductDetails.getMainImageUrl();

                        if (productAsin != null && !productAsin.isEmpty() && productTitle != null) {
                            Product productToAdd = new Product(
                                    productAsin,
                                    productTitle,
                                    productPrice,
                                    0.0,
                                    productMainImageUrl,
                                    false
                            );
                            Log.d(TAG, "AddProduct - Created Product object: "
                                    + productToAdd.getName() + " with ASIN "
                                    + productToAdd.getAsin());

                            AllProductManager.getInstance().addOrUpdateProduct(productToAdd);
                            Log.i(TAG, "AddProduct - Product '" + productToAdd.getName()
                                    + "' ADDED to AllProductManager.");

                            Toast.makeText(AddProductActivity.this, "Added: "
                                    + productToAdd.getName(), Toast.LENGTH_SHORT).show();
                            setResult(Activity.RESULT_OK);
                            finish();
                        } else {
                            Log.e(TAG,
                                    "Data container or Amazon product not found in the response");
                            Toast.makeText(AddProductActivity.this,
                                    "Product data not found in response.",
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // Handle API error
                        if (response.errorBody() != null) {
                            try {
                                String errorBodyStr = response.errorBody().string();
                                Log.e(TAG, "AddProduct API Error: "
                                        + response.code()
                                        + ": " + errorBodyStr);
                            } catch (Exception e) {
                                Log.e(TAG, "Error reading error body", e);
                            }
                        } else {
                            Log.e(TAG, "AddProduct API Error: No error body"
                                    + response.code()
                                    + ": No Error Body");
                        }
                        Toast.makeText(AddProductActivity.this,
                                "Failed to fetch product details (Code: "
                                        + response.code() + ")",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure (@NonNull Call < ApiProductDataResponse > call,
                                   @NonNull Throwable t) {
                Toast.makeText(AddProductActivity.this, "Network Failure: "
                        + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("AddProductActivity", "Network Failure: " + t.getMessage(), t);
            }
        });
    }
}
