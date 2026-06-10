package com.example.smartnotebook.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartnotebook.MateriaInsert
import com.example.smartnotebook.SessionManager
import com.example.smartnotebook.SupabaseClient
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
                    diasSelecionados.remove(dia)
                    chip.setBackgroundResource(com.example.smartnotebook.R.drawable.bg_chip_dia)
                    chip.setTextColor(getColor(com.example.smartnotebook.R.color.texto_secundario))
                } else {
                    diasSelecionados.add(dia)
                    chip.setBackgroundResource(com.example.smartnotebook.R.drawable.bg_chip_dia_selecionado)
                    chip.setTextColor(getColor(com.example.smartnotebook.R.color.roxo_primario))
                }
            }
        }
    }

    // Valida os campos e cadastra a matéria no Supabase (coluna dias_aula é text[])
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

            val userId = SessionManager.getUserId(this)

            val body = MateriaInsert(
                userId    = userId,
                nome      = nome,
                professor = "",
                diasAula  = diasSelecionados.toList(),
                corHex    = "#5C6BC0"
            )

            SupabaseClient.restApi.inserirMateria(body).enqueue(object : Callback<List<Materia>> {
                override fun onResponse(call: Call<List<Materia>>, response: Response<List<Materia>>) {
                    binding.btnCadastrarMateria.isEnabled = true
                    if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this@NovaMateriaActivity, "Erro ao cadastrar matéria", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<List<Materia>>, t: Throwable) {
                    binding.btnCadastrarMateria.isEnabled = true
                    Toast.makeText(this@NovaMateriaActivity, "Sem conexão com o Supabase. Verifique sua internet.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
