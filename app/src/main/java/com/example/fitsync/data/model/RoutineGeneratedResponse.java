package com.example.fitsync.data.model;

import com.google.gson.annotations.SerializedName;

public class RoutineGeneratedResponse {

    @SerializedName("rutina_id")
    private String rutinaId;

    private String nombre;

    @SerializedName("ejercicios_count")
    private int ejerciciosCount;

    @SerializedName("nuevos_ejercicios_creados")
    private int nuevosEjerciciosCreados;

    private String error;

    public String getRutinaId() { return rutinaId; }
    public String getNombre() { return nombre; }
    public int getEjerciciosCount() { return ejerciciosCount; }
    public int getNuevosEjerciciosCreados() { return nuevosEjerciciosCreados; }
    public String getError() { return error; }
}