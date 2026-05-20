package com.example.fitsync.data.model;

import com.google.gson.annotations.SerializedName;

public class AuthError {

    @SerializedName("error_code")
    private String errorCode;

    @SerializedName("msg")
    private String msg;

    private String error;

    @SerializedName("error_description")
    private String errorDescription;

    public String getMessage() {
        if (msg != null) return msg;
        if (errorDescription != null) return errorDescription;
        if (error != null) return error;
        return "Error desconocido";
    }

    public String getErrorCode() { return errorCode; }
}