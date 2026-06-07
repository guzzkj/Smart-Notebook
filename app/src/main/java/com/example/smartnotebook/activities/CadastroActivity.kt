package com.example.smartnotebook.activities

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.smartnotebook.AuthResponse
import com.example.smartnotebook.R
import com.example.smartnotebook.SessionManager
import com.example.smartnotebook.SignUpRequest
import com.example.smartnotebook.SupabaseClient
import com.example.smartnotebook.databinding.ActivityCadastroBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Tela de Cadastro — criação de nova conta no app
class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding
    private var senhaVisivel = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarBotaoVoltar()
        configurarToggleSenha()
        configurarTextosClicaveis()
        configurarBotaoCadastrar()
        configurarFazerLogin()
    }

    private fun configurarBotaoVoltar() {
        binding.btnVoltar.setOnClickListener { finish() }
    }

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

    private fun configurarTextosClicaveis() {
        val textoCompleto = "Eu li e aceito os Termos de Uso e a Política de Privacidade."
        val spannable = SpannableString(textoCompleto)
        val corRoxa = ContextCompat.getColor(this, R.color.roxo_primario)

        val inicioTermos = textoCompleto.indexOf("Termos de Uso")
        val fimTermos = inicioTermos + "Termos de Uso".length
        spannable.setSpan(ForegroundColorSpan(corRoxa), inicioTermos, fimTermos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(this@CadastroActivity, TermosUsoActivity::class.java))
            }
        }, inicioTermos, fimTermos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val inicioPriv = textoCompleto.indexOf("Política de Privacidade")
        val fimPriv = inicioPriv + "Política de Privacidade".length
        spannable.setSpan(ForegroundColorSpan(corRoxa), inicioPriv, fimPriv, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(this@CadastroActivity, TermosUsoActivity::class.java))
            }
        }, inicioPriv, fimPriv, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.tvTermos.text = spannable
        binding.tvTermos.movementMethod = LinkMovementMethod.getInstance()
        binding.tvTermos.highlightColor = android.graphics.Color.TRANSPARENT
    }

    // Valida os campos, cria a conta via Supabase Auth e segue para a tela principal (ou pede confirmação por e-mail)
    private fun configurarBotaoCadastrar() {
        binding.btnCadastrar.setOnClickListener {
            val nome           = binding.etNome.text.toString().trim()
            val email          = binding.etEmail.text.toString().trim()
            val senha          = binding.etSenha.text.toString().trim()
            val confirmarSenha = binding.etConfirmarSenha.text.toString().trim()
            val termosAceitos  = binding.cbTermos.isChecked

            if (!validarCampos(nome, email, senha, confirmarSenha, termosAceitos)) return@setOnClickListener

            binding.btnCadastrar.isEnabled = false

            val body = SignUpRequest(email = email, password = senha, data = mapOf("nome" to nome))

            SupabaseClient.authApi.cadastrar(body)
                .enqueue(object : Callback<AuthResponse> {
                    override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                        binding.btnCadastrar.isEnabled = true
                        val resposta = response.body()
                        val user = resposta?.user
                        val token = resposta?.accessToken

                        if (response.isSuccessful && user != null && token != null) {
                            // Confirmação de e-mail desativada no projeto: já entra logado
                            SessionManager.salvar(this@CadastroActivity, user.id, nome, user.email ?: email, token, resposta.refreshToken)
                            startActivity(Intent(this@CadastroActivity, MinhasMateriasActivity::class.java))
                            finish()
                        } else if (response.isSuccessful && user != null) {
                            Toast.makeText(this@CadastroActivity, "Conta criada! Confirme seu e-mail para entrar.", Toast.LENGTH_LONG).show()
                            finish()
                        } else {
                            val msg = resposta?.errorDescription ?: resposta?.msg ?: "Erro ao criar conta. Tente novamente."
                            Toast.makeText(this@CadastroActivity, msg, Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                        binding.btnCadastrar.isEnabled = true
                        Toast.makeText(this@CadastroActivity, "Sem conexão com o Supabase. Verifique sua internet.", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    private fun validarCampos(
        nome: String, email: String, senha: String,
        confirmarSenha: String, termosAceitos: Boolean
    ): Boolean {
        if (nome.isEmpty()) {
            binding.etNome.error = "Informe seu nome"
            binding.etNome.requestFocus(); return false
        }
        if (nome.length < 3) {
            binding.etNome.error = "Nome muito curto"
            binding.etNome.requestFocus(); return false
        }
        if (email.isEmpty()) {
            binding.etEmail.error = "Informe seu e-mail"
            binding.etEmail.requestFocus(); return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "E-mail inválido"
            binding.etEmail.requestFocus(); return false
        }
        if (senha.isEmpty()) {
            binding.etSenha.error = "Crie uma senha"
            binding.etSenha.requestFocus(); return false
        }
        if (senha.length < 6) {
            binding.etSenha.error = "Mínimo 6 caracteres"
            binding.etSenha.requestFocus(); return false
        }
        if (confirmarSenha != senha) {
            binding.etConfirmarSenha.error = "As senhas não coincidem"
            binding.etConfirmarSenha.requestFocus(); return false
        }
        if (!termosAceitos) {
            Toast.makeText(this, "Aceite os Termos de Uso para continuar", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun configurarFazerLogin() {
        binding.tvFazerLogin.setOnClickListener { finish() }
    }
}
