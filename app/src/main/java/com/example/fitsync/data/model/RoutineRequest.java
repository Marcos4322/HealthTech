package com.example.fitsync.data.model;

import com.google.gson.annotations.SerializedName;

public class RoutineRequest {

    private String objetivo;
    private String nivel;

    @SerializedName("dias_semana")
    private int diasSemana;

    private String equipamiento;

    public RoutineRequest(String objetivo, String nivel, int diasSemana, String equipamiento) {
        this.objetivo = objetivo;
        this.nivel = nivel;
        this.diasSemana = diasSemana;
        this.equipamiento = equipamiento;
    }

    public String getObjetivo() { return objetivo; }
    public String getNivel() { return nivel; }
    public int getDiasSemana() { return diasSemana; }
    public String getEquipamiento() { return equipamiento; }
}