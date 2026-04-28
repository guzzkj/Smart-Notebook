package com.example.smartnotebook.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartnotebook.adapters.AnotacoesAdapter
import com.example.smartnotebook.adapters.AtividadesAdapter
import com.example.smartnotebook.databinding.ActivityDetalhesMateriaBinding
import com.example.smartnotebook.models.DadosMock

// TELA 6: Detalhes da Matéria — anotações e atividades de uma matéria específica
class DetalhesMateriaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalhesMateriaBinding

    companion object {
        // Chave usada para passar o ID da matéria via Intent
        const val EXTRA_MATERIA_ID = "extra_materia_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalhesMateriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val materiaId = intent.getIntExtra(EXTRA_MATERIA_ID, -1)
        val materia   = DadosMock.materiaPorId(materiaId)

        if (materia != null) {
            // Exibe o nome da matéria no header
            binding.tvNomeMateriaHeader.text = materia.nome

            carregarAnotacoes(materiaId)
            carregarAtividades(materiaId)
        }

        configurarBotoes(materiaId)
        configurarBottomNav()
    }

    // Preenche o RecyclerView de anotações com as 3 primeiras (prévia)
    private fun carregarAnotacoes(materiaId: Int) {
        val anotacoes = DadosMock.anotacoesDaMateria(materiaId).take(3)

        val adapter = AnotacoesAdapter(anotacoes) { _ ->
            // Clique em uma anotação → futura tela de edição
            Toast.makeText(this, "Anotação selecionada", Toast.LENGTH_SHORT).show()
        }
        binding.rvAnotacoes.layoutManager = LinearLayoutManager(this)
        binding.rvAnotacoes.adapter = adapter
    }

    // Preenche o RecyclerView de atividades da matéria
    private fun carregarAtividades(materiaId: Int) {
        val atividades = DadosMock.atividadesDaMateria(materiaId)
        val adapter    = AtividadesAdapter(atividades)

        binding.rvAtividades.layoutManager = LinearLayoutManager(this)
        binding.rvAtividades.adapter = adapter
        // Divisor entre os itens de atividade dentro do container card
        binding.rvAtividades.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )
    }

    private fun configurarBotoes(materiaId: Int) {
        binding.btnVoltar.setOnClickListener { finish() }

        // "Ver todas" → abre a lista completa de anotações
        binding.tvVerTodasAnotacoes.setOnClickListener {
            val intent = Intent(this, TodasAnotacoesActivity::class.java)
            intent.putExtra(TodasAnotacoesActivity.EXTRA_MATERIA_ID, materiaId)
            startActivity(intent)
        }

        // FAB (+) → abre tela para nova atividade
        binding.fabNovaAcao.setOnClickListener {
            startActivity(Intent(this, NovaAtividadeActivity::class.java))
        }

        // Botão editar
        binding.btnEditar.setOnClickListener {
            Toast.makeText(this, "Edição de matéria em breve", Toast.LENGTH_SHORT).show()
        }

        // Botão excluir — confirmação simples
        binding.btnExcluir.setOnClickListener {
            Toast.makeText(this, "Matéria excluída", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    // Configura a BottomNavigationView com as 3 abas
    private fun configurarBottomNav() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                com.example.smartnotebook.R.id.nav_inicio -> {
                    // Intent explícita para a tela inicial
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
