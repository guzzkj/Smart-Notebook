package com.example.smartnotebook.activities

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartnotebook.MateriaUpdate
import com.example.smartnotebook.R
import com.example.smartnotebook.SupabaseClient
import com.example.smartnotebook.databinding.ActivityEditarMateriaBinding
import com.example.smartnotebook.eq
import com.example.smartnotebook.models.Materia
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TELA: Editar Matéria — altera nome, professor e dias de aula de uma matéria existente
class EditarMateriaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarMateriaBinding
    private var materiaId = -1

    // Conjunto que guarda os dias de aula selecionados pelo usuário
    private val diasSelecionados = mutableSetOf<String>()

    companion object {
        const val EXTRA_MATERIA_ID        = "extra_materia_id"
        const val EXTRA_MATERIA_NOME      = "extra_materia_nome"
        const val EXTRA_MATERIA_PROFESSOR = "extra_materia_professor"
        const val EXTRA_MATERIA_DIAS      = "extra_materia_dias"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarMateriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        materiaId = intent.getIntExtra(EXTRA_MATERIA_ID, -1)
        val nome      = intent.getStringExtra(EXTRA_MATERIA_NOME) ?: ""
        val professor = intent.getStringExtra(EXTRA_MATERIA_PROFESSOR) ?: ""
        val dias      = intent.getStringArrayListExtra(EXTRA_MATERIA_DIAS) ?: arrayListOf()

        binding.etNomeMateria.setText(nome)
        binding.etProfessor.setText(professor)

        binding.btnVoltar.setOnClickListener { finish() }
        configurarChipsDias(dias)
        configurarBotaoSalvar()
    }

    // Monta o mapa de chips e marca como selecionados os dias já cadastrados
    private fun configurarChipsDias(diasJaSelecionados: List<String>) {
        val chips = mapOf(
            binding.chipSegunda to "Segunda",
            binding.chipTerca   to "Terça",
            binding.chipQuarta  to "Quarta",
            binding.chipQuinta  to "Quinta",
            binding.chipSexta   to "Sexta"
        )

        chips.forEach { (chip, dia) ->
            if (diasJaSelecionados.contains(dia)) {
                diasSelecionados.add(dia)
                marcarChipSelecionado(chip)
            }

            chip.setOnClickListener {
                if (diasSelecionados.contains(dia)) {
                    diasSelecionados.remove(dia)
                    chip.setBackgroundResource(R.drawable.bg_chip_dia)
                    chip.setTextColor(getColor(R.color.texto_secundario))
                } else {
                    diasSelecionados.add(dia)
                    marcarChipSelecionado(chip)
                }
            }
        }
    }

    private fun marcarChipSelecionado(chip: TextView) {
        chip.setBackgroundResource(R.drawable.bg_chip_dia_selecionado)
        chip.setTextColor(getColor(R.color.roxo_primario))
    }

    // Valida os campos e atualiza a matéria no Supabase via PATCH
    private fun configurarBotaoSalvar() {
        binding.btnSalvarEdicaoMateria.setOnClickListener {
            val nome      = binding.etNomeMateria.text.toString().trim()
            val professor = binding.etProfessor.text.toString().trim()

            if (nome.isEmpty()) {
                binding.etNomeMateria.error = "Informe o nome da matéria"
                binding.etNomeMateria.requestFocus()
                return@setOnClickListener
            }
            if (diasSelecionados.isEmpty()) {
                Toast.makeText(this, "Selecione ao menos um dia de aula", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.btnSalvarEdicaoMateria.isEnabled = false

            val body = MateriaUpdate(
                nome      = nome,
                professor = professor,
                diasAula  = diasSelecionados.toList()
            )

            SupabaseClient.restApi.atualizarMateria(eq(materiaId), body).enqueue(object : Callback<List<Materia>> {
                override fun onResponse(call: Call<List<Materia>>, response: Response<List<Materia>>) {
                    binding.btnSalvarEdicaoMateria.isEnabled = true
                    if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this@EditarMateriaActivity, "Erro ao salvar alterações", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<List<Materia>>, t: Throwable) {
                    binding.btnSalvarEdicaoMateria.isEnabled = true
                    Toast.makeText(this@EditarMateriaActivity, "Sem conexão com o Supabase. Verifique sua internet.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
