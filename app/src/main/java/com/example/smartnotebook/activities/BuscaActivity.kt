package com.example.smartnotebook.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartnotebook.databinding.ActivityNovaAnotacaoBinding

// TELA 8: Nova Anotação — editor de texto simples com título e conteúdo
class NovaAnotacaoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNovaAnotacaoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNovaAnotacaoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVoltar.setOnClickListener { finish() }
        configurarBotaoSalvar()
    }

    // Valida os campos e simula o salvamento da anotação
    private fun configurarBotaoSalvar() {
        binding.btnSalvar.setOnClickListener {
            val titulo   = binding.etTituloAnotacao.text.toString().trim()
            val conteudo = binding.etConteudoAnotacao.text.toString().trim()

            if (titulo.isEmpty()) {
                binding.etTituloAnotacao.error = "Informe o título"
                binding.etTituloAnotacao.requestFocus()
                return@setOnClickListener
            }
            if (conteudo.isEmpty()) {
                Toast.makeText(this, "Escreva o conteúdo da anotação", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Simulação — em produção salvaria no banco de dados
            Toast.makeText(this, "Anotação \"$titulo\" salva!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
