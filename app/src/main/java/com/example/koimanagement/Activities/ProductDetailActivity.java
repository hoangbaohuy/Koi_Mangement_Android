package com.example.koimanagement.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NotificationCompat;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bumptech.glide.Glide;
import com.example.koimanagement.Fragments.CartFragment;
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
import java.text.NumberFormat;
import java.util.Locale;

import javax.net.ssl.X509TrustManager;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView imageProduct, imageBack;
    private TextView txtName, txtPrice, txtStockQuantity, txtDescription;
    private AppCompatButton btnAddToCart;
    private OkHttpClient client;
    private ImageView imageMinus, imageAdd;
    private TextView txtOrder;
    private int quantity = 1;
    private static final String CHANNEL_ID = "cart_notifications";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_item_detail);


        // Retrieve the JWT from SharedPreferences

//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
//        String jwtToken = sharedPreferences.getString("jwtToken", null);
//        // Get the userId from the JWT
//        int userId = (jwtToken != null) ? getUserIdFromJwt(jwtToken) : -1;


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
        imageMinus = findViewById(R.id.imageMinus);
        imageAdd = findViewById(R.id.imageAdd);
        txtOrder = findViewById(R.id.txtOrder);
        txtOrder.setText(String.valueOf(quantity));
        // Use UnsafeOkHttpClient
        client = UnsafeOkHttpClient.getUnsafeOkHttpClient();
        imageMinus.setOnClickListener(v -> {
            if (quantity > 1) { // Ensure quantity doesn't go below 1
                quantity--;
                txtOrder.setText(String.valueOf(quantity)); // Update the TextView
            } else {
                Toast.makeText(this, "Quantity cannot be less than 1", Toast.LENGTH_SHORT).show();
            }
        });

// Increase quantity button click listener
        imageAdd.setOnClickListener(v -> {
            quantity++;
            txtOrder.setText(String.valueOf(quantity)); // Update the TextView
        });
        // Set product details
        Glide.with(this)
                .load(product.getImage()) // Lấy URL hình ảnh từ đối tượng Product
                .placeholder(R.drawable.replace)
                .error(R.drawable.replace)
                .into(imageProduct);

        txtName.setText(product.getProductName());
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN")); // Định dạng theo ngôn ngữ và vùng miền Việt Nam
        txtPrice.setText(numberFormat.format(product.getPrice()) + " VND");
        txtStockQuantity.setText("Stock: " + product.getStockQuantity());
        txtDescription.setText(product.getProductDescription());

        // Back button click listener
        imageBack.setOnClickListener(v -> finish());
        btnAddToCart.setOnClickListener(v -> {
            // Lấy SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
            String jwtToken = sharedPreferences.getString("jwtToken", null);

            if (jwtToken != null) {
                try {
                    // Giải mã JWT để lấy userId
                    DecodedJWT decodedJWT = JWT.decode(jwtToken);
                    String userIdString = decodedJWT.getClaim("userId").asString();

                    if (userIdString != null) {
                        int userId = Integer.parseInt(userIdString);
                        // Thêm sản phẩm vào giỏ hàng
                        addToCart(product.getProductId(), quantity, userId);
                    } else {
                        Toast.makeText(ProductDetailActivity.this, "User ID not found in JWT", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(ProductDetailActivity.this, "Error decoding JWT: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ProductDetailActivity.this, "JWT token not found", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void addToCart(int productId, int quantity,int userId) {

        // Đảm bảo URL giống như trong yêu cầu curl
        String url = "https://10.0.2.2:7177/Cart/add-to-cart?userId=" + userId
                + "&ProductID=" + productId + "&Quantity=" + quantity;

        // Tạo yêu cầu
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "{}")) // Gửi body rỗng
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
                        // Xử lý phản hồi JSON
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        String returnedProductName = jsonResponse.getString("productName");
                        int returnedQuantity = jsonResponse.getInt("quantity");

                        runOnUiThread(() -> {
                            Toast.makeText(ProductDetailActivity.this, "Added to cart: " + returnedProductName + ", Quantity: " + returnedQuantity, Toast.LENGTH_SHORT).show();
                            showAddToCartNotification(returnedProductName,returnedQuantity);
                        });

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
    private void createNotificationChannel() {
        // Check if running on Android Oreo or higher
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // Set channel attributes
            CharSequence name = "Cart Notifications";
            String description = "Channel for cart-related notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            // Create the NotificationChannel
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
    private void showAddToCartNotification(String productName, int quantity) {
        createNotificationChannel(); // Ensure the notification channel exists

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, CartFragment.class); // Replace with the activity you want to open
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Create PendingIntent with IMMUTABLE flag
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.shopping_cart) // Use a valid drawable resource
                .setContentTitle("Added to Cart")
                .setContentText("Product: " + productName + ", Quantity: " + quantity)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Show the notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(1, builder.build());
        } else {
            Toast.makeText(this, "Notification manager is null", Toast.LENGTH_SHORT).show();
        }
    }

}