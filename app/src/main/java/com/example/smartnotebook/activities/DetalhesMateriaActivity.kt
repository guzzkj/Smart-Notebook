package com.example.smartnotebook.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
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

    // Recarrega os dados da matéria quando a edição é concluída com sucesso
    private val editarMateriaLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            recarregarNomeMateria()
        }
    }

    companion object {
        const val EXTRA_MATERIA_ID   = "extra_materia_id"
        const val EXTRA_MATERIA_NOME = "extra_materia_nome"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalhesMateriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        materiaId = intent.getIntExtra(EXTRA_MATERIA_ID, -1)
        val materiaNome = intent.getStringExtra(EXTRA_MATERIA_NOME) ?: "Matéria"

        binding.tvNomeMateriaHeader.text = materiaNome

        if (materiaId != -1) {
            carregarAnotacoes()
            carregarAtividades()
        }

        configurarBotoes()
        configurarBottomNav()
    }

    override fun onResume() {
        super.onResume()
        // Recarrega anotações e atividades ao voltar de criar/editar/excluir algo
        if (materiaId != -1) {
            carregarAnotacoes()
            carregarAtividades()
        }
    }

    // Preenche o RecyclerView de anotações com as 3 primeiras (prévia)
    private fun carregarAnotacoes() {
        SupabaseClient.restApi.listarAnotacoes(eq(materiaId))
            .enqueue(object : Callback<List<Anotacao>> {
                override fun onResponse(call: Call<List<Anotacao>>, response: Response<List<Anotacao>>) {
                    val anotacoes = (response.body() ?: emptyList()).take(3)
                    val adapter = AnotacoesAdapter(anotacoes, aoClicar = { anotacao ->
                        val intent = Intent(this@DetalhesMateriaActivity, NovaAnotacaoActivity::class.java)
                        intent.putExtra(NovaAnotacaoActivity.EXTRA_MATERIA_ID, materiaId)
                        intent.putExtra(NovaAnotacaoActivity.EXTRA_ANOTACAO_ID, anotacao.id)
                        intent.putExtra(NovaAnotacaoActivity.EXTRA_ANOTACAO_TITULO, anotacao.titulo)
                        intent.putExtra(NovaAnotacaoActivity.EXTRA_ANOTACAO_CONTEUDO, anotacao.conteudo)
                        startActivity(intent)
                    })
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
                    val adapter    = AtividadesAdapter(
                        atividades,
                        aoClicar  = { atividade -> abrirEdicaoAtividade(atividade) },
                        aoExcluir = { atividade -> confirmarExclusaoAtividade(atividade) }
                    )
                    binding.rvAtividades.layoutManager = LinearLayoutManager(this@DetalhesMateriaActivity)
                    binding.rvAtividades.adapter = adapter
                    binding.rvAtividades.addItemDecoration(
                        DividerItemDecoration(this@DetalhesMateriaActivity, LinearLayoutManager.VERTICAL)
                    )
                }
                override fun onFailure(call: Call<List<Atividade>>, t: Throwable) {
                    Toast.makeText(this@DetalhesMateriaActivity, "Sem conexão com o Supabase. Verifique sua internet.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // Abre o editor de atividade já preenchido com os dados da atividade selecionada
    private fun abrirEdicaoAtividade(atividade: Atividade) {
        val intent = Intent(this, NovaAtividadeActivity::class.java)
        intent.putExtra(NovaAtividadeActivity.EXTRA_MATERIA_ID, materiaId)
        intent.putExtra(NovaAtividadeActivity.EXTRA_ATIVIDADE_ID, atividade.id)
        intent.putExtra(NovaAtividadeActivity.EXTRA_ATIVIDADE_TITULO, atividade.titulo)
        intent.putExtra(NovaAtividadeActivity.EXTRA_ATIVIDADE_TIPO, atividade.tipo)
        intent.putExtra(NovaAtividadeActivity.EXTRA_ATIVIDADE_STATUS, atividade.status)
        intent.putExtra(NovaAtividadeActivity.EXTRA_ATIVIDADE_DATA, atividade.dataEntrega)
        intent.putExtra(NovaAtividadeActivity.EXTRA_ATIVIDADE_HORA, atividade.hora)
        startActivity(intent)
    }

    // Pede confirmação antes de excluir — toque longo no item dispara este fluxo
    private fun confirmarExclusaoAtividade(atividade: Atividade) {
        AlertDialog.Builder(this)
            .setTitle("Excluir atividade")
            .setMessage("Tem certeza que deseja excluir \"${atividade.titulo}\"? Esta ação não pode ser desfeita.")
            .setPositiveButton("Excluir") { _, _ -> excluirAtividade(atividade) }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // Remove a atividade no Supabase (RLS garante que só o dono apaga) e recarrega a lista
    private fun excluirAtividade(atividade: Atividade) {
        SupabaseClient.restApi.excluirAtividade(eq(atividade.id))
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@DetalhesMateriaActivity, "Atividade excluída", Toast.LENGTH_SHORT).show()
                        carregarAtividades()
                    } else {
                        Toast.makeText(this@DetalhesMateriaActivity, "Erro ao excluir atividade", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@DetalhesMateriaActivity, "Sem conexão com o Supabase. Verifique sua internet.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // Atualiza o nome exibido no header após a edição da matéria
    private fun recarregarNomeMateria() {
        SupabaseClient.restApi.buscarMateria(eq(materiaId)).enqueue(object : Callback<List<com.example.smartnotebook.models.Materia>> {
            override fun onResponse(
                call: Call<List<com.example.smartnotebook.models.Materia>>,
                response: Response<List<com.example.smartnotebook.models.Materia>>
            ) {
                response.body()?.firstOrNull()?.let { materia ->
                    binding.tvNomeMateriaHeader.text = materia.nome
                }
            }
            override fun onFailure(call: Call<List<com.example.smartnotebook.models.Materia>>, t: Throwable) {
                // Silencioso: o nome no header só será atualizado na próxima abertura da tela
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

        // Botão editar — busca os dados completos da matéria e abre a tela de edição
        binding.btnEditar.setOnClickListener {
            binding.btnEditar.isEnabled = false

            SupabaseClient.restApi.buscarMateria(eq(materiaId)).enqueue(object : Callback<List<com.example.smartnotebook.models.Materia>> {
                override fun onResponse(
                    call: Call<List<com.example.smartnotebook.models.Materia>>,
                    response: Response<List<com.example.smartnotebook.models.Materia>>
                ) {
                    binding.btnEditar.isEnabled = true
                    val materia = response.body()?.firstOrNull()
                    if (response.isSuccessful && materia != null) {
                        val intent = Intent(this@DetalhesMateriaActivity, EditarMateriaActivity::class.java)
                        intent.putExtra(EditarMateriaActivity.EXTRA_MATERIA_ID, materiaId)
                        intent.putExtra(EditarMateriaActivity.EXTRA_MATERIA_NOME, materia.nome)
                        intent.putExtra(EditarMateriaActivity.EXTRA_MATERIA_PROFESSOR, materia.professor)
                        intent.putStringArrayListExtra(EditarMateriaActivity.EXTRA_MATERIA_DIAS, ArrayList(materia.diasAula))
                        editarMateriaLauncher.launch(intent)
                    } else {
                        Toast.makeText(this@DetalhesMateriaActivity, "Erro ao carregar dados da matéria", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<List<com.example.smartnotebook.models.Materia>>, t: Throwable) {
                    binding.btnEditar.isEnabled = true
                    Toast.makeText(this@DetalhesMateriaActivity, "Sem conexão com o Supabase. Verifique sua internet.", Toast.LENGTH_SHORT).show()
                }
            })
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
