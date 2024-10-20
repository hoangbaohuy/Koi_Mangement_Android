package com.example.koimanagement.Models.Request;

public class AddToCartRequest {
    private int userId;
    private int productId;
    private int quantity;

    public AddToCartRequest(int userId, int productId, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }
}
