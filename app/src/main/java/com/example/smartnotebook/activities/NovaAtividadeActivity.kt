package com.example.smartnotebook.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.smartnotebook.R
import com.example.smartnotebook.SupabaseRepository
import com.example.smartnotebook.databinding.ActivityNovaAtividadeBinding
import com.example.smartnotebook.models.Materia
import kotlinx.coroutines.launch

// TELA 9: Nova Atividade — formulário para cadastrar tarefa ou prova
class NovaAtividadeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNovaAtividadeBinding

    // Controla qual tipo de atividade está selecionado (Tarefa ou Prova)
    private var tipoSelecionado = "TAREFA"

    // Lista de matérias carregadas para o Spinner
    private var materias = listOf<Materia>()

    companion object {
        // Chave opcional para pré-selecionar uma matéria via Intent
        const val EXTRA_MATERIA_ID = "extra_materia_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNovaAtividadeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVoltar.setOnClickListener { finish() }
        configurarAbas()
        carregarMateriasSpinner()
        configurarBotaoSalvar()
        configurarBottomNav()
    }

    // Alterna entre as abas "Tarefa" e "Prova" com feedback visual
    private fun configurarAbas() {
        binding.tabTarefa.setOnClickListener {
            tipoSelecionado = "TAREFA"
            // Tarefa selecionada: fundo branco + texto roxo
            binding.tabTarefa.setBackgroundResource(R.drawable.bg_tab_ativo)
            binding.tabTarefa.setTextColor(getColor(R.color.roxo_primario))
            // Prova não selecionada: sem fundo + texto secundário
            binding.tabProva.background = null
            binding.tabProva.setTextColor(getColor(R.color.texto_secundario))
        }
        binding.tabProva.setOnClickListener {
            tipoSelecionado = "PROVA"
            // Prova selecionada: fundo branco + texto roxo
            binding.tabProva.setBackgroundResource(R.drawable.bg_tab_ativo)
            binding.tabProva.setTextColor(getColor(R.color.roxo_primario))
            // Tarefa não selecionada: sem fundo + texto secundário
            binding.tabTarefa.background = null
            binding.tabTarefa.setTextColor(getColor(R.color.texto_secundario))
        }
    }

    // Carrega as matérias do Supabase e popula o Spinner
    private fun carregarMateriasSpinner() {
        lifecycleScope.launch {
            try {
                materias = SupabaseRepository.listarMaterias()
                val itens = listOf("Selecione a matéria") + materias.map { it.nome }
                val adapter = android.widget.ArrayAdapter(
                    this@NovaAtividadeActivity,
                    android.R.layout.simple_spinner_item, itens
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerMateria.adapter = adapter

            } catch (e: Exception) {
                Toast.makeText(this@NovaAtividadeActivity,
                    "Erro ao carregar matérias: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Valida os campos obrigatórios e salva a atividade no Supabase
    private fun configurarBotaoSalvar() {
        binding.btnSalvarAtividade.setOnClickListener {
            val titulo = binding.etTituloAtividade.text.toString().trim()
            val data   = binding.etData.text.toString().trim()
            val posicao = binding.spinnerMateria.selectedItemPosition

            if (titulo.isEmpty()) {
                binding.etTituloAtividade.error = "Informe o título"
                binding.etTituloAtividade.requestFocus()
                return@setOnClickListener
            }
            if (data.isEmpty()) {
                binding.etData.error = "Informe a data (AAAA-MM-DD)"
                binding.etData.requestFocus()
                return@setOnClickListener
            }
            // Posição 0 é o prompt "Selecione a matéria"
            if (posicao == 0 || materias.isEmpty()) {
                Toast.makeText(this, "Selecione uma matéria", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val materiaId = materias[posicao - 1].id

            // Desabilita o botão durante a operação para evitar cliques duplicados
            binding.btnSalvarAtividade.isEnabled = false

            lifecycleScope.launch {
                try {
                    SupabaseRepository.inserirAtividade(
                        materiaId   = materiaId,
                        titulo      = titulo,
                        tipo        = tipoSelecionado,
                        status      = "EM ANDAMENTO",
                        dataEntrega = data,
                        hora        = "23:59"
                    )
                    setResult(RESULT_OK)
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this@NovaAtividadeActivity,
                        "Erro ao salvar atividade: ${e.message}", Toast.LENGTH_SHORT).show()
                } finally {
                    binding.btnSalvarAtividade.isEnabled = true
                }
            }
        }
    }

    // Configura as 3 abas do BottomNavigationView
    private fun configurarBottomNav() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio -> {
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
