package com.example.smartnotebook

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// Cliente Retrofit exclusivo para a API do Google Gemini — separado do SupabaseClient
object GeminiClient {

    private const val BASE_URL = "https://generativelanguage.googleapis.com/"
    // Chave lida de local.properties via BuildConfig — nunca commitar a chave no código-fonte
    val API_KEY: String = BuildConfig.GEMINI_API_KEY
    const val MODEL = "gemini-2.5-flash"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val service: GeminiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeminiService::class.java)
    }
}
