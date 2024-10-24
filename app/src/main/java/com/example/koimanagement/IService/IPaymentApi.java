package com.example.koimanagement.IService;


import com.example.koimanagement.Models.Response.PaymentResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IPaymentApi {
    @POST("api/VnPay/proceed-vnpay-payment")
    Call<PaymentResponse> proceedPayment(@Body String orderId);
}