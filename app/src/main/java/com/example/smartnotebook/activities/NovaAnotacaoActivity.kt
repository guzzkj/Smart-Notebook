package com.example.smartnotebook.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.smartnotebook.SupabaseRepository
import com.example.smartnotebook.databinding.ActivityNovaAnotacaoBinding
import kotlinx.coroutines.launch

// TELA 8: Nova Anotação — editor de texto simples com título e conteúdo
class NovaAnotacaoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNovaAnotacaoBinding

    companion object {
        // Chave para receber o ID da matéria via Intent
        const val EXTRA_MATERIA_ID = "extra_materia_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNovaAnotacaoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVoltar.setOnClickListener { finish() }
        configurarBotaoSalvar()
    }

    // Valida os campos e salva a anotação no Supabase
    private fun configurarBotaoSalvar() {
        binding.btnSalvar.setOnClickListener {
            val materiaId = intent.getStringExtra(EXTRA_MATERIA_ID) ?: ""
            val titulo    = binding.etTituloAnotacao.text.toString().trim()
            val conteudo  = binding.etConteudoAnotacao.text.toString().trim()

            if (titulo.isEmpty()) {
                binding.etTituloAnotacao.error = "Informe o título"
                binding.etTituloAnotacao.requestFocus()
                return@setOnClickListener
            }
            if (conteudo.isEmpty()) {
                Toast.makeText(this, "Escreva o conteúdo da anotação", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (materiaId.isEmpty()) {
                Toast.makeText(this, "Matéria não identificada", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Desabilita o botão durante a operação para evitar cliques duplicados
            binding.btnSalvar.isEnabled = false

            lifecycleScope.launch {
                try {
                    SupabaseRepository.inserirAnotacao(materiaId, titulo, conteudo)
                    setResult(RESULT_OK)
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this@NovaAnotacaoActivity,
                        "Erro ao salvar anotação: ${e.message}", Toast.LENGTH_SHORT).show()
                } finally {
                    binding.btnSalvar.isEnabled = true
                }
            }
        }
    }
}
