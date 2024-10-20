package com.example.koimanagement.IService;

import okhttp3.RequestBody;
import retrofit2.Call;

import com.example.koimanagement.Models.CartItem;
import com.example.koimanagement.Models.Request.AddToCartRequest;
import com.example.koimanagement.Models.Response.AddToCartResponse;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ICartService {
    @POST("api/Cart/add-to-cart")
    Call<AddToCartResponse> addToCart(@Url String url, @Body RequestBody body);

    @GET("api/Cart/get-cart")
    Call<List<CartItem>> getCartItems(@Query("userId") int userId);
}
