package com.example.smartnotebook

import android.content.Context

// Gerencia a sessão do usuário via SharedPreferences (Supabase Auth — guarda JWT da sessão)
object SessionManager {
    private const val PREFS         = "smartnotebook_prefs"
    private const val KEY_USER_ID   = "user_id"
    private const val KEY_USER_NOME = "user_nome"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_ACCESS_TOKEN = "access_token"

    fun salvar(context: Context, userId: String, nome: String, email: String, accessToken: String) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
            .putString(KEY_USER_ID, userId)
            .putString(KEY_USER_NOME, nome)
            .putString(KEY_USER_EMAIL, email)
            .putString(KEY_ACCESS_TOKEN, accessToken)
            .apply()
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

    fun estaLogado(context: Context): Boolean = getUserId(context).isNotEmpty()

    fun logout(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().clear().apply()
    }
}
