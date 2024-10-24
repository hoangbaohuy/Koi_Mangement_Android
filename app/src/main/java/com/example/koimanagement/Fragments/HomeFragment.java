package com.example.koimanagement.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.koimanagement.Activities.UnsafeOkHttpClient;
import com.example.koimanagement.Adapters.Product3Adapter;
import com.example.koimanagement.Adapters.ProductAdapter;
import com.example.koimanagement.Adapters.SlideAdapter;
import com.example.koimanagement.IService.IProductApiService;
import com.example.koimanagement.Models.Product;
import com.example.koimanagement.Models.SlideItem;
import com.example.koimanagement.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeFragment extends Fragment {


    ViewPager2 viewPager2;
    private RecyclerView recyclerViewProducts;
    private Product3Adapter product3Adapter;

    // Singleton Retrofit instance
    private static Retrofit retrofitInstance;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerViewProducts = view.findViewById(R.id.recyclerViewProducts); // Initialize RecyclerView
        // Setup RecyclerView with a horizontal layout manager
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        viewPager2 = view.findViewById(R.id.viewPager);
        List<SlideItem> slideItems = new ArrayList<>();
        slideItems.add(new SlideItem(R.drawable.slide1));
        slideItems.add(new SlideItem(R.drawable.slide4));
        viewPager2.setAdapter(new SlideAdapter(slideItems,viewPager2));


        // Fetch products from API
        fetchProducts();
        return view;
    }


    private void fetchProducts() {
        if (retrofitInstance == null) {
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl("https://10.0.2.2:7177/") // Adjust this for local testing
                    .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        IProductApiService productApi = retrofitInstance.create(IProductApiService.class);
        Call<List<Product>> call = productApi.getAllProducts();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Product> productList = response.body();
                if (productList != null && !productList.isEmpty()) {
                    product3Adapter = new Product3Adapter(getContext(), productList);
                    recyclerViewProducts.setAdapter(product3Adapter);
                } else {
                    Toast.makeText(getContext(), "No products found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("ProductFragment", "onFailure: " + t.getMessage());
                Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}