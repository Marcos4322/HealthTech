package com.example.fitsync.data.repository;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.fitsync.data.api.SupabaseClient;
import com.example.fitsync.data.model.AuthError;
import com.example.fitsync.data.model.AuthRequest;
import com.example.fitsync.data.model.AuthResponse;
import com.example.fitsync.data.session.SessionManager;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private final SessionManager session;

    public AuthRepository(Context context) {
        this.session = new SessionManager(context);
    }

    public interface AuthCallback {
        void onSuccess();
        void onError(String message);
    }

    public void signUp(String email, String password, AuthCallback cb) {
        SupabaseClient.getAuthApi()
                .signUp(new AuthRequest(email, password))
                .enqueue(authCallback(cb));
    }

    public void signIn(String email, String password, AuthCallback cb) {
        SupabaseClient.getAuthApi()
                .signIn(new AuthRequest(email, password))
                .enqueue(authCallback(cb));
    }

    private Callback<AuthResponse> authCallback(AuthCallback cb) {
        return new Callback<AuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call,
                                   @NonNull Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse body = response.body();
                    if (body.getAccessToken() != null) {
                        session.saveSession(
                                body.getAccessToken(),
                                body.getRefreshToken(),
                                body.getUser() != null ? body.getUser().getId() : null,
                                body.getUser() != null ? body.getUser().getEmail() : null
                        );
                        cb.onSuccess();
                    } else {
                        cb.onError("Revisa tu correo para confirmar la cuenta");
                    }
                } else {
                    cb.onError(parseError(response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                cb.onError("Sin conexión: " + t.getMessage());
            }
        };
    }

    private String parseError(Response<?> response) {
        try {
            if (response.errorBody() != null) {
                String json = response.errorBody().string();
                AuthError err = new Gson().fromJson(json, AuthError.class);
                return translate(err.getMessage());
            }
        } catch (Exception ignored) {}
        return "Error " + response.code();
    }

    private String translate(String msg) {
        if (msg == null) return "Error desconocido";
        String m = msg.toLowerCase();
        if (m.contains("invalid login") || m.contains("invalid credentials"))
            return "Email o contraseña incorrectos";
        if (m.contains("already registered") || m.contains("already been registered") || m.contains("user already"))
            return "Ese email ya está registrado";
        if (m.contains("password should be at least"))
            return "La contraseña debe tener al menos 6 caracteres";
        if (m.contains("unable to validate email") || m.contains("invalid email"))
            return "Email no válido";
        if (m.contains("email not confirmed"))
            return "Debes confirmar tu email antes de iniciar sesión";
        return msg;
    }
}