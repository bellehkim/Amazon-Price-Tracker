package com.example.amazonpricetracker.frontend.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.amazonpricetracker.R;
import com.example.amazonpricetracker.backend.utils.AmazonIdExtractor;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddProductActivity extends AppCompatActivity {
    private TextInputLayout inputLayout;
    private TextInputEditText editTextUrlOrAsin;
    private Button buttonAddProduct;
    private AmazonIdExtractor amazonIdExtractor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

//        inputLayout = findViewById(R.id.inputLayout);
//        editTextUrlOrAsin = findViewById(R.id.editTextUrlOrAsin);
//        buttonAddProduct = findViewById(R.id.buttonAddProduct);
        amazonIdExtractor = new AmazonIdExtractor();

        tapAddButton();
    }

    private void tapAddButton() {
        buttonAddProduct.setOnClickListener(v -> {
            String input = editTextUrlOrAsin.getText().toString().trim();
            if (TextUtils.isEmpty(input)) {
                Toast.makeText(this, "Please enter a URL or ASIN",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            String asin = amazonIdExtractor.extractASIN(input);

            if (asin != null) {
                Toast.makeText(this, "Extracted ASIN: "
                        + asin, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Invalid URL or ASIN", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
