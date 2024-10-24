package com.example.koimanagement.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.koimanagement.Adapters.OrderAdapter;
import com.example.koimanagement.Models.Response.OrderResponse;
import com.example.koimanagement.R;

import java.security.cert.CertificateException;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class OrderActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;

    private static final String BASE_URL = "https://10.0.2.2:7177/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_list);
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the previous screen
                onBackPressed(); // This will simulate the back button press
            }
        });
        recyclerView = findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadUserIdAndFetchOrders();
    }

    private void loadUserIdAndFetchOrders() {
        // Lấy JWT từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String jwtToken = sharedPreferences.getString("jwtToken", null);

        if (jwtToken != null) {
            // Giải mã JWT để lấy userID
            DecodedJWT decodedJWT = JWT.decode(jwtToken);
            String userIdString = decodedJWT.getClaim("userId").asString(); // Lấy userId dưới dạng chuỗi

            if (userIdString != null) {
                try {
                    int userId = Integer.parseInt(userIdString); // Chuyển đổi chuỗi thành int
                    fetchOrders(userId); // Gọi hàm fetchOrders với userId đã lấy được
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Invalid userId format", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Không tìm thấy userId trong token", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Token không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchOrders(int userId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getUnsafeOkHttpClient())  // Sử dụng client bỏ qua SSL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OrderApiService apiService = retrofit.create(OrderApiService.class);
        Call<List<OrderResponse>> call = apiService.getOrders(userId);

        call.enqueue(new Callback<List<OrderResponse>>() {
            @Override
            public void onResponse(Call<List<OrderResponse>> call, Response<List<OrderResponse>> response) {
                if (response.isSuccessful()) {
                    List<OrderResponse> orders = response.body();
                    orderAdapter = new OrderAdapter(orders, OrderActivity.this);
                    recyclerView.setAdapter(orderAdapter);
                } else {
                    Toast.makeText(OrderActivity.this, "Failed to load orders", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OrderResponse>> call, Throwable t) {
                Toast.makeText(OrderActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // OkHttp client bỏ qua SSL cho phát triển
    private OkHttpClient getUnsafeOkHttpClient() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public interface OrderApiService {
        @GET("Order/get-order")
        Call<List<OrderResponse>> getOrders(@Query("userId") int userId);
    }
}
