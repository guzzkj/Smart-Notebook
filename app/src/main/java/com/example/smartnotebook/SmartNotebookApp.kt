package com.example.smartnotebook

import android.app.Application

// Application customizada — necessária para o SupabaseClient acessar o token de sessão salvo
class SmartNotebookApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SupabaseClient.init(this)
    }
}
