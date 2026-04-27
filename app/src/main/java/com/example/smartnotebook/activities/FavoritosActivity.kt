package com.example.smartnotebook.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartnotebook.databinding.ActivityNovaAtividadeBinding
import com.example.smartnotebook.models.DadosMock

// TELA 9: Nova Atividade — formulário para cadastrar tarefa ou prova
class NovaAtividadeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNovaAtividadeBinding

    // Controla qual tipo de atividade está selecionado (Tarefa ou Prova)
    private var tipoSelecionado = "Tarefa"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNovaAtividadeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVoltar.setOnClickListener { finish() }
        configurarAbas()
        configurarSpinnerMaterias()
        configurarBotaoSalvar()
    }

    // Alterna entre as abas "Tarefa" e "Prova" com feedback visual
    private fun configurarAbas() {
        binding.tabTarefa.setOnClickListener {
            tipoSelecionado = "Tarefa"
            binding.tabTarefa.setBackgroundResource(com.example.smartnotebook.R.drawable.bg_chip_selecionado)
            binding.tabTarefa.setTextColor(getColor(com.example.smartnotebook.R.color.fundo_card))
            binding.tabProva.background = null
            binding.tabProva.setTextColor(getColor(com.example.smartnotebook.R.color.texto_secundario))
        }
        binding.tabProva.setOnClickListener {
            tipoSelecionado = "Prova"
            binding.tabProva.setBackgroundResource(com.example.smartnotebook.R.drawable.bg_chip_selecionado)
            binding.tabProva.setTextColor(getColor(com.example.smartnotebook.R.color.fundo_card))
            binding.tabTarefa.background = null
            binding.tabTarefa.setTextColor(getColor(com.example.smartnotebook.R.color.texto_secundario))
        }
    }

    // Popula o Spinner com os nomes das matérias do mock
    private fun configurarSpinnerMaterias() {
        val nomes = DadosMock.materias.map { it.nome }
        val adapter = android.widget.ArrayAdapter(this,
            android.R.layout.simple_spinner_item, nomes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMateria.adapter = adapter
    }

    // Valida os campos e simula o salvamento da atividade
    private fun configurarBotaoSalvar() {
        binding.btnSalvarAtividade.setOnClickListener {
            val titulo = binding.etTituloAtividade.text.toString().trim()
            val data   = binding.etData.text.toString().trim()
            val hora   = binding.etHora.text.toString().trim()

            if (titulo.isEmpty()) {
                binding.etTituloAtividade.error = "Informe o título"
                binding.etTituloAtividade.requestFocus()
                return@setOnClickListener
            }
            if (data.isEmpty()) {
                binding.etData.error = "Informe a data"
                binding.etData.requestFocus()
                return@setOnClickListener
            }

            // Simulação — em produção salvaria no banco de dados
            Toast.makeText(this, "$tipoSelecionado \"$titulo\" salva!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
