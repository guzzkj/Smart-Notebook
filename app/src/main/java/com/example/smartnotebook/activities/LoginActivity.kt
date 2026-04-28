package com.example.smartnotebook.activities

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartnotebook.databinding.ActivityLoginBinding

// TELA 2: Login — autenticação do usuário para acesso ao app
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var senhaVisivel = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarToggleSenha()
        configurarBotaoEntrar()
        configurarEsqueceuSenha()
        configurarCadastreSe()
    }

    // Alterna visibilidade da senha ao clicar no ícone de olho
    private fun configurarToggleSenha() {
        binding.btnToggleSenha.setOnClickListener {
            senhaVisivel = !senhaVisivel
            binding.etSenha.inputType = if (senhaVisivel) {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            binding.etSenha.setSelection(binding.etSenha.text?.length ?: 0)
        }
    }

    // Valida os campos e navega para a tela principal após login
    private fun configurarBotaoEntrar() {
        binding.btnEntrar.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val senha = binding.etSenha.text.toString().trim()

            // if (!validarCampos(email, senha)) return@setOnClickListener

            // Intent explícita para a tela principal (Minhas Matérias)
            startActivity(Intent(this, MinhasMateriasActivity::class.java))
            finish() // Remove o Login da pilha — usuário não pode voltar
        }
    }

    private fun validarCampos(email: String, senha: String): Boolean {
        if (email.isEmpty()) {
            binding.etEmail.error = "Informe seu email"
            binding.etEmail.requestFocus()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Email inválido"
            binding.etEmail.requestFocus()
            return false
        }
        if (senha.isEmpty()) {
            binding.etSenha.error = "Informe sua senha"
            binding.etSenha.requestFocus()
            return false
        }
        if (senha.length < 6) {
            binding.etSenha.error = "Mínimo 6 caracteres"
            binding.etSenha.requestFocus()
            return false
        }
        return true
    }

    // Link "Esqueceu a senha?" → abre a tela de recuperação via Intent explícita
    private fun configurarEsqueceuSenha() {
        binding.tvEsqueceuSenha.setOnClickListener {
            startActivity(Intent(this, EsqueceuSenhaActivity::class.java))
        }
    }

    // Link "Cadastre-se" → abre a tela de Cadastro via Intent explícita
    private fun configurarCadastreSe() {
        binding.tvCadastreSe.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }
    }
}
