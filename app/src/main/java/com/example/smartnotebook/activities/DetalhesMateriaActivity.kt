package com.example.smartnotebook.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartnotebook.SupabaseClient
import com.example.smartnotebook.adapters.AnotacoesAdapter
import com.example.smartnotebook.adapters.AtividadesAdapter
import com.example.smartnotebook.databinding.ActivityDetalhesMateriaBinding
import com.example.smartnotebook.eq
import com.example.smartnotebook.models.Anotacao
import com.example.smartnotebook.models.Atividade
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TELA 6: Detalhes da Matéria — anotações e atividades de uma matéria específica
class DetalhesMateriaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalhesMateriaBinding
    private var materiaId = -1

    companion object {
        // Chaves usadas para passar dados da matéria via Intent
        const val EXTRA_MATERIA_ID   = "extra_materia_id"
        const val EXTRA_MATERIA_NOME = "extra_materia_nome"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalhesMateriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        materiaId = intent.getIntExtra(EXTRA_MATERIA_ID, -1)
        val materiaNome = intent.getStringExtra(EXTRA_MATERIA_NOME) ?: "Matéria"

        // Exibe o nome da matéria no header
        binding.tvNomeMateriaHeader.text = materiaNome

        if (materiaId != -1) {
            carregarAnotacoes()
            carregarAtividades()
        }

        configurarBotoes()
        configurarBottomNav()
    }

    // Preenche o RecyclerView de anotações com as 3 primeiras (prévia)
    private fun carregarAnotacoes() {
        SupabaseClient.restApi.listarAnotacoes(eq(materiaId))
            .enqueue(object : Callback<List<Anotacao>> {
                override fun onResponse(call: Call<List<Anotacao>>, response: Response<List<Anotacao>>) {
                    val anotacoes = (response.body() ?: emptyList()).take(3)
                    val adapter = AnotacoesAdapter(anotacoes) { _ ->
                        // Clique em uma anotação → futura tela de edição
                        Toast.makeText(this@DetalhesMateriaActivity, "Anotação selecionada", Toast.LENGTH_SHORT).show()
                    }
                    binding.rvAnotacoes.layoutManager = LinearLayoutManager(this@DetalhesMateriaActivity)
                    binding.rvAnotacoes.adapter = adapter
                }
                override fun onFailure(call: Call<List<Anotacao>>, t: Throwable) {
                    Toast.makeText(this@DetalhesMateriaActivity, "Sem conexão com o Supabase. Verifique sua internet.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // Preenche o RecyclerView de atividades da matéria
    private fun carregarAtividades() {
        SupabaseClient.restApi.listarAtividades(eq(materiaId))
            .enqueue(object : Callback<List<Atividade>> {
                override fun onResponse(call: Call<List<Atividade>>, response: Response<List<Atividade>>) {
                    val atividades = response.body() ?: emptyList()
                    val adapter    = AtividadesAdapter(atividades)
                    binding.rvAtividades.layoutManager = LinearLayoutManager(this@DetalhesMateriaActivity)
                    binding.rvAtividades.adapter = adapter
                    // Divisor entre os itens de atividade dentro do container card
                    binding.rvAtividades.addItemDecoration(
                        DividerItemDecoration(this@DetalhesMateriaActivity, LinearLayoutManager.VERTICAL)
                    )
                }
                override fun onFailure(call: Call<List<Atividade>>, t: Throwable) {
                    Toast.makeText(this@DetalhesMateriaActivity, "Sem conexão com o Supabase. Verifique sua internet.", Toast.LENGTH_SHORT).show()
                }
            })
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

        // Botão excluir — exclui a matéria no Supabase (RLS garante que só o dono apaga) e volta para a tela anterior
        binding.btnExcluir.setOnClickListener {
            binding.btnExcluir.isEnabled = false

            SupabaseClient.restApi.excluirMateria(eq(materiaId))
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@DetalhesMateriaActivity, "Matéria excluída", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@DetalhesMateriaActivity, "Erro ao excluir matéria", Toast.LENGTH_SHORT).show()
                            binding.btnExcluir.isEnabled = true
                        }
                    }
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(this@DetalhesMateriaActivity, "Sem conexão com o Supabase. Verifique sua internet.", Toast.LENGTH_SHORT).show()
                        binding.btnExcluir.isEnabled = true
                    }
                })
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
