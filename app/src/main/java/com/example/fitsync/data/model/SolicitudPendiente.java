package com.example.fitsync.data.model;

import com.google.gson.annotations.SerializedName;

public class SolicitudPendiente {

    @SerializedName("amistad_id")
    private String amistadId;

    @SerializedName("solicitante_id")
    private String solicitanteId;

    private String username;

    @SerializedName("nombre_completo")
    private String nombreCompleto;

    @SerializedName("avatar_url")
    private String avatarUrl;

    private int nivel;

    @SerializedName("created_at")
    private String createdAt;

    public String getAmistadId() { return amistadId; }
    public String getSolicitanteId() { return solicitanteId; }
    public String getUsername() { return username; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getAvatarUrl() { return avatarUrl; }
    public int getNivel() { return nivel; }
    public String getCreatedAt() { return createdAt; }
}