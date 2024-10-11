package com.example.koimanagement.IService;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import com.example.koimanagement.Models.Request.LoginRequest;
import com.example.koimanagement.Models.Request.RegisterRequest;
import com.example.koimanagement.Models.Response.RegisterResponse;
import com.example.koimanagement.Models.Response.ResponseEntity;

public interface  IAuthApiService {
    @POST("Authen/Register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @POST("Authen/Login")
    Call<ResponseEntity> login(@Body LoginRequest request);
}
