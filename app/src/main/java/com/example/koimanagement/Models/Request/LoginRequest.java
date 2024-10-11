package com.example.koimanagement.Models.Request;

public class LoginRequest {
    private String email;
    private String passwordHash;
    public LoginRequest(String email,String passwordHash ){
        this.email = email;
        this.passwordHash = passwordHash;
    }
}
