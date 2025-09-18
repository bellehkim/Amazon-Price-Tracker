package com.example.amazonpricetracker.frontend.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.amazonpricetracker.R;
import com.example.amazonpricetracker.backend.utils.AmazonIdExtractor;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddProductActivity extends AppCompatActivity {
    private MaterialToolbar toolbarClose;
    private TextInputLayout inputLayout;
    private TextInputEditText editTextUrlOrAsin;
    private MaterialButton buttonAddProduct;
    private AmazonIdExtractor amazonIdExtractor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        amazonIdExtractor = new AmazonIdExtractor();

        // Find views by their IDs
        toolbarClose = findViewById(R.id.button_close);
        inputLayout = findViewById(R.id.input_layout_product);
        editTextUrlOrAsin = findViewById(R.id.edit_text_product_input);
        buttonAddProduct = findViewById(R.id.btn_add_product);

        // Setup Toolbar
        toolbarClose.setNavigationOnClickListener(v -> finish());

        // Setup Add Button
        buttonAddProduct.setOnClickListener(v -> { tapAddButton(); });



        tapAddButton();
    }

    private void tapAddButton() {
        buttonAddProduct.setOnClickListener(v -> {
            String input = editTextUrlOrAsin.getText().toString().trim();
            if (TextUtils.isEmpty(input)) {
                Toast.makeText(this, "Please enter a URL or ASIN",
                        Toast.LENGTH_SHORT).show();
                return;
            } else {
                inputLayout.setError(null);
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
