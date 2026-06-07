package com.example.smartnotebook

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

// Endpoints do Supabase Auth (GoTrue) — autenticação por e-mail/senha
interface AuthApi {

    @POST("auth/v1/signup")
    fun cadastrar(@Body body: SignUpRequest): Call<AuthResponse>

    @POST("auth/v1/token?grant_type=password")
    fun login(@Body body: SignInRequest): Call<AuthResponse>
}
