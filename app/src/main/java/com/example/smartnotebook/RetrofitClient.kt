package com.example.smartnotebook

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // ⚠️ Trocar pelo IP IPv4 da máquina (rodar ipconfig no cmd)
    private const val BASE_URL = "http://10.0.2.2/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
