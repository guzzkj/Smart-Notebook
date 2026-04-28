package com.example.smartnotebook.activities

import android.graphics.Paint
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartnotebook.databinding.ActivityEsqueceuSenhaBinding

// TELA 4: Esqueceu Minha Senha — recuperação de acesso via e-mail acadêmico
class EsqueceuSenhaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEsqueceuSenhaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEsqueceuSenhaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarBotaoEnviar()
        configurarVoltarLogin()
        // Sublinha o link de suporte, fiel à referência visual
        binding.tvSuporte.paintFlags = binding.tvSuporte.paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }

    // Valida o e-mail e simula o envio do código de recuperação
    private fun configurarBotaoEnviar() {
        binding.btnEnviarCodigo.setOnClickListener {
            val email = binding.etEmailRecuperacao.text.toString().trim()

            if (email.isEmpty()) {
                binding.etEmailRecuperacao.error = "Informe seu e-mail"
                binding.etEmailRecuperacao.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.etEmailRecuperacao.error = "E-mail inválido"
                binding.etEmailRecuperacao.requestFocus()
                return@setOnClickListener
            }

            // Simulação de envio — em produção chamaria uma API
            Toast.makeText(this, "Código enviado para $email", Toast.LENGTH_LONG).show()
        }
    }

    // Link "← Voltar para o Login" → fecha esta tela e volta ao Login
    private fun configurarVoltarLogin() {
        binding.tvVoltarLogin.setOnClickListener { finish() }
    }
}
