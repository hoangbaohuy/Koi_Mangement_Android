package com.example.koimanagement.Activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class MyCookieJar implements CookieJar {
    private final List<Cookie> cookies = new ArrayList<>();

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            this.cookies.add(cookie); // Lưu cookies từ response
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return cookies; // Trả về cookies đã lưu cho request
    }
}
