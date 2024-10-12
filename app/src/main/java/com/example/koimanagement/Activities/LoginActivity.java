package com.example.koimanagement.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.koimanagement.IService.IAuthApiService;
import com.example.koimanagement.Models.Request.LoginRequest;
import com.example.koimanagement.Models.Response.RegisterResponse;
import com.example.koimanagement.Models.Response.ResponseEntity;
import com.example.koimanagement.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private EditText edEmail ,edPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }
    private void loginUser(){
        String email = edEmail.getText().toString();
        String password = edPassword.getText().toString();
        LoginRequest request = new LoginRequest(email,password);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://10.0.2.2:7230/")
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IAuthApiService apiService = retrofit.create(IAuthApiService.class);
        Call<ResponseEntity> call = apiService.login(request);
        call.enqueue(new Callback<ResponseEntity>() {
            @Override
            public void onResponse(Call<ResponseEntity> call, Response<ResponseEntity> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, IntroductionActivity.class);
                    startActivity(intent);
                    finish();
                }  else {
                    try {
                        String errorBody = response.errorBody().string();
                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại: " + errorBody, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại: Không thể lấy thông báo lỗi", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            @Override
            public void onFailure(Call<ResponseEntity> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}