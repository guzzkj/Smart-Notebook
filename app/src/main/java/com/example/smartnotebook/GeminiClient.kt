package com.example.smartnotebook

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Cliente Retrofit exclusivo para a API do Google Gemini — separado do RetrofitClient do XAMPP
object GeminiClient {

    private const val BASE_URL = "https://generativelanguage.googleapis.com/"
    // Chave lida de local.properties via BuildConfig — nunca commitar a chave no código-fonte
    val API_KEY: String = BuildConfig.GEMINI_API_KEY
    const val MODEL = "gemini-2.5-flash"

    val service: GeminiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeminiService::class.java)
    }
}
