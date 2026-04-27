package com.example.smartnotebook.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.smartnotebook.databinding.ActivitySplashBinding

// TELA 1: Splash Screen — exibida por 2 segundos ao abrir o app
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Aguarda 2 segundos e navega para a tela de Login usando Intent explícita
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Remove a Splash da pilha — usuário não pode voltar para ela
        }, 2000)
    }
}
