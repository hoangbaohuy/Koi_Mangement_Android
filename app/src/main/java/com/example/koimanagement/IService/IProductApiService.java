package com.example.koimanagement.IService;

import com.example.koimanagement.Models.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IProductApiService {
    @GET("Product/get-all-product")
    Call<List<Product>> getAllProducts();
}