package com.example.koimanagement.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Callback;

import androidx.appcompat.app.AppCompatActivity;

import com.example.koimanagement.Models.Request.RegisterRequest;
import com.example.koimanagement.Models.Response.RegisterResponse;
import com.example.koimanagement.R;

import com.example.koimanagement.IService.IAuthApiService;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity  extends AppCompatActivity {
    private EditText edName , edEmail ,edPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edName = findViewById(R.id.edName);
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        findViewById(R.id.btnSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }
    private void registerUser(){
        String name = edName.getText().toString();
        String email = edEmail.getText().toString();
        String password = edPassword.getText().toString();


        RegisterRequest request = new RegisterRequest( name ,email,password);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://localhost:7230/Authen/Register")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IAuthApiService apiService = retrofit.create(IAuthApiService.class);

        Call<RegisterResponse> call = apiService.register(request);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}


