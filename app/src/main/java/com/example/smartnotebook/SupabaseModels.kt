package com.example.smartnotebook

import com.google.gson.annotations.SerializedName

// ── Auth (GoTrue /auth/v1) ────────────────────────────────────────────────────

data class SignUpRequest(
    val email: String,
    val password: String,
    val data: Map<String, String> // metadados extras, ex: {"nome": "..."}
)

data class SignInRequest(
    val email: String,
    val password: String
)

data class RefreshTokenRequest(
    @SerializedName("refresh_token") val refreshToken: String
)

data class AuthResponse(
    @SerializedName("access_token") val accessToken: String? = null,
    @SerializedName("refresh_token") val refreshToken: String? = null,
    val user: SupabaseUser? = null,
    @SerializedName("error_description") val errorDescription: String? = null,
    val msg: String? = null
)

// Corpo do PUT /auth/v1/user — atualiza e-mail e/ou metadados (nome) do usuário logado
data class UpdateUserRequest(
    val email: String? = null,
    val data: Map<String, String>? = null
)

data class SupabaseUser(
    val id: String,
    val email: String? = null,
    @SerializedName("user_metadata") val userMetadata: Map<String, String>? = null
)

// ── Corpos de inserção (PostgREST /rest/v1) ───────────────────────────────────

data class MateriaInsert(
    @SerializedName("user_id") val userId: String,
    val nome: String,
    val professor: String,
    @SerializedName("dias_aula") val diasAula: List<String>,
    @SerializedName("cor_hex") val corHex: String
)

data class MateriaUpdate(
    val nome: String,
    val professor: String,
    @SerializedName("dias_aula") val diasAula: List<String>
)

data class AnotacaoInsert(
    @SerializedName("user_id") val userId: String,
    @SerializedName("materia_id") val materiaId: Int,
    val titulo: String,
    val conteudo: String
)

data class AnotacaoUpdate(
    val titulo: String,
    val conteudo: String
)

data class AtividadeInsert(
    @SerializedName("user_id") val userId: String,
    @SerializedName("materia_id") val materiaId: Int,
    val titulo: String,
    val tipo: String,
    val status: String,
    @SerializedName("data_entrega") val dataEntrega: String,
    val hora: String
)

data class AtividadeUpdate(
    val titulo: String,
    val tipo: String,
    val status: String,
    @SerializedName("data_entrega") val dataEntrega: String,
    val hora: String
)
