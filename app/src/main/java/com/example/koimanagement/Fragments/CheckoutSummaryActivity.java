package com.example.koimanagement.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.koimanagement.Activities.BillingActivity;
import com.example.koimanagement.Activities.UnsafeOkHttpClient;
import com.example.koimanagement.IService.IPaymentApi;
import com.example.koimanagement.Models.Response.OrderResponse;
import com.example.koimanagement.Models.Response.PaymentResponse;
import com.example.koimanagement.R;
import com.google.gson.Gson;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CheckoutSummaryActivity extends AppCompatActivity {

    private TextView txtOrderId, txtUserId, txtOrderDate, txtTotalPrice, txtAddress, txtPhone, txtOrderItems;
    private ImageButton btnBack; // Declare your back button
    private Button btnSuccess, btnPayment; // Declare your payment button


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_billing); // Make sure this is your correct layout file

        // Initialize TextViews
        txtOrderId = findViewById(R.id.txtOrderId);
        txtUserId = findViewById(R.id.txtUserId);
        txtOrderDate = findViewById(R.id.txtOrderDate);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        txtAddress = findViewById(R.id.txtAddress);
        txtPhone = findViewById(R.id.txtPhone);
        txtOrderItems = findViewById(R.id.txtOrderItems);
        btnSuccess = findViewById(R.id.btnSuccess);
        btnPayment = findViewById(R.id.btnPayment); // Initialize payment button
        String orderId = txtOrderId.getText().toString();        // Initialize back button
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish()); // Close the current activity

        btnSuccess.setOnClickListener(v -> {
            Intent intent = new Intent(CheckoutSummaryActivity.this, BillingActivity.class);
            startActivity(intent);
        });


        btnPayment.setOnClickListener(v -> {
            // Lấy giá trị từ TextView dưới dạng String
            String orderIdString = txtOrderId.getText().toString();

            // Kiểm tra nếu orderIdString không rỗng
            if (!orderIdString.isEmpty()) {
                proceedToPayment(orderIdString); // Truyền orderId là String
            } else {
                // Thông báo cho người dùng rằng order ID không hợp lệ
                Toast.makeText(CheckoutSummaryActivity.this, "Order ID không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });

        if (getIntent().hasExtra("orderDetails")) {
            String orderDetailsJson = getIntent().getStringExtra("orderDetails");
            OrderResponse orderResponse = parseOrderJson(orderDetailsJson);

            // Set the TextViews
            txtOrderId.setText(String.valueOf(orderResponse.getOrderId()));
            txtUserId.setText(String.valueOf(orderResponse.getUserId()));
            txtOrderDate.setText(orderResponse.getOrderDate());
            NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
            txtTotalPrice.setText(numberFormat.format(orderResponse.getTotalPrice()) + " VND");
            txtAddress.setText(orderResponse.getAddress());
            txtPhone.setText(orderResponse.getPhone());
            txtOrderItems.setText(getOrderItemsString(orderResponse.getOrderItems()));
        }
    }

    private void proceedToPayment(String orderId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://10.0.2.2:7177/") // Localhost for emulator
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IPaymentApi paymentApi = retrofit.create(IPaymentApi.class);
        Call<PaymentResponse> call = paymentApi.proceedPayment(orderId);

        call.enqueue(new Callback<PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String paymentUrl = response.body().getPaymentUrl();
                    // Mở URL thanh toán trong trình duyệt
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(paymentUrl));
                    startActivity(browserIntent);
                } else {
                    // Xử lý lỗi
                }
            }

            @Override
            public void onFailure(Call<PaymentResponse> call, Throwable t) {
                // Xử lý lỗi kết nối
            }
        });
    }

    private OrderResponse parseOrderJson(String orderDetailsJson) {
        return new Gson().fromJson(orderDetailsJson, OrderResponse.class);
    }

    private String getOrderItemsString(List<OrderResponse.OrderItemResponse> orderItems) {
        StringBuilder items = new StringBuilder();
        for (OrderResponse.OrderItemResponse item : orderItems) {
            items.append(item.getProductName()).append("\n");
        }
        return items.toString();
    }
}
