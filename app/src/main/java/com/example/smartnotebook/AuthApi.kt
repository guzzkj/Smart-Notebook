package com.example.smartnotebook

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

// Endpoints do Supabase Auth (GoTrue) — autenticação por e-mail/senha
interface AuthApi {

    @POST("auth/v1/signup")
    fun cadastrar(@Body body: SignUpRequest): Call<AuthResponse>

    @POST("auth/v1/token?grant_type=password")
    fun login(@Body body: SignInRequest): Call<AuthResponse>

    // Troca o refresh_token por um novo par access/refresh — usado quando o access_token expira
    @POST("auth/v1/token?grant_type=refresh_token")
    fun renovarToken(@Body body: RefreshTokenRequest): Call<AuthResponse>

    // Atualiza e-mail/metadados do usuário logado (header Authorization carrega o access_token dele)
    @PUT("auth/v1/user")
    fun atualizarUsuario(@Body body: UpdateUserRequest): Call<SupabaseUser>
}
