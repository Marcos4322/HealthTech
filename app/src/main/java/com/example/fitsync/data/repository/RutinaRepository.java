package com.example.fitsync.data.repository;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.fitsync.data.api.SupabaseClient;
import com.example.fitsync.data.model.RoutineGeneratedResponse;
import com.example.fitsync.data.model.RoutineRequest;
import com.example.fitsync.data.model.Rutina;
import com.example.fitsync.data.model.RutinaEjercicio;
import com.example.fitsync.data.session.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RutinaRepository {

    private final SessionManager session;

    public RutinaRepository(Context context) {
        this.session = new SessionManager(context);
    }

    public interface GenerateCallback {
        void onSuccess(RoutineGeneratedResponse response);
        void onError(String message);
    }

    public interface RutinaCallback {
        void onSuccess(Rutina rutina);
        void onError(String message);
    }

    public interface EjerciciosCallback {
        void onSuccess(List<RutinaEjercicio> ejercicios);
        void onError(String message);
    }

    /**
     * Llama a la Edge Function para generar una rutina con IA.
     * Tarda 10-25 segundos típicamente.
     */
    public void generateRoutineWithAI(RoutineRequest request, GenerateCallback cb) {
        String token = session.getAccessToken();
        if (token == null) {
            cb.onError("No hay sesión activa");
            return;
        }

        SupabaseClient.getFunctionsApi()
                .generateRoutine("Bearer " + token, request)
                .enqueue(new Callback<RoutineGeneratedResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<RoutineGeneratedResponse> call,
                                           @NonNull Response<RoutineGeneratedResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            RoutineGeneratedResponse body = response.body();
                            if (body.getError() != null) {
                                cb.onError(body.getError());
                            } else if (body.getRutinaId() != null) {
                                cb.onSuccess(body);
                            } else {
                                cb.onError("Respuesta inesperada del servidor");
                            }
                        } else {
                            cb.onError("Error " + response.code() + " generando rutina");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<RoutineGeneratedResponse> call, @NonNull Throwable t) {
                        cb.onError("Error de conexión: " + t.getMessage());
                    }
                });
    }

    public void getRutinaById(String rutinaId, RutinaCallback cb) {
        String token = session.getAccessToken();
        if (token == null) {
            cb.onError("No hay sesión activa");
            return;
        }

        SupabaseClient.getDbApi()
                .getRutina("Bearer " + token, "eq." + rutinaId, "*")
                .enqueue(new Callback<List<Rutina>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Rutina>> call,
                                           @NonNull Response<List<Rutina>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Rutina> list = response.body();
                            if (list.isEmpty()) {
                                cb.onError("Rutina no encontrada");
                            } else {
                                cb.onSuccess(list.get(0));
                            }
                        } else {
                            cb.onError("Error " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Rutina>> call, @NonNull Throwable t) {
                        cb.onError("Sin conexión: " + t.getMessage());
                    }
                });
    }

    public void getRutinaEjercicios(String rutinaId, EjerciciosCallback cb) {
        String token = session.getAccessToken();
        if (token == null) {
            cb.onError("No hay sesión activa");
            return;
        }

        SupabaseClient.getDbApi()
                .getRutinaEjercicios(
                        "Bearer " + token,
                        "eq." + rutinaId,
                        "*,ejercicios(*)",
                        "orden.asc"
                )
                .enqueue(new Callback<List<RutinaEjercicio>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<RutinaEjercicio>> call,
                                           @NonNull Response<List<RutinaEjercicio>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            cb.onSuccess(response.body());
                        } else {
                            cb.onError("Error " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<RutinaEjercicio>> call, @NonNull Throwable t) {
                        cb.onError("Sin conexión: " + t.getMessage());
                    }
                });
    }
}