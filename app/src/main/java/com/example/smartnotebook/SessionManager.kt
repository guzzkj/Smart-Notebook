package com.example.smartnotebook

import android.content.Context

// Gerencia a sessão do usuário via SharedPreferences (Supabase Auth — guarda JWT da sessão)
object SessionManager {
    private const val PREFS         = "smartnotebook_prefs"
    private const val KEY_USER_ID   = "user_id"
    private const val KEY_USER_NOME = "user_nome"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_ACCESS_TOKEN = "access_token"
    private const val KEY_REFRESH_TOKEN = "refresh_token"
    private const val KEY_AVATAR_URI = "avatar_uri"

    fun salvar(context: Context, userId: String, nome: String, email: String, accessToken: String, refreshToken: String? = null) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().apply {
            putString(KEY_USER_ID, userId)
            putString(KEY_USER_NOME, nome)
            putString(KEY_USER_EMAIL, email)
            putString(KEY_ACCESS_TOKEN, accessToken)
            if (refreshToken != null) putString(KEY_REFRESH_TOKEN, refreshToken)
        }.apply()
    }

    fun getUserId(context: Context): String =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY_USER_ID, "") ?: ""

    fun getNome(context: Context): String =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY_USER_NOME, "") ?: ""

    fun getEmail(context: Context): String =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY_USER_EMAIL, "") ?: ""

    // Token JWT da sessão Supabase — enviado no header Authorization para a Auth/PostgREST respeitar RLS
    fun getAccessToken(context: Context): String? =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY_ACCESS_TOKEN, null)

    // Refresh token — usado para obter um novo access_token quando o atual expira
    fun getRefreshToken(context: Context): String? =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY_REFRESH_TOKEN, null)

    // Atualiza apenas o par de tokens, preservando o restante da sessão (chamado pelo Authenticator ao renovar)
    fun atualizarTokens(context: Context, accessToken: String, refreshToken: String?) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().apply {
            putString(KEY_ACCESS_TOKEN, accessToken)
            if (refreshToken != null) putString(KEY_REFRESH_TOKEN, refreshToken)
        }.apply()
    }

    // URI local da foto de perfil escolhida pelo usuário (não há backend de storage no projeto)
    fun getAvatarUri(context: Context): String? =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY_AVATAR_URI, null)

    fun salvarAvatarUri(context: Context, uri: String) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putString(KEY_AVATAR_URI, uri).apply()
    }

    fun estaLogado(context: Context): Boolean = getUserId(context).isNotEmpty()

    fun logout(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().clear().apply()
    }
}
