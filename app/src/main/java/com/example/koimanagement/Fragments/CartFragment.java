package com.example.koimanagement.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.koimanagement.Activities.UnsafeOkHttpClient;
import com.example.koimanagement.Adapters.CartAdapter;
import com.example.koimanagement.IService.ICartService;
import com.example.koimanagement.Models.CartItem;
import com.example.koimanagement.R;

import java.util.ArrayList;
import java.util.List;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // Initialize RecyclerView
        recyclerViewCart = view.findViewById(R.id.recyclerViewCart);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize cart items list
        cartItems = new ArrayList<>();

        // Set up Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://10.0.2.2:7230/")
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ICartService.class);

        // Load cart items from API
        loadCartItems(884);

        // Set up the adapter
        cartAdapter = new CartAdapter(getContext(), cartItems);
        recyclerViewCart.setAdapter(cartAdapter);

        return view;
    }

    private void loadCartItems(int userId) {
        Call<List<CartItem>> call = apiService.getCartItems(userId);
        call.enqueue(new Callback<List<CartItem>>() {
            @Override
            public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cartItems.clear();
                    cartItems.addAll(response.body());
                    cartAdapter.notifyDataSetChanged(); // Notify adapter of data changes
                } else {
                    // Log response body for debugging
                    Log.e("CartFragment", "Response unsuccessful: " + response.code() + ", " + response.message());
                    Toast.makeText(getContext(), "Failed to load cart items", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CartItem>> call, Throwable t) {
                Log.e("CartFragment", "Error loading cart items", t);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
