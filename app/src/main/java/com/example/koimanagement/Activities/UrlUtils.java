package com.example.koimanagement.Activities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UrlUtils {
    // Phương thức static để mã hóa URL
    public static String encode(String value) {
        try {
            // Mã hóa với UTF-8
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // Xử lý ngoại lệ nếu cần
            e.printStackTrace();
            return value; // Hoặc có thể trả về một chuỗi mặc định nào đó
        }
    }
}