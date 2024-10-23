package com.example.koimanagement.Models;

public class CartItem {
    private int cartId; // Thêm trường cartId
    private int productId; // ID sản phẩm
    private String productName; // Tên sản phẩm
    private int userId; // ID người dùng
    private int quantity; // Số lượng sản phẩm trong giỏ
    private int price; // Giá sản phẩm
    private boolean status;
    private String image ;// Trạng thái (có thể được sử dụng để xác định sản phẩm có còn trong giỏ hay không)

    // Constructor
    public CartItem(int cartId, int productId, String productName, int userId, int quantity, int price, boolean status , String image) {
        this.cartId = cartId; // Khởi tạo cartId
        this.productId = productId;
        this.productName = productName;
        this.userId = userId; // Khởi tạo userId
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.image = image;// Khởi tạo trạng thái
    }

    // Getters và Setters
    public int getCartId() {
        return cartId; // Getter cho cartId
    }

    public void setCartId(int cartId) {
        this.cartId = cartId; // Setter cho cartId
    }

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

    public int getUserId() {
        return userId; // Getter cho userId
    }

    public void setUserId(int userId) {
        this.userId = userId; // Setter cho userId
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isStatus() { // Getter cho trạng thái
        return status;
    }

    public void setStatus(boolean status) { // Setter cho trạng thái
        this.status = status;
    }
    public String getImage() {
        return image; // Getter for image
    }

    public void setImage(String image) {
        this.image = image; // Setter for image
    }
}
