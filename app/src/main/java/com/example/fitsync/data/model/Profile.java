package com.example.fitsync.data.model;

import com.google.gson.annotations.SerializedName;

public class Profile {

    private String id;
    private String username;

    @SerializedName("nombre_completo")
    private String nombreCompleto;

    @SerializedName("avatar_url")
    private String avatarUrl;

    private int nivel;

    @SerializedName("racha_dias")
    private int rachaDias;

    private String biografia;

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getAvatarUrl() { return avatarUrl; }
    public int getNivel() { return nivel; }
    public int getRachaDias() { return rachaDias; }
    public String getBiografia() { return biografia; }

    public void setUsername(String username) { this.username = username; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public void setBiografia(String biografia) { this.biografia = biografia; }
}