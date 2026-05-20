package com.example.fitsync.data.api;

import com.example.fitsync.data.model.AuthRequest;
import com.example.fitsync.data.model.AuthResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SupabaseAuthApi {

    @POST("auth/v1/signup")
    Call<AuthResponse> signUp(@Body AuthRequest request);

    @POST("auth/v1/token?grant_type=password")
    Call<AuthResponse> signIn(@Body AuthRequest request);
}