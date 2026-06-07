package com.example.smartnotebook.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smartnotebook.databinding.ActivityTermosUsoBinding

// Tela Termos de Uso — texto institucional acessado pelo Menu
class TermosUsoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTermosUsoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermosUsoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVoltar.setOnClickListener { finish() }
    }
}
