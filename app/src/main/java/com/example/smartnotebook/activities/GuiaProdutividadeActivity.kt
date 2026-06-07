package com.example.smartnotebook.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smartnotebook.databinding.ActivityGuiaProdutividadeBinding

// Tela Guia de Produtividade — dicas de uso do app acessadas pelo Menu
class GuiaProdutividadeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGuiaProdutividadeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuiaProdutividadeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVoltar.setOnClickListener { finish() }
    }
}
