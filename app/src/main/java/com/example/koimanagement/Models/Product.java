package com.example.koimanagement.Models;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
public class Product implements Serializable {
    @SerializedName("productId")
    public int ProductId;

    @SerializedName("productName")
    public String ProductName;

    @SerializedName("productDescription")
    public String ProductDescription;

    @SerializedName("price")
    public double Price;

    @SerializedName("stockQuantity")
    public int StockQuantity;
    public Product() {}
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
