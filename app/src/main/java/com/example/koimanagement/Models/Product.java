package com.example.koimanagement.Models;

import java.io.Serializable;

public class Product implements Serializable {
    public int ProductId;
    public String ProductName;
    public String ProductDescription;
    public double Price;
    public int StockQuantity;
    // List OrderItem OrderItems

    public Product(int productId, String productName, String productDescription, double price, int stockQuantity) {
        ProductId = productId;
        ProductName = productName;
        ProductDescription = productDescription;
        Price = price;
        StockQuantity = stockQuantity;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductDescription() {
        return ProductDescription;
    }

    public void setProductDescription(String productDescription) {
        ProductDescription = productDescription;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public int getStockQuantity() {
        return StockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        StockQuantity = stockQuantity;
    }
}
