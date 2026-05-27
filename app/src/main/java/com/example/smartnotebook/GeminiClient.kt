package com.example.smartnotebook

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Cliente Retrofit exclusivo para a API do Google Gemini — separado do RetrofitClient do XAMPP
object GeminiClient {

    private const val BASE_URL = "https://generativelanguage.googleapis.com/"
    const val API_KEY = "AIzaSyA4WjG2AMwanY63o9sXKbyocVYxV0UIbzw"
    const val MODEL   = "gemini-1.5-flash"

    val service: GeminiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeminiService::class.java)
    }
}
