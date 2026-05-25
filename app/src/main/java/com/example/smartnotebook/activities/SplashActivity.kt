package com.example.smartnotebook.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.smartnotebook.databinding.ActivitySplashBinding
import com.example.smartnotebook.supabase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// TELA 1: Splash Screen — exibida por 2 segundos ao abrir o app
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Aguarda 2 segundos e verifica se já existe sessão ativa no Supabase
        lifecycleScope.launch {
            delay(2000)
            val sessaoAtiva = supabase.auth.currentSessionOrNull()
            val destino = if (sessaoAtiva != null) {
                Intent(this@SplashActivity, MinhasMateriasActivity::class.java)
            } else {
                Intent(this@SplashActivity, LoginActivity::class.java)
            }
            startActivity(destino)
            finish() // Remove a Splash da pilha — usuário não pode voltar para ela
        }
    }
}
