package com.example.koimanagement.Fragments;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.koimanagement.Models.Response.OrderResponse;
import com.example.koimanagement.R;
import com.google.gson.Gson;
import java.util.List;

public class CheckoutSummaryActivity extends AppCompatActivity {

    private TextView txtOrderId, txtUserId, txtOrderDate, txtTotalPrice, txtAddress, txtPhone, txtOrderItems;
    private ImageButton btnBack; // Declare your back button

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
        btnBack.setOnClickListener(v -> finish()); // Close the current activity

        // Get the order details from the intent
        if (getIntent().hasExtra("orderDetails")) {
            String orderDetailsJson = getIntent().getStringExtra("orderDetails");
            OrderResponse orderResponse = parseOrderJson(orderDetailsJson);

            // Set the TextViews
            txtOrderId.setText(String.valueOf(orderResponse.getOrderId()));
            txtUserId.setText(String.valueOf(orderResponse.getUserId()));
            txtOrderDate.setText(orderResponse.getOrderDate());
            txtTotalPrice.setText(String.format("%.0f VND", orderResponse.getTotalPrice()));
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
