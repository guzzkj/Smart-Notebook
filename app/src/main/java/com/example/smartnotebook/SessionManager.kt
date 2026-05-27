package com.example.smartnotebook

import android.content.Context

// Gerencia a sessão do usuário via SharedPreferences (sem JWT — só user_id local)
object SessionManager {
    private const val PREFS        = "smartnotebook_prefs"
    private const val KEY_USER_ID  = "user_id"
    private const val KEY_USER_NOME  = "user_nome"
    private const val KEY_USER_EMAIL = "user_email"

    fun salvar(context: Context, userId: Int, nome: String, email: String) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
            .putInt(KEY_USER_ID, userId)
            .putString(KEY_USER_NOME, nome)
            .putString(KEY_USER_EMAIL, email)
            .apply()
    }

    fun getUserId(context: Context): Int =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getInt(KEY_USER_ID, -1)

    fun getNome(context: Context): String =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY_USER_NOME, "") ?: ""

    fun getEmail(context: Context): String =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY_USER_EMAIL, "") ?: ""

    fun estaLogado(context: Context): Boolean = getUserId(context) != -1

    fun logout(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().clear().apply()
    }
}
