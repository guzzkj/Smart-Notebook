package com.example.smartnotebook.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartnotebook.SupabaseRepository
import com.example.smartnotebook.adapters.AnotacoesAdapter
import com.example.smartnotebook.adapters.AtividadesAdapter
import com.example.smartnotebook.databinding.ActivityDetalhesMateriaBinding
import kotlinx.coroutines.launch

// TELA 6: Detalhes da Matéria — anotações e atividades de uma matéria específica
class DetalhesMateriaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalhesMateriaBinding
    private var materiaId = ""

    companion object {
        // Chaves usadas para passar dados da matéria via Intent
        const val EXTRA_MATERIA_ID   = "extra_materia_id"
        const val EXTRA_MATERIA_NOME = "extra_materia_nome"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalhesMateriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        materiaId = intent.getStringExtra(EXTRA_MATERIA_ID) ?: ""
        val materiaNome = intent.getStringExtra(EXTRA_MATERIA_NOME) ?: "Matéria"

        // Exibe o nome da matéria no header
        binding.tvNomeMateriaHeader.text = materiaNome

        if (materiaId.isNotEmpty()) {
            carregarAnotacoes()
            carregarAtividades()
        }

        configurarBotoes()
        configurarBottomNav()
    }

    // Preenche o RecyclerView de anotações com as 3 primeiras (prévia)
    private fun carregarAnotacoes() {
        lifecycleScope.launch {
            try {
                val anotacoes = SupabaseRepository.listarAnotacoes(materiaId).take(3)

                val adapter = AnotacoesAdapter(anotacoes) { _ ->
                    // Clique em uma anotação → futura tela de edição
                    Toast.makeText(this@DetalhesMateriaActivity,
                        "Anotação selecionada", Toast.LENGTH_SHORT).show()
                }
                binding.rvAnotacoes.layoutManager = LinearLayoutManager(this@DetalhesMateriaActivity)
                binding.rvAnotacoes.adapter = adapter

            } catch (e: Exception) {
                Toast.makeText(this@DetalhesMateriaActivity,
                    "Erro ao carregar anotações: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Preenche o RecyclerView de atividades da matéria
    private fun carregarAtividades() {
        lifecycleScope.launch {
            try {
                val atividades = SupabaseRepository.listarAtividades(materiaId)
                val adapter    = AtividadesAdapter(atividades)

                binding.rvAtividades.layoutManager = LinearLayoutManager(this@DetalhesMateriaActivity)
                binding.rvAtividades.adapter = adapter
                // Divisor entre os itens de atividade dentro do container card
                binding.rvAtividades.addItemDecoration(
                    DividerItemDecoration(this@DetalhesMateriaActivity, LinearLayoutManager.VERTICAL)
                )
            } catch (e: Exception) {
                Toast.makeText(this@DetalhesMateriaActivity,
                    "Erro ao carregar atividades: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun configurarBotoes() {
        binding.btnVoltar.setOnClickListener { finish() }

        // "Ver todas" → abre a lista completa de anotações
        binding.tvVerTodasAnotacoes.setOnClickListener {
            val intent = Intent(this, TodasAnotacoesActivity::class.java)
            intent.putExtra(TodasAnotacoesActivity.EXTRA_MATERIA_ID, materiaId)
            intent.putExtra(TodasAnotacoesActivity.EXTRA_MATERIA_NOME, binding.tvNomeMateriaHeader.text.toString())
            startActivity(intent)
        }

        // FAB (+) → abre tela para nova atividade
        binding.fabNovaAcao.setOnClickListener {
            val intent = Intent(this, NovaAtividadeActivity::class.java)
            intent.putExtra(NovaAtividadeActivity.EXTRA_MATERIA_ID, materiaId)
            startActivity(intent)
        }

        // Botão editar
        binding.btnEditar.setOnClickListener {
            Toast.makeText(this, "Edição de matéria em breve", Toast.LENGTH_SHORT).show()
        }

        // Botão excluir — exclui a matéria no Supabase e volta para a tela anterior
        binding.btnExcluir.setOnClickListener {
            binding.btnExcluir.isEnabled = false

            lifecycleScope.launch {
                try {
                    SupabaseRepository.excluirMateria(materiaId)
                    Toast.makeText(this@DetalhesMateriaActivity,
                        "Matéria excluída", Toast.LENGTH_SHORT).show()
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this@DetalhesMateriaActivity,
                        "Erro ao excluir matéria: ${e.message}", Toast.LENGTH_SHORT).show()
                    binding.btnExcluir.isEnabled = true
                }
            }
        }
    }

    // Configura a BottomNavigationView com as 3 abas
    private fun configurarBottomNav() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                com.example.smartnotebook.R.id.nav_inicio -> {
                    val intent = Intent(this, MinhasMateriasActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    true
                }
                com.example.smartnotebook.R.id.nav_calendario -> {
                    startActivity(Intent(this, CalendarioActivity::class.java))
                    true
                }
                com.example.smartnotebook.R.id.nav_menu -> {
                    startActivity(Intent(this, MenuInstitucionalActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
