package com.example.smartnotebook.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartnotebook.databinding.ActivityNovaMateriaBinding

// TELA 5: Nova Matéria — formulário para cadastrar uma nova matéria acadêmica
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
                    // Deselecionar: volta ao estilo padrão (borda cinza)
                    diasSelecionados.remove(dia)
                    chip.setBackgroundResource(com.example.smartnotebook.R.drawable.bg_chip_dia)
                    chip.setTextColor(getColor(com.example.smartnotebook.R.color.texto_secundario))
                } else {
                    // Selecionar: fundo roxo, texto branco
                    diasSelecionados.add(dia)
                    chip.setBackgroundResource(com.example.smartnotebook.R.drawable.bg_chip_selecionado)
                    chip.setTextColor(getColor(com.example.smartnotebook.R.color.fundo_card))
                }
            }
        }
    }

    // Valida os campos e simula o cadastro da matéria
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

            // Simulação de cadastro — em produção salvaria no banco de dados
            Toast.makeText(this, "Matéria \"$nome\" cadastrada com sucesso!", Toast.LENGTH_SHORT).show()

            // Retorna para a tela de Minhas Matérias
            val intent = Intent(this, MinhasMateriasActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }
}
