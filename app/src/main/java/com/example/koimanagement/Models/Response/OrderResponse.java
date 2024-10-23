package com.example.koimanagement.Models.Response;

import java.util.Date;
import java.util.List;

public class OrderResponse {
    private int orderId; // Assuming orderId is required and can't be null
    private Integer userId; // Using Integer to allow null value
    private String  orderDate; // Date can be nullable in C#
    private Double totalPrice; // Using Double for decimal representation
    private String address;
    private String phone;
    private List<OrderItemResponse> orderItems; // Renamed for consistency

    // Nested class for OrderItem
    public static class OrderItemResponse {
        private String productName;
        private String image;

        public String getProductName() {
            return productName;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public OrderItemResponse(String productName) {
            this.productName = productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemResponse> orderItems) {
        this.orderItems = orderItems;
    }
}