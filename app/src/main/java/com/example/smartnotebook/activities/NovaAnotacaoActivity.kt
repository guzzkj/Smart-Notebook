package com.example.smartnotebook.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartnotebook.RetrofitClient
import com.example.smartnotebook.SessionManager
import com.example.smartnotebook.databinding.ActivityNovaAnotacaoBinding
import com.example.smartnotebook.models.Anotacao
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    // Valida os campos e salva a anotação via PHP/MySQL
    private fun configurarBotaoSalvar() {
        binding.btnSalvar.setOnClickListener {
            val materiaId = intent.getIntExtra(EXTRA_MATERIA_ID, -1)
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
            if (materiaId == -1) {
                Toast.makeText(this, "Matéria não identificada", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Desabilita o botão durante a operação para evitar cliques duplicados
            binding.btnSalvar.isEnabled = false

            val userId = SessionManager.getUserId(this)

            RetrofitClient.apiService.inserirAnotacao(userId, materiaId, titulo, conteudo)
                .enqueue(object : Callback<Anotacao> {
                    override fun onResponse(call: Call<Anotacao>, response: Response<Anotacao>) {
                        binding.btnSalvar.isEnabled = true
                        if (response.isSuccessful && response.body() != null) {
                            setResult(RESULT_OK)
                            finish()
                        } else {
                            Toast.makeText(this@NovaAnotacaoActivity, "Erro ao salvar anotação", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<Anotacao>, t: Throwable) {
                        binding.btnSalvar.isEnabled = true
                        Toast.makeText(this@NovaAnotacaoActivity, "Sem conexão. Verifique se o XAMPP está ativo.", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}
