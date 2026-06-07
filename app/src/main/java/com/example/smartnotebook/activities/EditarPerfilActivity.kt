package com.example.smartnotebook.activities

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.smartnotebook.SessionManager
import com.example.smartnotebook.SupabaseClient
import com.example.smartnotebook.SupabaseUser
import com.example.smartnotebook.UpdateUserRequest
import com.example.smartnotebook.databinding.ActivityEditarPerfilBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TELA 13: Editar Perfil — permite alterar nome, e-mail e foto do usuário
class EditarPerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarPerfilBinding

    // URI da foto escolhida nesta sessão de edição (persistida localmente ao salvar)
    private var avatarUriEscolhida: Uri? = null

    // Seletor de imagens do sistema (Photo Picker) — não exige permissão de armazenamento
    private val seletorDeFoto = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            avatarUriEscolhida = uri
            exibirAvatar(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVoltar.setOnClickListener { finish() }
        carregarDadosUsuario()
        configurarBotaoSalvar()
        configurarAlterarFoto()
    }

    // Preenche os campos com os dados reais do usuário logado (sessão salva no SessionManager)
    private fun carregarDadosUsuario() {
        binding.etNomeCompleto.setText(SessionManager.getNome(this))
        binding.etEmailPerfil.setText(SessionManager.getEmail(this))

        // Restaura o avatar salvo localmente em edições anteriores (não há storage no backend)
        SessionManager.getAvatarUri(this)?.let { uriSalva ->
            try {
                exibirAvatar(Uri.parse(uriSalva))
            } catch (e: SecurityException) {
                // Permissão de leitura da URI expirou — mantém o avatar padrão
            }
        }
    }

    private fun exibirAvatar(uri: Uri) {
        binding.imgAvatarPerfil.scaleType = ImageView.ScaleType.CENTER_CROP
        binding.imgAvatarPerfil.setImageURI(uri)
    }

    // Abre o seletor de fotos do sistema para trocar o avatar
    private fun configurarAlterarFoto() {
        val abrirSeletor = {
            seletorDeFoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.layoutAvatar.setOnClickListener { abrirSeletor() }
        binding.tvAltararFoto.setOnClickListener { abrirSeletor() }
    }

    // Valida os campos e envia as alterações de nome/e-mail para o Supabase Auth
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

            binding.btnSalvarAlteracoes.isEnabled = false

            // E-mail só entra no corpo se mudou — evita disparar fluxo de confirmação à toa
            val emailMudou = email != SessionManager.getEmail(this)
            val body = UpdateUserRequest(
                email = if (emailMudou) email else null,
                data  = mapOf("nome" to nome)
            )

            SupabaseClient.authApi.atualizarUsuario(body).enqueue(object : Callback<SupabaseUser> {
                override fun onResponse(call: Call<SupabaseUser>, response: Response<SupabaseUser>) {
                    binding.btnSalvarAlteracoes.isEnabled = true
                    val usuario = response.body()
                    if (response.isSuccessful && usuario != null) {
                        avatarUriEscolhida?.let { SessionManager.salvarAvatarUri(this@EditarPerfilActivity, it.toString()) }

                        // Preserva token/userId; atualiza apenas nome e e-mail confirmados pelo Supabase
                        SessionManager.salvar(
                            context     = this@EditarPerfilActivity,
                            userId      = SessionManager.getUserId(this@EditarPerfilActivity),
                            nome        = nome,
                            email       = usuario.email ?: email,
                            accessToken = SessionManager.getAccessToken(this@EditarPerfilActivity) ?: ""
                        )

                        val msg = if (emailMudou) "Perfil atualizado! Confirme o novo e-mail na sua caixa de entrada."
                                  else "Perfil atualizado com sucesso!"
                        Toast.makeText(this@EditarPerfilActivity, msg, Toast.LENGTH_LONG).show()
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this@EditarPerfilActivity, "Erro ao salvar alterações no Supabase", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<SupabaseUser>, t: Throwable) {
                    binding.btnSalvarAlteracoes.isEnabled = true
                    Toast.makeText(this@EditarPerfilActivity, "Sem conexão com o Supabase. Verifique sua internet.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
