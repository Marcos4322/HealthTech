package com.example.fitsync.data.api;

import com.example.fitsync.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SupabaseClient {

    private static SupabaseAuthApi authApi;

    public static SupabaseAuthApi getAuthApi() {
        if (authApi == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(BuildConfig.DEBUG
                    ? HttpLoggingInterceptor.Level.BODY
                    : HttpLoggingInterceptor.Level.NONE);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        okhttp3.Request original = chain.request();
                        okhttp3.Request request = original.newBuilder()
                                .header("apikey", BuildConfig.SUPABASE_ANON_KEY)
                                .header("Content-Type", "application/json")
                                .build();
                        return chain.proceed(request);
                    })
                    .addInterceptor(logging)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.SUPABASE_URL + "/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            authApi = retrofit.create(SupabaseAuthApi.class);
        }
        return authApi;
    }
}