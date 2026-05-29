package com.example.fitsync.data.model;

import com.google.gson.annotations.SerializedName;

public class AmistadInsert {

    @SerializedName("solicitante_id")
    private String solicitanteId;

    @SerializedName("destinatario_id")
    private String destinatarioId;

    public AmistadInsert(String solicitanteId, String destinatarioId) {
        this.solicitanteId = solicitanteId;
        this.destinatarioId = destinatarioId;
    }
}