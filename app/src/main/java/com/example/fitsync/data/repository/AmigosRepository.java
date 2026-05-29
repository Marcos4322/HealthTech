package com.example.fitsync.data.repository;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.fitsync.data.api.SupabaseClient;
import com.example.fitsync.data.model.Amigo;
import com.example.fitsync.data.model.AmistadInsert;
import com.example.fitsync.data.model.AmistadUpdate;
import com.example.fitsync.data.model.SolicitudPendiente;
import com.example.fitsync.data.model.UsuarioBuscado;
import com.example.fitsync.data.session.SessionManager;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AmigosRepository {

    private final SessionManager session;

    public AmigosRepository(Context context) {
        this.session = new SessionManager(context);
    }

    public interface ListCallback<T> {
        void onSuccess(List<T> items);
        void onError(String message);
    }

    public interface ActionCallback {
        void onSuccess();
        void onError(String message);
    }

    public void buscarUsuarios(String termino, ListCallback<UsuarioBuscado> cb) {
        String token = session.getAccessToken();
        if (token == null) { cb.onError("No hay sesión"); return; }

        Map<String, String> params = new HashMap<>();
        params.put("termino", termino);

        SupabaseClient.getDbApi()
                .buscarUsuarios("Bearer " + token, params)
                .enqueue(new Callback<List<UsuarioBuscado>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<UsuarioBuscado>> call,
                                           @NonNull Response<List<UsuarioBuscado>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            cb.onSuccess(response.body());
                        } else {
                            cb.onError("Error " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<UsuarioBuscado>> call, @NonNull Throwable t) {
                        cb.onError("Sin conexión: " + t.getMessage());
                    }
                });
    }

    public void misSolicitudesPendientes(ListCallback<SolicitudPendiente> cb) {
        String token = session.getAccessToken();
        if (token == null) { cb.onError("No hay sesión"); return; }

        SupabaseClient.getDbApi()
                .misSolicitudesPendientes("Bearer " + token)
                .enqueue(new Callback<List<SolicitudPendiente>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<SolicitudPendiente>> call,
                                           @NonNull Response<List<SolicitudPendiente>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            cb.onSuccess(response.body());
                        } else {
                            cb.onError("Error " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<SolicitudPendiente>> call, @NonNull Throwable t) {
                        cb.onError("Sin conexión: " + t.getMessage());
                    }
                });
    }

    public void misAmigos(ListCallback<Amigo> cb) {
        String token = session.getAccessToken();
        if (token == null) { cb.onError("No hay sesión"); return; }

        SupabaseClient.getDbApi()
                .misAmigos("Bearer " + token)
                .enqueue(new Callback<List<Amigo>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Amigo>> call,
                                           @NonNull Response<List<Amigo>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            cb.onSuccess(response.body());
                        } else {
                            cb.onError("Error " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Amigo>> call, @NonNull Throwable t) {
                        cb.onError("Sin conexión: " + t.getMessage());
                    }
                });
    }

    public void enviarSolicitud(String destinatarioId, ActionCallback cb) {
        String token = session.getAccessToken();
        String userId = session.getUserId();
        if (token == null || userId == null) { cb.onError("No hay sesión"); return; }

        SupabaseClient.getDbApi()
                .enviarSolicitud("Bearer " + token, new AmistadInsert(userId, destinatarioId))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            cb.onSuccess();
                        } else {
                            cb.onError("Error " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        cb.onError("Sin conexión: " + t.getMessage());
                    }
                });
    }

    public void aceptarSolicitud(String amistadId, ActionCallback cb) {
        responderSolicitud(amistadId, "aceptada", cb);
    }

    public void rechazarSolicitud(String amistadId, ActionCallback cb) {
        responderSolicitud(amistadId, "rechazada", cb);
    }

    private void responderSolicitud(String amistadId, String estado, ActionCallback cb) {
        String token = session.getAccessToken();
        if (token == null) { cb.onError("No hay sesión"); return; }

        SupabaseClient.getDbApi()
                .actualizarAmistad("Bearer " + token, "eq." + amistadId, new AmistadUpdate(estado))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            cb.onSuccess();
                        } else {
                            cb.onError("Error " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        cb.onError("Sin conexión: " + t.getMessage());
                    }
                });
    }
}