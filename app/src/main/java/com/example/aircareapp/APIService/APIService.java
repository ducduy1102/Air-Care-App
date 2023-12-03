package com.example.aircareapp.APIService;

import com.example.aircareapp.Model.LoginResponse;
import com.example.aircareapp.Model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIService {
    @FormUrlEncoded
    @POST("token") // Replace "login" with your API endpoint
    Call<LoginResponse> login(
            @Field("client_id") String client_id,
            @Field("username") String username,
            @Field("password") String password,
            @Field("grant_type") String grant_type
    );
    @GET("user")
    Call<User> getUser();//, @Header("Authorization") String auth);
}
