package com.example.koimanagement.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.koimanagement.Activities.UnsafeOkHttpClient;
import com.example.koimanagement.Adapters.ProductAdapter;
import com.example.koimanagement.IService.IProductApiService;
import com.example.koimanagement.Models.Product;
import com.example.koimanagement.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends Fragment {
    RecyclerView recyclerViewProducts;
    ProductAdapter productAdapter;
    int[] productImages = {R.drawable.replace, R.drawable.replace};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        // Initialize RecyclerView
        recyclerViewProducts = view.findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Fetch products from API
        fetchProducts();

        return view;
    }
    private void fetchProducts() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://10.0.2.2:7230/")
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IProductApiService productApi = retrofit.create(IProductApiService.class);
        Call<List<Product>> call = productApi.getAllProducts();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                // Log the raw response
                Log.d("ProductFragment", "Response: " + response.body());

                List<Product> productList = response.body();
                if (productList != null) {
                    productAdapter = new ProductAdapter(getContext(), productList, productImages);
                    recyclerViewProducts.setAdapter(productAdapter);
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
