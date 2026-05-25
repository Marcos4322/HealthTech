package com.example.fitsync.data.api;

import com.example.fitsync.data.model.RoutineGeneratedResponse;
import com.example.fitsync.data.model.RoutineRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface SupabaseFunctionsApi {

    /**
     * Llama a la Edge Function generate-routine en Supabase.
     * POST /functions/v1/generate-routine
     */
    @POST("functions/v1/generate-routine")
    Call<RoutineGeneratedResponse> generateRoutine(
            @Header("Authorization") String bearerToken,
            @Body RoutineRequest request
    );
}