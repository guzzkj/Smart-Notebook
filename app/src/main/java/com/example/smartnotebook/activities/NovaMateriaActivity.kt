package com.example.smartnotebook.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartnotebook.RetrofitClient
import com.example.smartnotebook.SessionManager
import com.example.smartnotebook.databinding.ActivityNovaMateriaBinding
import com.example.smartnotebook.models.Materia
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TELA 7: Nova Matéria — formulário para cadastrar uma nova matéria acadêmica
class NovaMateriaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNovaMateriaBinding

    // Conjunto que guarda os dias de aula selecionados pelo usuário
    private val diasSelecionados = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNovaMateriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVoltar.setOnClickListener { finish() }
        configurarChipsDias()
        configurarBotaoCadastrar()
    }

    // Alterna o estado visual dos chips de dias da semana ao clicar
    private fun configurarChipsDias() {
        val chips = mapOf(
            binding.chipSegunda to "Segunda",
            binding.chipTerca   to "Terça",
            binding.chipQuarta  to "Quarta",
            binding.chipQuinta  to "Quinta",
            binding.chipSexta   to "Sexta"
        )

        chips.forEach { (chip, dia) ->
            chip.setOnClickListener {
                if (diasSelecionados.contains(dia)) {
                    // Deselecionar: borda cinza, fundo branco, texto cinza
                    diasSelecionados.remove(dia)
                    chip.setBackgroundResource(com.example.smartnotebook.R.drawable.bg_chip_dia)
                    chip.setTextColor(getColor(com.example.smartnotebook.R.color.texto_secundario))
                } else {
                    // Selecionar: borda roxa, fundo roxo claro, texto roxo
                    diasSelecionados.add(dia)
                    chip.setBackgroundResource(com.example.smartnotebook.R.drawable.bg_chip_dia_selecionado)
                    chip.setTextColor(getColor(com.example.smartnotebook.R.color.roxo_primario))
                }
            }
        }
    }

    // Valida os campos e cadastra a matéria via PHP/MySQL
    private fun configurarBotaoCadastrar() {
        binding.btnCadastrarMateria.setOnClickListener {
            val nome = binding.etNomeMateria.text.toString().trim()

            if (nome.isEmpty()) {
                binding.etNomeMateria.error = "Informe o nome da matéria"
                binding.etNomeMateria.requestFocus()
                return@setOnClickListener
            }
            if (diasSelecionados.isEmpty()) {
                Toast.makeText(this, "Selecione ao menos um dia de aula", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Desabilita o botão durante a operação para evitar cliques duplicados
            binding.btnCadastrarMateria.isEnabled = false

            // Dias enviados como string separada por vírgula — PHP faz o split e armazena
            val diasAula = diasSelecionados.joinToString(",")
            val userId   = SessionManager.getUserId(this)

            RetrofitClient.apiService.inserirMateria(
                userId    = userId,
                nome      = nome,
                professor = "",
                diasAula  = diasAula,
                corHex    = "#5C6BC0"
            ).enqueue(object : Callback<Materia> {
                override fun onResponse(call: Call<Materia>, response: Response<Materia>) {
                    binding.btnCadastrarMateria.isEnabled = true
                    if (response.isSuccessful && response.body() != null) {
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this@NovaMateriaActivity, "Erro ao cadastrar matéria", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Materia>, t: Throwable) {
                    binding.btnCadastrarMateria.isEnabled = true
                    Toast.makeText(this@NovaMateriaActivity, "Sem conexão. Verifique se o XAMPP está ativo.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
