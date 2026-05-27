package com.example.smartnotebook

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiService {

    @POST("v1beta/models/gemini-1.5-flash:generateContent")
    fun melhorarAnotacao(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): Call<GeminiResponse>
}
