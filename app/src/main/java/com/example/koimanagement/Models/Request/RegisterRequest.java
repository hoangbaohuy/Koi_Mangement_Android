package com.example.koimanagement.Models.Request;

public class RegisterRequest {
    private String userName;
    private String passwordHash;
    private String email;

    public RegisterRequest(String userName, String email, String passwordHash) {
        this.userName = userName;
        this.email = email;
        this.passwordHash = passwordHash;
    }
}
