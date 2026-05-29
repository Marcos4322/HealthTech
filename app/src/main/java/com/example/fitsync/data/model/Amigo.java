package com.example.fitsync.data.model;

import com.google.gson.annotations.SerializedName;

public class Amigo {

    @SerializedName("amistad_id")
    private String amistadId;

    @SerializedName("amigo_id")
    private String amigoId;

    private String username;

    @SerializedName("nombre_completo")
    private String nombreCompleto;

    @SerializedName("avatar_url")
    private String avatarUrl;

    private int nivel;

    private String desde;

    public String getAmistadId() { return amistadId; }
    public String getAmigoId() { return amigoId; }
    public String getUsername() { return username; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getAvatarUrl() { return avatarUrl; }
    public int getNivel() { return nivel; }
    public String getDesde() { return desde; }
}