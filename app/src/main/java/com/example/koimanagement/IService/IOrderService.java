package com.example.koimanagement.IService;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IOrderService {

    @POST("/Order/create-order")
    Call<Void> createOrder(@Query("userID") int userId, @Body Map<String, String> requestBody);
}
