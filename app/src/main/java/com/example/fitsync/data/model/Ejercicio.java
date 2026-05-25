package com.example.fitsync.data.model;

import com.google.gson.annotations.SerializedName;

public class Ejercicio {

    private String id;
    private String nombre;
    private String descripcion;

    @SerializedName("grupo_muscular")
    private String grupoMuscular;

    private String equipamiento;
    private String dificultad;

    @SerializedName("imagen_url")
    private String imagenUrl;

    @SerializedName("video_url")
    private String videoUrl;

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getGrupoMuscular() { return grupoMuscular; }
    public String getEquipamiento() { return equipamiento; }
    public String getDificultad() { return dificultad; }
    public String getImagenUrl() { return imagenUrl; }
    public String getVideoUrl() { return videoUrl; }
}