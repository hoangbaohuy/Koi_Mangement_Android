package com.example.koimanagement.Activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide; // Thêm import Glide
import com.example.koimanagement.Models.Product;
import com.example.koimanagement.R;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView imageProduct, imageBack;
    private TextView txtName, txtPrice, txtStockQuantity, txtDescription;
    private AppCompatButton btnAddToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_item_detail);

        // Get the passed product object
        Product product = (Product) getIntent().getSerializableExtra("product");

        // Initialize views
        imageProduct = findViewById(R.id.imageProduct);
        imageBack = findViewById(R.id.imageBack);
        txtName = findViewById(R.id.txtName);
        txtPrice = findViewById(R.id.txtPrice);
        txtStockQuantity = findViewById(R.id.txtStockQuantity);
        txtDescription = findViewById(R.id.txtDescription);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        // Set product details
        // Sử dụng Glide để tải hình ảnh từ URL
        Glide.with(this)
                .load(product.getImage()) // Giả sử bạn có phương thức getImageUrl() trong Product để lấy URL
                .placeholder(R.drawable.replace) // Hình ảnh thay thế trong lúc tải
                .error(R.drawable.replace) // Hình ảnh hiển thị khi có lỗi
                .into(imageProduct);

        txtName.setText(product.getProductName());
        txtPrice.setText(String.format("%.2f", product.getPrice()) + " VND");
        txtStockQuantity.setText("Stock: " + product.getStockQuantity());
        txtDescription.setText(product.getProductDescription());

        // Back button click listener
        imageBack.setOnClickListener(v -> finish());

        // Add to cart button click listener
        btnAddToCart.setOnClickListener(v -> {
            // Handle add to cart logic here
            Toast.makeText(this, product.getProductName() + " added to cart", Toast.LENGTH_SHORT).show();
        });
    }
}
