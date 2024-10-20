package com.example.koimanagement.Models;

public class CartItem {
    private int productId; // Product ID
    private String productName; // Product name
    private String productDescription; // Product description
    private int price; // Price of the product
    private int stockQuantity; // Quantity in stock
    private String image;// Change to String for image URL

    public CartItem(int productId, String productName, String productDescription, int price, int stockQuantity, String image) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.image = image;
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
