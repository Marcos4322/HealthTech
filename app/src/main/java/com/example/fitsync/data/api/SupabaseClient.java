package com.example.fitsync.data.api;

import com.example.fitsync.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SupabaseClient {

    private static Retrofit retrofitRest;
    private static Retrofit retrofitFunctions;
    private static SupabaseAuthApi authApi;
    private static SupabaseDbApi dbApi;
    private static SupabaseFunctionsApi functionsApi;

    /**
     * Limpia y valida la URL base para Retrofit.
     * Retrofit exige que la baseUrl acabe en '/' y sea una URL válida.
     */
    private static String getBaseUrl() {
        String url = BuildConfig.SUPABASE_URL
                .replace("\\:", ":")   // por si viene escapada de local.properties
                .trim();
        if (!url.endsWith("/")) url = url + "/";
        return url;
    }

    private static OkHttpClient buildClient(String apiKey) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG
                ? HttpLoggingInterceptor.Level.BODY
                : HttpLoggingInterceptor.Level.NONE);

        return new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    okhttp3.Request original = chain.request();
                    okhttp3.Request request = original.newBuilder()
                            .header("apikey", apiKey)
                            .header("Content-Type", "application/json")
                            .build();
                    return chain.proceed(request);
                })
                .addInterceptor(logging)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    private static Retrofit getRetrofitRest() {
        if (retrofitRest == null) {
            retrofitRest = new Retrofit.Builder()
                    .baseUrl(getBaseUrl())
                    .client(buildClient(BuildConfig.SUPABASE_ANON_KEY))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitRest;
    }

    private static Retrofit getRetrofitFunctions() {
        if (retrofitFunctions == null) {
            retrofitFunctions = new Retrofit.Builder()
                    .baseUrl(getBaseUrl())
                    .client(buildClient(BuildConfig.SUPABASE_PUBLISHABLE_KEY))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitFunctions;
    }

    public static SupabaseAuthApi getAuthApi() {
        if (authApi == null) {
            authApi = getRetrofitRest().create(SupabaseAuthApi.class);
        }
        return authApi;
    }

    public static SupabaseDbApi getDbApi() {
        if (dbApi == null) {
            dbApi = getRetrofitRest().create(SupabaseDbApi.class);
        }
        return dbApi;
    }

    public static SupabaseFunctionsApi getFunctionsApi() {
        if (functionsApi == null) {
            functionsApi = getRetrofitFunctions().create(SupabaseFunctionsApi.class);
        }
        return functionsApi;
    }
}