package com.example.fitsync.data.repository;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.fitsync.data.api.SupabaseClient;
import com.example.fitsync.data.model.Profile;
import com.example.fitsync.data.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {

    private final SessionManager session;

    public ProfileRepository(Context context) {
        this.session = new SessionManager(context);
    }

    public interface ProfileCallback {
        void onSuccess(Profile profile);
        void onError(String message);
    }

    public interface CountCallback {
        void onSuccess(int count);
        void onError(String message);
    }

    public interface ListCallback<T> {
        void onSuccess(List<T> items);
        void onError(String message);
    }

    // ── PERFIL PROPIO ──

    public void getMyProfile(ProfileCallback cb) {
        String token = session.getAccessToken();
        String userId = session.getUserId();

        if (token == null || userId == null) {
            cb.onError("No hay sesión activa");
            return;
        }

        SupabaseClient.getDbApi()
                .getProfile(
                        "Bearer " + token,
                        "eq." + userId,
                        "*"
                )
                .enqueue(new Callback<List<Profile>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Profile>> call,
                                           @NonNull Response<List<Profile>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Profile> list = response.body();
                            if (list.isEmpty()) {
                                cb.onError("Perfil no encontrado");
                            } else {
                                cb.onSuccess(list.get(0));
                            }
                        } else {
                            cb.onError("Error " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Profile>> call, @NonNull Throwable t) {
                        cb.onError("Sin conexión: " + t.getMessage());
                    }
                });
    }

    // ── SESIONES COMPLETADAS ──

    public void countMyCompletedSessions(CountCallback cb) {
        String token = session.getAccessToken();
        String userId = session.getUserId();

        if (token == null || userId == null) {
            cb.onError("No hay sesión activa");
            return;
        }

        SupabaseClient.getDbApi()
                .countCompletedSessions(
                        "Bearer " + token,
                        "eq." + userId,
                        "eq.true"
                )
                .enqueue(new Callback<List<Object>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Object>> call,
                                           @NonNull Response<List<Object>> response) {
                        if (response.isSuccessful()) {
                            String contentRange = response.headers().get("Content-Range");
                            int count = parseCount(contentRange);
                            cb.onSuccess(count);
                        } else {
                            cb.onError("Error " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Object>> call, @NonNull Throwable t) {
                        cb.onError("Sin conexión: " + t.getMessage());
                    }
                });
    }

    // ── RANKING ──

    public void getRanking(ListCallback<Profile> cb) {
        String token = session.getAccessToken();
        if (token == null) {
            cb.onError("No hay sesión activa");
            return;
        }

        SupabaseClient.getDbApi()
                .getRanking(
                        "Bearer " + token,
                        "id,username,nombre_completo,nivel,racha_dias",
                        "nivel.desc",
                        "50"
                )
                .enqueue(new Callback<List<Profile>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Profile>> call,
                                           @NonNull Response<List<Profile>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            cb.onSuccess(response.body());
                        } else {
                            cb.onError("Error " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Profile>> call,
                                          @NonNull Throwable t) {
                        cb.onError("Sin conexión: " + t.getMessage());
                    }
                });
    }

    public void getRankingAmigos(List<String> amigoIds, ListCallback<Profile> cb) {
        getRanking(new ListCallback<Profile>() {
            @Override
            public void onSuccess(List<Profile> items) {
                List<Profile> filtrados = new ArrayList<>();
                for (Profile p : items) {
                    if (amigoIds.contains(p.getId())) filtrados.add(p);
                }
                cb.onSuccess(filtrados);
            }

            @Override
            public void onError(String message) {
                cb.onError(message);
            }
        });
    }

    // ── UTILIDADES ──

    private int parseCount(String contentRange) {
        if (contentRange == null) return 0;
        int slash = contentRange.indexOf('/');
        if (slash == -1) return 0;
        String totalStr = contentRange.substring(slash + 1).trim();
        if (totalStr.equals("*")) return 0;
        try {
            return Integer.parseInt(totalStr);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}