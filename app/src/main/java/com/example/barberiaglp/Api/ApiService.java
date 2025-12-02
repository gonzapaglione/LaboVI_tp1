package com.example.barberiaglp.Network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("auth/login")
    Call<AuthResponse> login(@Body LoginRequest loginRequest);

    @POST("auth/register")
    Call<AuthResponse> register(@Body RegisterRequest registerRequest);

    @GET("users/{id}")
    Call<AuthResponse> getUserById(@Path("id") int userId);

    @PUT("users/{id}")
    Call<AuthResponse> updateUser(@Path("id") int userId, @Body UpdateUserRequest updateRequest);
}
