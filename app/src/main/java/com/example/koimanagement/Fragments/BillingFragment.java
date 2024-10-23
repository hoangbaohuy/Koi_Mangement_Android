/*
package com.example.koimanagement.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.koimanagement.Models.Response.OrderResponse;
import com.example.koimanagement.R;

public class BillingFragment extends Fragment {

    private TextView txtOrderId, txtOrderDate, txtTotalPrice, txtAddress, txtPhone;
    private LinearLayout layoutOrderItems;
    private Button btnBackToCart;
    private OrderResponse orderResponse;

    // No-argument constructor required for Fragment
    public BillingFragment() {
        // Required empty public constructor
    }

    // Method to set the OrderResponse
    public void setOrderResponse(OrderResponse orderResponse) {
        this.orderResponse = orderResponse;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_billing, container, false);

        // Initialize views
        txtOrderId = view.findViewById(R.id.txtOrderId);
        txtOrderDate = view.findViewById(R.id.txtOrderDate);
        txtTotalPrice = view.findViewById(R.id.txtTotalPrice);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtPhone = view.findViewById(R.id.txtPhone);
        layoutOrderItems = view.findViewById(R.id.layoutOrderItems);
        btnBackToCart = view.findViewById(R.id.btnBackToCart);

        // Set order details
        if (orderResponse != null) {
            txtOrderId.setText("Order ID: " + orderResponse.getOrderId());
            txtOrderDate.setText("Order Date: " + (orderResponse.getOrderDate() != null ? orderResponse.getOrderDate().toString() : "N/A"));
            txtTotalPrice.setText("Total Price: " + (orderResponse.getTotalPrice() != null ? orderResponse.getTotalPrice() + " VND" : "N/A"));
            txtAddress.setText("Address: " + orderResponse.getAddress());
            txtPhone.setText("Phone: " + orderResponse.getPhone());

            // Add order items dynamically
            if (orderResponse.getOrderItems() != null) {
                for (OrderResponse.OrderItemResponse item : orderResponse.getOrderItems()) {
                    TextView itemView = new TextView(getContext());
                    itemView.setText(item.getProductName());
                    layoutOrderItems.addView(itemView);
                }
            }
        }

        // Set button click listener
        btnBackToCart.setOnClickListener(v -> {
            // Handle back to cart action, e.g., navigate back to the cart fragment
        });

        return view;
    }
}
*/
