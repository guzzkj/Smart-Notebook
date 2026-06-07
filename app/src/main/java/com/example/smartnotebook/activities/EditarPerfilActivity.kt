package com.example.smartnotebook.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.smartnotebook.databinding.ActivityEditarPerfilBinding

// TELA 13: Editar Perfil — permite alterar nome, e-mail e foto do usuário
class EditarPerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarPerfilBinding

    // Seletor de imagens do sistema (Photo Picker) — não exige permissão de armazenamento
    private val seletorDeFoto = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.imgAvatarPerfil.scaleType = ImageView.ScaleType.CENTER_CROP
            binding.imgAvatarPerfil.setImageURI(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVoltar.setOnClickListener { finish() }
        configurarBotaoSalvar()
        configurarAlterarFoto()
    }

    // Abre o seletor de fotos do sistema para trocar o avatar
    private fun configurarAlterarFoto() {
        val abrirSeletor = {
            seletorDeFoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.layoutAvatar.setOnClickListener { abrirSeletor() }
        binding.tvAltararFoto.setOnClickListener { abrirSeletor() }
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
