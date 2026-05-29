package com.example.fitsync.data.model;

import com.google.gson.annotations.SerializedName;

public class UsuarioBuscado {

    private String id;
    private String username;

    @SerializedName("nombre_completo")
    private String nombreCompleto;

    @SerializedName("avatar_url")
    private String avatarUrl;

    private int nivel;

    @SerializedName("estado_amistad")
    private String estadoAmistad;

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getAvatarUrl() { return avatarUrl; }
    public int getNivel() { return nivel; }
    public String getEstadoAmistad() { return estadoAmistad; }
}