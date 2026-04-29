package com.example.smartnotebook.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartnotebook.R
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
        configurarBottomNav()
    }

    // Alterna entre as abas "Tarefa" e "Prova" com feedback visual
    private fun configurarAbas() {
        binding.tabTarefa.setOnClickListener {
            tipoSelecionado = "Tarefa"
            // Tarefa selecionada: fundo branco + texto roxo
            binding.tabTarefa.setBackgroundResource(R.drawable.bg_tab_ativo)
            binding.tabTarefa.setTextColor(getColor(R.color.roxo_primario))
            // Prova não selecionada: sem fundo + texto secundário
            binding.tabProva.background = null
            binding.tabProva.setTextColor(getColor(R.color.texto_secundario))
        }
        binding.tabProva.setOnClickListener {
            tipoSelecionado = "Prova"
            // Prova selecionada: fundo branco + texto roxo
            binding.tabProva.setBackgroundResource(R.drawable.bg_tab_ativo)
            binding.tabProva.setTextColor(getColor(R.color.roxo_primario))
            // Tarefa não selecionada: sem fundo + texto secundário
            binding.tabTarefa.background = null
            binding.tabTarefa.setTextColor(getColor(R.color.texto_secundario))
        }
    }

    // Popula o Spinner com os nomes das matérias do mock e prompt inicial
    private fun configurarSpinnerMaterias() {
        val itens = listOf("Selecione a matéria") + DadosMock.materias.map { it.nome }
        val adapter = android.widget.ArrayAdapter(this,
            android.R.layout.simple_spinner_item, itens)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMateria.adapter = adapter
    }

    // Valida os campos obrigatórios e simula o salvamento da atividade
    private fun configurarBotaoSalvar() {
        binding.btnSalvarAtividade.setOnClickListener {
            val titulo = binding.etTituloAtividade.text.toString().trim()
            val data   = binding.etData.text.toString().trim()

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

    // Configura as 3 abas do BottomNavigationView
    private fun configurarBottomNav() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio -> {
                    // Volta para a tela principal de matérias
                    val intent = Intent(this, MinhasMateriasActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    true
                }
                R.id.nav_calendario -> {
                    startActivity(Intent(this, CalendarioActivity::class.java))
                    true
                }
                R.id.nav_menu -> {
                    startActivity(Intent(this, MenuInstitucionalActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
