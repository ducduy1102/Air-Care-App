package com.example.aircareapp.AccessAPI;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.Date;

public class TokenManager {

    private final RequestQueue queue;
    private final String refreshToken;
    private String accessToken;
    private Date expirationTime;

    private TokenManager(Context context, String refreshToken) {
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
