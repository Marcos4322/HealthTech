package com.example.fitsync.data.model;

import com.google.gson.annotations.SerializedName;

public class RutinaEjercicio {

    private String id;

    @SerializedName("rutina_id")
    private String rutinaId;

    private int orden;
    private int series;
    private Integer repeticiones;

    @SerializedName("duracion_seg")
    private Integer duracionSeg;

    @SerializedName("peso_kg")
    private Double pesoKg;

    @SerializedName("descanso_seg")
    private int descansoSeg;

    private String notas;

    // Join con la tabla ejercicios (anidado)
    @SerializedName("ejercicios")
    private Ejercicio ejercicio;

    public String getId() { return id; }
    public String getRutinaId() { return rutinaId; }
    public int getOrden() { return orden; }
    public int getSeries() { return series; }
    public Integer getRepeticiones() { return repeticiones; }
    public Integer getDuracionSeg() { return duracionSeg; }
    public Double getPesoKg() { return pesoKg; }
    public int getDescansoSeg() { return descansoSeg; }
    public String getNotas() { return notas; }
    public Ejercicio getEjercicio() { return ejercicio; }
}