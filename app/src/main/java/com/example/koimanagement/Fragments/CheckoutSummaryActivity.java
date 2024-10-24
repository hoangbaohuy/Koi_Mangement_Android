package com.example.koimanagement.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.koimanagement.Activities.CustomerActivity;
import com.example.koimanagement.Activities.OrderActivity;
import com.example.koimanagement.Models.Response.OrderResponse;
import com.example.koimanagement.R;
import com.google.gson.Gson;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CheckoutSummaryActivity extends AppCompatActivity {

    private TextView txtOrderId, txtUserId, txtOrderDate, txtTotalPrice, txtAddress, txtPhone, txtOrderItems;
    private ImageButton btnBack; // Declare your back button
    private Button btnViewDetails;
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

        // Initialize back button
        btnBack = findViewById(R.id.btnBack);
        btnViewDetails = findViewById(R.id.btnViewDetails);
        btnBack.setOnClickListener(v -> finish()); // Close the current activity
        btnViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the new activity
                Intent intent = new Intent(CheckoutSummaryActivity.this, OrderActivity.class);
                // Start the new activity
                startActivity(intent);
            }
        });
        // Get the order details from the intent
        if (getIntent().hasExtra("orderDetails")) {
            String orderDetailsJson = getIntent().getStringExtra("orderDetails");
            OrderResponse orderResponse = parseOrderJson(orderDetailsJson);

            // Set the TextViews
            txtOrderId.setText(String.valueOf(orderResponse.getOrderId()));
            txtUserId.setText(String.valueOf(orderResponse.getUserId()));
            txtOrderDate.setText(orderResponse.getOrderDate());
            NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN")); // Định dạng theo ngôn ngữ và vùng miền Việt Nam
            txtTotalPrice.setText(numberFormat.format(orderResponse.getTotalPrice())+ " VND");
            txtAddress.setText(orderResponse.getAddress());
            txtPhone.setText(orderResponse.getPhone());
            txtOrderItems.setText(getOrderItemsString(orderResponse.getOrderItems()));
        }
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
