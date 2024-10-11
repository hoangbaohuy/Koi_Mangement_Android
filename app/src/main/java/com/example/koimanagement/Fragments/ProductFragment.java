package com.example.koimanagement.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.koimanagement.Adapters.ProductAdapter;
import com.example.koimanagement.Models.Product;
import com.example.koimanagement.R;

import java.util.ArrayList;
import java.util.List;


public class ProductFragment extends Fragment {
    RecyclerView recyclerViewProducts;
    ProductAdapter productAdapter;
    List<Product> productList;
    int[] productImages = {R.drawable.replace, R.drawable.replace};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productList = new ArrayList<>();
        productList.add(new Product(1, "Product 1", "Description 1", 29.99, 10));
        productList.add(new Product(2, "Product 2", "Description 2", 49.99, 5));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        // Initialize RecyclerView
        recyclerViewProducts = view.findViewById(R.id.recyclerViewProducts);

        // Set LayoutManager for RecyclerView
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Initialize Adapter and set it to RecyclerView
        productAdapter = new ProductAdapter(getContext(), productList, productImages);
        recyclerViewProducts.setAdapter(productAdapter);

        return view;
    }
}