package com.example.fitsync.data.model;

import com.google.gson.annotations.SerializedName;

public class Rutina {

    private String id;

    @SerializedName("creada_por")
    private String creadaPor;

    private String nombre;
    private String descripcion;
    private String origen;
    private String nivel;

    @SerializedName("duracion_estimada_min")
    private Integer duracionEstimadaMin;

    @SerializedName("calorias_estimadas")
    private Integer caloriasEstimadas;

    @SerializedName("created_at")
    private String createdAt;

    public String getId() { return id; }
    public String getCreadaPor() { return creadaPor; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getOrigen() { return origen; }
    public String getNivel() { return nivel; }
    public Integer getDuracionEstimadaMin() { return duracionEstimadaMin; }
    public Integer getCaloriasEstimadas() { return caloriasEstimadas; }
    public String getCreatedAt() { return createdAt; }
}