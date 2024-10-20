package com.example.koimanagement.Activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.example.koimanagement.Models.Product;
import com.example.koimanagement.R;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

import javax.net.ssl.X509TrustManager;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView imageProduct, imageBack;
    private TextView txtName, txtPrice, txtStockQuantity, txtDescription;
    private AppCompatButton btnAddToCart;
    private OkHttpClient client;

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

        // Use UnsafeOkHttpClient
        client = UnsafeOkHttpClient.getUnsafeOkHttpClient();

        // Set product details
        Glide.with(this)
                .load(product.getImage()) // Lấy URL hình ảnh từ đối tượng Product
                .placeholder(R.drawable.replace)
                .error(R.drawable.replace)
                .into(imageProduct);

        txtName.setText(product.getProductName());
        txtPrice.setText(String.format("%.2f", product.getPrice()) + " VND");
        txtStockQuantity.setText("Stock: " + product.getStockQuantity());
        txtDescription.setText(product.getProductDescription());

        // Back button click listener
        imageBack.setOnClickListener(v -> finish());

        // Add to cart button click listener
        btnAddToCart.setOnClickListener(v -> {
            Toast.makeText(this, "Add to Cart clicked", Toast.LENGTH_SHORT).show();
            addToCart(product.getProductId(), 1); // Giả sử quantity là 1
        });
    }

    private void addToCart(int productId, int quantity) {
        int userId = 884;

        String url = "https://10.0.2.2:7230/api/Cart/add-to-cart?userId=" + userId + "&productId=" + productId + "&quantity=" + quantity;

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create("", MediaType.parse("application/json")))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(ProductDetailActivity.this, "Failed to add to cart: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body() != null ? response.body().string() : "No response body";
                    try {
                        JSONObject jsonResponse = new JSONObject(responseBody);

                        // Extract data from the JSON object
                        int returnedProductId = jsonResponse.getInt("productId");
                        String returnedProductName = jsonResponse.getString("productName");
                        String returnedProductDescription = jsonResponse.getString("productDescription");
                        double returnedPrice = jsonResponse.getDouble("price");
                        int returnedStockQuantity = jsonResponse.getInt("stockQuantity");
                        String returnedImage = jsonResponse.getString("image");

                        // You can use the extracted data as needed, e.g., display it
                        runOnUiThread(() -> {
                            Toast.makeText(ProductDetailActivity.this, "Added to cart successfully: " + returnedProductName, Toast.LENGTH_SHORT).show();
                            // Optionally update UI or perform actions with the returned data
                        });

                        // Debug: Display response body
                        System.out.println("Response body: " + responseBody);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> Toast.makeText(ProductDetailActivity.this, "Failed to parse response: " + e.getMessage(), Toast.LENGTH_LONG).show());
                    }
                } else {
                    String errorMessage = response.body() != null ? response.body().string() : "Unknown error";
                    runOnUiThread(() -> Toast.makeText(ProductDetailActivity.this, "Failed to add to cart: " + errorMessage, Toast.LENGTH_LONG).show());
                }
            }
        });
    }

}
