package com.example.fitsync.data.api;

import com.example.fitsync.data.model.Amigo;
import com.example.fitsync.data.model.AmistadInsert;
import com.example.fitsync.data.model.AmistadUpdate;
import com.example.fitsync.data.model.SolicitudPendiente;
import com.example.fitsync.data.model.UsuarioBuscado;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

import com.example.fitsync.data.model.Profile;
import com.example.fitsync.data.model.Rutina;
import com.example.fitsync.data.model.RutinaEjercicio;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

import retrofit2.http.DELETE;

public interface SupabaseDbApi {

    @GET("rest/v1/profiles")
    Call<List<Profile>> getProfile(
            @Header("Authorization") String bearerToken,
            @Query("id") String idFilter,
            @Query("select") String select
    );

    @Headers({
            "Prefer: count=exact",
            "Range: 0-0"
    })
    @GET("rest/v1/sesiones?select=id")
    Call<List<Object>> countCompletedSessions(
            @Header("Authorization") String bearerToken,
            @Query("usuario_id") String userIdFilter,
            @Query("completada") String completadaFilter
    );

    /**
     * Obtiene una rutina por su ID.
     * GET /rest/v1/rutinas?id=eq.<id>&select=*
     */
    @GET("rest/v1/rutinas")
    Call<List<Rutina>> getRutina(
            @Header("Authorization") String bearerToken,
            @Query("id") String idFilter,
            @Query("select") String select
    );

    /**
     * Obtiene los ejercicios de una rutina con datos del catálogo (join).
     * GET /rest/v1/rutina_ejercicios?rutina_id=eq.<id>&select=*,ejercicios(*)&order=orden
     */
    @GET("rest/v1/rutina_ejercicios")
    Call<List<RutinaEjercicio>> getRutinaEjercicios(
            @Header("Authorization") String bearerToken,
            @Query("rutina_id") String rutinaIdFilter,
            @Query("select") String select,
            @Query("order") String order
    );

    /**
     * Lista las rutinas del usuario (las suyas + las de amigos).
     * GET /rest/v1/rutinas?creada_por=eq.<userId>&select=*&order=created_at.desc
     */
    @GET("rest/v1/rutinas")
    Call<List<Rutina>> listMyRutinas(
            @Header("Authorization") String bearerToken,
            @Query("creada_por") String creadaPorFilter,
            @Query("select") String select,
            @Query("order") String order
    );

    @DELETE("rest/v1/rutinas")
    Call<Void> deleteRutina(
            @Header("Authorization") String bearerToken,
            @Query("id") String idFilter
    );

    // ── AMIGOS ──

    /**
     * Buscar usuarios por username (función RPC).
     * POST /rest/v1/rpc/buscar_usuarios
     */
    @POST("rest/v1/rpc/buscar_usuarios")
    Call<List<UsuarioBuscado>> buscarUsuarios(
            @Header("Authorization") String bearerToken,
            @Body java.util.Map<String, String> params
    );

    /**
     * Solicitudes de amistad pendientes (función RPC).
     * POST /rest/v1/rpc/mis_solicitudes_pendientes
     */
    @POST("rest/v1/rpc/mis_solicitudes_pendientes")
    Call<List<SolicitudPendiente>> misSolicitudesPendientes(
            @Header("Authorization") String bearerToken
    );

    /**
     * Mis amigos aceptados (función RPC).
     * POST /rest/v1/rpc/mis_amigos
     */
    @POST("rest/v1/rpc/mis_amigos")
    Call<List<Amigo>> misAmigos(
            @Header("Authorization") String bearerToken
    );

    /**
     * Enviar solicitud de amistad.
     * POST /rest/v1/amistades
     */
    @POST("rest/v1/amistades")
    Call<Void> enviarSolicitud(
            @Header("Authorization") String bearerToken,
            @Body AmistadInsert body
    );

    /**
     * Aceptar o rechazar solicitud.
     * PATCH /rest/v1/amistades?id=eq.<id>
     */
    @PATCH("rest/v1/amistades")
    Call<Void> actualizarAmistad(
            @Header("Authorization") String bearerToken,
            @Query("id") String idFilter,
            @Body AmistadUpdate body
    );

    /**
     * Eliminar amistad.
     * DELETE /rest/v1/amistades?id=eq.<id>
     */
    @DELETE("rest/v1/amistades")
    Call<Void> eliminarAmistad(
            @Header("Authorization") String bearerToken,
            @Query("id") String idFilter
    );
    // Ranking global: todos los perfiles ordenados por nivel desc
    @GET("rest/v1/profiles")
    Call<List<Profile>> getRanking(
            @Header("Authorization") String bearerToken,
            @Query("select") String select,
            @Query("order") String order,
            @Query("limit") String limit
    );
}