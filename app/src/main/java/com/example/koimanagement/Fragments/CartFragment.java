package com.example.koimanagement.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.koimanagement.Activities.UnsafeOkHttpClient;
import com.example.koimanagement.Adapters.CartAdapter;
import com.example.koimanagement.IService.ICartService;
import com.example.koimanagement.Models.CartItem;
import com.example.koimanagement.Models.Response.OrderResponse;
import com.example.koimanagement.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartFragment extends Fragment {

    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;
    private ICartService apiService;
    private TextView txtTotal;
    private Button btnCheckOut;
    int userId  = 1 ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // Initialize RecyclerView
        recyclerViewCart = view.findViewById(R.id.recyclerViewCart);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize cart items list
        cartItems = new ArrayList<>();

        // Initialize total price TextView
        txtTotal = view.findViewById(R.id.txtTotal);

        // Initialize checkout button
        btnCheckOut = view.findViewById(R.id.btnCheckOut);

        // Set up Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://10.0.2.2:7177/") // Localhost for emulator
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ICartService.class);

        // Load cart items from API
        loadCartItemsByUserID();

        // Set up the adapter
        cartAdapter = new CartAdapter(getContext(), cartItems, apiService, this); // Thêm tham chiếu đến CartFragment
        recyclerViewCart.setAdapter(cartAdapter);




        // Set click listener for checkout button
        btnCheckOut.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
            String jwtToken = sharedPreferences.getString("jwtToken", null);

            if (jwtToken != null) {
                // Decode JWT to get userId
                DecodedJWT decodedJWT = JWT.decode(jwtToken);
                String userIdString = decodedJWT.getClaim("userId").asString(); // Extract userId from JWT

                if (userIdString != null) {
                    try {
                        int userId = Integer.parseInt(userIdString); // Convert userId to integer
                        showCheckoutDialog(userId); // Pass the userId to the checkout dialog
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Invalid userId format", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "UserId not found in token", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Invalid token", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void loadCartItemsByUserID() {
        // Lấy JWT từ SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String jwtToken = sharedPreferences.getString("jwtToken", null);

        if (jwtToken != null) {
            // Giải mã JWT để lấy userID
            DecodedJWT decodedJWT = JWT.decode(jwtToken);
            String userIdString = decodedJWT.getClaim("userId").asString(); // Lấy userId dưới dạng chuỗi

            if (userIdString != null) {
                try {
                    int userId = Integer.parseInt(userIdString); // Chuyển đổi chuỗi thành int
                    loadCartItems(userId); // Gọi hàm loadCartItems với userId đã lấy được
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Invalid userId format", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Không tìm thấy userId trong token", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Token không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to load cart items
    private void loadCartItems(int userId) {
        Call<List<CartItem>> call = apiService.getCartItems(userId);
        call.enqueue(new Callback<List<CartItem>>() {
            @Override
            public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cartItems.clear();
                    cartItems.addAll(response.body());
                    cartAdapter.notifyDataSetChanged(); // Update adapter with new data
                    calculateTotalPrice();
                } else {
                    Toast.makeText(getContext(), "Failed to load cart items", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CartItem>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to calculate total price
    public void calculateTotalPrice() { // Đổi từ private thành public
        double totalPrice = 0.0;
        for (CartItem item : cartItems) {
            totalPrice += item.getPrice() * item.getQuantity();
        }
        txtTotal.setText(String.format("%.0f VND", totalPrice));
    }

    // Show AlertDialog to get address and phone from user
    private void showCheckoutDialog(int userId) {
        // Create an AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter Address and Phone");

        // Inflate custom layout with EditTexts
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_checkout, null);
        builder.setView(dialogView);

        // Get the EditTexts from the layout
        EditText editAddress = dialogView.findViewById(R.id.editAddress);
        EditText editPhone = dialogView.findViewById(R.id.editPhone);

        // Set the "Confirm" button
        builder.setPositiveButton("Confirm", (dialog, which) -> {
            String address = editAddress.getText().toString();
            String phone = editPhone.getText().toString();

            // Validate input
            if (address.isEmpty() || phone.isEmpty()) {
                Toast.makeText(getContext(), "Please enter both address and phone", Toast.LENGTH_SHORT).show();
            } else {
                // Create order if inputs are valid
                createOrder(userId, address, phone);
            }
        });

        // Set the "Cancel" button
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        builder.create().show();
    }

    // Method to create order when checkout button is clicked
    private void createOrder(int userId, String address, String phone) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("address", address);
        requestBody.put("phone", phone);

        Call<OrderResponse> call = apiService.createOrder(userId, requestBody);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    OrderResponse orderResponse = response.body();

                    // Start CheckoutSummaryActivity and pass the order details
                    Intent intent = new Intent(getContext(), CheckoutSummaryActivity.class);
                    intent.putExtra("orderDetails", new Gson().toJson(orderResponse)); // Convert order response to JSON string
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Failed to create order", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}