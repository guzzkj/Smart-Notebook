package com.example.smartnotebook.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartnotebook.databinding.ActivityEditarPerfilBinding

// TELA 12: Editar Perfil — permite alterar nome e e-mail do usuário
class EditarPerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarPerfilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVoltar.setOnClickListener { finish() }
        configurarBotaoSalvar()
        configurarAlterarFoto()
    }

    // Simula a alteração da foto de perfil
    private fun configurarAlterarFoto() {
        binding.layoutAvatar.setOnClickListener {
            Toast.makeText(this, "Selecionar foto em breve", Toast.LENGTH_SHORT).show()
        }
        binding.tvAltararFoto.setOnClickListener {
            Toast.makeText(this, "Selecionar foto em breve", Toast.LENGTH_SHORT).show()
        }
    }

    // Valida os campos e simula o salvamento das alterações
    private fun configurarBotaoSalvar() {
        binding.btnSalvarAlteracoes.setOnClickListener {
            val nome  = binding.etNomeCompleto.text.toString().trim()
            val email = binding.etEmailPerfil.text.toString().trim()

            if (nome.isEmpty()) {
                binding.etNomeCompleto.error = "Informe seu nome"
                binding.etNomeCompleto.requestFocus()
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                binding.etEmailPerfil.error = "Informe seu e-mail"
                binding.etEmailPerfil.requestFocus()
                return@setOnClickListener
            }

            // Simulação — em produção salvaria no banco de dados
            Toast.makeText(this, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
