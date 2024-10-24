package com.example.koimanagement.IService;

import okhttp3.RequestBody;
import retrofit2.Call;

import com.example.koimanagement.Models.CartItem;
import com.example.koimanagement.Models.Request.AddToCartRequest;
import com.example.koimanagement.Models.Response.AddToCartResponse;
import com.example.koimanagement.Models.Response.OrderResponse;

import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ICartService {
    @POST("api/Cart/add-to-cart")
    Call<AddToCartResponse> addToCart(@Url String url, @Body RequestBody body);

    @GET("Cart/get-cart")
    Call<List<CartItem>> getCartItems(@Query("userId") int userId);


        @DELETE("Cart/remove-cartID")
        Call<Boolean> removeCartItem(@Query("cartId") int cartId);

    @POST("/Order/create-order")
    Call<OrderResponse> createOrder(@Query("userID") int userId, @Body Map<String, String> requestBody);

    @PUT("Cart/updatequantity")
    Call<Boolean> updateCartItemQuantity(@Query("cartId") int cartId, @Query("quantity") int quantity);
}
