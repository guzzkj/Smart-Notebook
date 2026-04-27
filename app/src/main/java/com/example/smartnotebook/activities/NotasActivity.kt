package com.example.smartnotebook.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartnotebook.adapters.AnotacoesAdapter
import com.example.smartnotebook.databinding.ActivityTodasAnotacoesBinding
import com.example.smartnotebook.models.DadosMock

// TELA 7: Todas as Anotações — lista completa de anotações de uma matéria
class TodasAnotacoesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTodasAnotacoesBinding

    companion object {
        // Chave para receber o ID da matéria via Intent
        const val EXTRA_MATERIA_ID = "extra_materia_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodasAnotacoesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val materiaId = intent.getIntExtra(EXTRA_MATERIA_ID, -1)
        val materia   = DadosMock.materiaPorId(materiaId)

        // Exibe o nome da matéria no header
        binding.tvNomeMateriaHeader.text = materia?.nome ?: "Anotações"

        carregarAnotacoes(materiaId)
        configurarBotoes()
    }

    // Preenche o RecyclerView com todas as anotações da matéria
    private fun carregarAnotacoes(materiaId: Int) {
        val lista = if (materiaId != -1) DadosMock.anotacoesDaMateria(materiaId)
                    else                  DadosMock.anotacoes

        val adapter = AnotacoesAdapter(lista) { _ ->
            // Ao clicar em uma anotação → feedback (futuro: abrir editor)
            Toast.makeText(this, "Anotação selecionada", Toast.LENGTH_SHORT).show()
        }
        binding.rvTodasAnotacoes.layoutManager = LinearLayoutManager(this)
        binding.rvTodasAnotacoes.adapter = adapter
    }

    private fun configurarBotoes() {
        binding.btnVoltar.setOnClickListener { finish() }

        // FAB (+) → abre tela de nova anotação
        binding.fabNovaAnotacao.setOnClickListener {
            startActivity(Intent(this, NovaAnotacaoActivity::class.java))
        }

        // Botão de busca — futura funcionalidade
        binding.btnBuscar.setOnClickListener {
            Toast.makeText(this, "Busca em breve", Toast.LENGTH_SHORT).show()
        }
    }
}
