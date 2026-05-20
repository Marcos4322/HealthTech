package com.example.fitsync.data.model;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("refresh_token")
    private String refreshToken;

    @SerializedName("expires_in")
    private long expiresIn;

    @SerializedName("token_type")
    private String tokenType;

    private User user;

    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public long getExpiresIn() { return expiresIn; }
    public String getTokenType() { return tokenType; }
    public User getUser() { return user; }

    public static class User {
        private String id;
        private String email;

        public String getId() { return id; }
        public String getEmail() { return email; }
    }
}