package com.example.smartnotebook.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartnotebook.R
import com.example.smartnotebook.SupabaseClient
import com.example.smartnotebook.adapters.AnotacoesAdapter
import com.example.smartnotebook.databinding.ActivityTodasAnotacoesBinding
import com.example.smartnotebook.eq
import com.example.smartnotebook.models.Anotacao
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TELA 10: Todas as Anotações — lista completa de anotações de uma matéria
class TodasAnotacoesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTodasAnotacoesBinding
    private var materiaId = -1

    companion object {
        // Chaves para receber dados da matéria via Intent
        const val EXTRA_MATERIA_ID   = "extra_materia_id"
        const val EXTRA_MATERIA_NOME = "extra_materia_nome"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodasAnotacoesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        materiaId = intent.getIntExtra(EXTRA_MATERIA_ID, -1)
        binding.tvNomeMateriaHeader.text = intent.getStringExtra(EXTRA_MATERIA_NOME) ?: "Anotações"

        carregarAnotacoes()
        configurarBotoes()
        configurarBottomNav()
    }

    override fun onResume() {
        super.onResume()
        // Recarrega ao voltar da tela de nova anotação
        carregarAnotacoes()
    }

    // Preenche o RecyclerView com todas as anotações da matéria
    private fun carregarAnotacoes() {
        if (materiaId == -1) return

        SupabaseClient.restApi.listarAnotacoes(eq(materiaId))
            .enqueue(object : Callback<List<Anotacao>> {
                override fun onResponse(call: Call<List<Anotacao>>, response: Response<List<Anotacao>>) {
                    val lista = response.body() ?: emptyList()
                    val adapter = AnotacoesAdapter(lista) { _ ->
                        // Ao clicar em uma anotação → feedback (futuro: abrir editor)
                        Toast.makeText(this@TodasAnotacoesActivity, "Anotação selecionada", Toast.LENGTH_SHORT).show()
                    }
                    binding.rvTodasAnotacoes.layoutManager = LinearLayoutManager(this@TodasAnotacoesActivity)
                    binding.rvTodasAnotacoes.adapter = adapter
                }
                override fun onFailure(call: Call<List<Anotacao>>, t: Throwable) {
                    Toast.makeText(this@TodasAnotacoesActivity, "Sem conexão com o Supabase. Verifique sua internet.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun configurarBotoes() {
        binding.btnVoltar.setOnClickListener { finish() }

        // FAB (+) → abre tela de nova anotação passando o ID da matéria
        binding.fabNovaAnotacao.setOnClickListener {
            val intent = Intent(this, NovaAnotacaoActivity::class.java)
            intent.putExtra(NovaAnotacaoActivity.EXTRA_MATERIA_ID, materiaId)
            startActivity(intent)
        }

        // Botão de busca — futura funcionalidade
        binding.btnBuscar.setOnClickListener {
            Toast.makeText(this, "Busca em breve", Toast.LENGTH_SHORT).show()
        }

        // Ordenação — futura funcionalidade
        binding.btnOrdenar.setOnClickListener {
            Toast.makeText(this, "Ordenação em breve", Toast.LENGTH_SHORT).show()
        }
    }

    // Configura a navegação pelo BottomNavigationView
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
