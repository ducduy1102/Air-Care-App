package com.example.aircareapp.AccessAPI;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.Date;

public class TokenManager {
//    private final RequestQueue queue;
//    private final String refreshToken;
//
//    public TokenManager(RequestQueue queue, String refreshToken) {
//        this.queue = queue;
//        this.refreshToken = refreshToken;
//    }
//
//    public Token getToken() {
//        return new Token(
//                token,
//                expiration
//        );
//    }
//
//    public void refreshToken() {
//        // Make a request to refresh the token
//        StringRequest request = new StringRequest(
//                Request.Method.POST,
//                "https://api.example.com/oauth/token",
//                this::onResponse,
//                this::onError
//        );
//
//        request.setHeader("Authorization", "Bearer " + refreshToken);
//
//        queue.add(request);
//    }
//
//    private void onResponse(String response) {
//        // Parse the response
//        TokenResponse responseObject = new Gson().fromJson(response, TokenResponse.class);
//
//        // Update the token
//        token = responseObject.access_token;
//        expiration = new Date(System.currentTimeMillis() + responseObject.expires_in * 1000);
//    }
//
//    private void onError(VolleyError error) {
//        // Handle an error
//    }
//
//    private static class Token {
//
//        private String token;
//        private Date expiration;
//
//        public Token(String token, Date expiration) {
//            this.token = token;
//            this.expiration = expiration;
//        }
//    }
//
//    private static class TokenResponse {
//
//        private String access_token;
//        private int expires_in;
//
//        public TokenResponse(String access_token, int expires_in) {
//            this.access_token = access_token;
//            this.expires_in = expires_in;
//        }
//    }

//    private final RequestQueue queue;
//    private final String refreshToken;
//    private String token;
//    private long expirationTime;
//    private String requestUrl;
//
//    public interface TokenRefreshCallback {
//        void onTokenRefreshed();
//        void onTokenRefreshFailed();
//    }
//
//    public TokenManager(RequestQueue queue, String refreshToken) {
//        this.queue = queue;
//        this.refreshToken = refreshToken;
//    }
//
//    public void makeRequest(String url, TokenRefreshCallback callback) {
//        this.requestUrl = url;
//        if (isTokenExpired()) {
//            // Token đã hết hạn, làm mới trước khi thực hiện yêu cầu
//            refreshToken(callback);
//        } else {
//            // Token còn hiệu lực, thực hiện yêu cầu ngay
//            performRequest(url, callback);
//        }
//    }
//
//    private boolean isTokenExpired() {
//        // Kiểm tra xem token có hết hạn chưa
//        return System.currentTimeMillis() >= expirationTime;
//    }
//
//    private void refreshToken(String url, TokenRefreshCallback callback) {
//        StringRequest request = new StringRequest(
//                Request.Method.POST,
//                "https://uiot.ixxc.dev/auth/realms/master/protocol/openid-connect/token",
//                response -> {
//                    // Xử lý phản hồi và cập nhật token mới
//                    TokenResponse responseObject = new Gson().fromJson(response, TokenResponse.class);
//                    token = responseObject.getAccess_token();
//                    expirationTime = System.currentTimeMillis() + responseObject.getExpires_in() * 1000;
//
//                    // Gọi callback khi làm mới thành công
//                    callback.onTokenRefreshed();
//
//                    // Tiếp tục thực hiện yêu cầu ban đầu
//                    performRequest(url, callback);
//                },
//                error -> {
//                    // Xử lý lỗi khi làm mới token
//                    callback.onTokenRefreshFailed();
//                }
//        ) {
//            @Override
//            public byte[] getBody() {
//                return ("grant_type=refresh_token&refresh_token=" + refreshToken).getBytes();
//            }
//
//            @Override
//            public String getBodyContentType() {
//                return "application/x-www-form-urlencoded";
//            }
//        };
//
//        queue.add(request);
//    }
//
//    private void performRequest(String url, TokenRefreshCallback callback) {
//        // Thực hiện yêu cầu sử dụng token hiện tại
//        // ...
//    }


    private final RequestQueue queue;
    private final String refreshToken;
    private String accessToken;
    private Date expirationTime;

    private TokenManager(Context context, String refreshToken) {
        // Sử dụng ApplicationContext để tránh memory leak
        queue = Volley.newRequestQueue(context.getApplicationContext());
        this.refreshToken = refreshToken;
    }
    public void makeRequest(String url, final TokenRequestCallback callback) {
        if (isTokenExpired()) {
            refreshToken(callback);
        } else {
            performRequest(url, callback);
        }
    }

    private boolean isTokenExpired() {
        return expirationTime == null || expirationTime.before(new Date());
    }

    private void refreshToken(final TokenRequestCallback callback) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                "https://uiot.ixxc.dev/auth/realms/master/protocol/openid-connect/token",
                response -> {
                    TokenResponse responseObject = new Gson().fromJson(response, TokenResponse.class);
                    updateToken(responseObject.getAccess_token(), responseObject.getExpires_in());
                    callback.onRequestCompleted(accessToken);
                },
                error -> {
                    callback.onRequestFailed(error);
                }
        ) {
            @Override
            public byte[] getBody() {
                // Include grant_type and any other necessary parameters for token refresh
                return ("grant_type=refresh_token&refresh_token=" + refreshToken.getBytes()).getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };

        queue.add(request);
    }

    private void performRequest(String url, final TokenRequestCallback callback) {
        // Thực hiện yêu cầu sử dụng token hiện tại
        // ...

        // Sau khi hoàn thành yêu cầu, gọi callback với token hiện tại
        callback.onRequestCompleted(accessToken);
    }

    private void updateToken(String newAccessToken, int expiresIn) {
        accessToken = newAccessToken;
        expirationTime = new Date(System.currentTimeMillis() + expiresIn * 1000);
    }

    public interface TokenRequestCallback {
        void onRequestCompleted(String accessToken);
        void onRequestFailed(Exception error);
    }
}
