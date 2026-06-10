package com.example.smartnotebook.activities

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartnotebook.R
import com.example.smartnotebook.SupabaseClient
import com.example.smartnotebook.adapters.AnotacoesAdapter
import com.example.smartnotebook.databinding.ActivityTodasAnotacoesBinding
import com.example.smartnotebook.eq
import com.example.smartnotebook.models.Anotacao
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TELA 10: Todas as Anotações — lista completa de anotações de uma matéria
class TodasAnotacoesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTodasAnotacoesBinding
    private var materiaId = -1

    // Lista completa vinda do Supabase — busca e ordenação operam sobre uma cópia filtrada dela
    private var listaCompleta: List<Anotacao> = emptyList()
    private var filtroBusca = ""
    private var ordemAtual = Ordem.MAIS_RECENTES

    private enum class Ordem { MAIS_RECENTES, MAIS_ANTIGAS, TITULO_AZ }

    companion object {
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
                    listaCompleta = response.body() ?: emptyList()
                    exibirLista()
                }
                override fun onFailure(call: Call<List<Anotacao>>, t: Throwable) {
                    Toast.makeText(this@TodasAnotacoesActivity, "Sem conexão com o Supabase. Verifique sua internet.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // Aplica o filtro de busca e a ordenação atuais sobre listaCompleta e atualiza o RecyclerView
    private fun exibirLista() {
        var lista = if (filtroBusca.isBlank()) {
            listaCompleta
        } else {
            listaCompleta.filter { it.titulo.contains(filtroBusca, ignoreCase = true) }
        }

        lista = when (ordemAtual) {
            Ordem.MAIS_RECENTES -> lista.sortedByDescending { it.createdAt }
            Ordem.MAIS_ANTIGAS  -> lista.sortedBy { it.createdAt }
            Ordem.TITULO_AZ     -> lista.sortedBy { it.titulo.lowercase() }
        }

        val adapter = AnotacoesAdapter(
            lista,
            aoClicar = { anotacao -> abrirEdicaoAnotacao(anotacao) },
            aoExcluir = { anotacao -> confirmarExclusao(anotacao) }
        )
        binding.rvTodasAnotacoes.layoutManager = LinearLayoutManager(this@TodasAnotacoesActivity)
        binding.rvTodasAnotacoes.adapter = adapter
    }

    // Abre o editor já preenchido com os dados da anotação selecionada
    private fun abrirEdicaoAnotacao(anotacao: Anotacao) {
        val intent = Intent(this, NovaAnotacaoActivity::class.java)
        intent.putExtra(NovaAnotacaoActivity.EXTRA_MATERIA_ID, materiaId)
        intent.putExtra(NovaAnotacaoActivity.EXTRA_ANOTACAO_ID, anotacao.id)
        intent.putExtra(NovaAnotacaoActivity.EXTRA_ANOTACAO_TITULO, anotacao.titulo)
        intent.putExtra(NovaAnotacaoActivity.EXTRA_ANOTACAO_CONTEUDO, anotacao.conteudo)
        startActivity(intent)
    }

    // Pede confirmação antes de excluir — toque longo no item dispara este fluxo
    private fun confirmarExclusao(anotacao: Anotacao) {
        AlertDialog.Builder(this)
            .setTitle("Excluir anotação")
            .setMessage("Tem certeza que deseja excluir \"${anotacao.titulo}\"? Esta ação não pode ser desfeita.")
            .setPositiveButton("Excluir") { _, _ -> excluirAnotacao(anotacao) }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // Remove a anotação no Supabase (RLS garante que só o dono apaga) e recarrega a lista
    private fun excluirAnotacao(anotacao: Anotacao) {
        SupabaseClient.restApi.excluirAnotacao(eq(anotacao.id))
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@TodasAnotacoesActivity, "Anotação excluída", Toast.LENGTH_SHORT).show()
                        carregarAnotacoes()
                    } else {
                        Toast.makeText(this@TodasAnotacoesActivity, "Erro ao excluir anotação", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@TodasAnotacoesActivity, "Sem conexão com o Supabase. Verifique sua internet.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // Mostra um diálogo com campo de texto para filtrar as anotações pelo título
    private fun abrirDialogoBusca() {
        val campo = EditText(this)
        campo.hint = "Buscar por título..."
        campo.setText(filtroBusca)
        val padding = (16 * resources.displayMetrics.density).toInt()
        campo.setPadding(padding, padding, padding, padding)

        AlertDialog.Builder(this)
            .setTitle("Buscar anotação")
            .setView(campo)
            .setPositiveButton("Buscar") { _, _ ->
                filtroBusca = campo.text.toString().trim()
                exibirLista()
            }
            .setNegativeButton("Limpar") { _, _ ->
                filtroBusca = ""
                exibirLista()
            }
            .show()
    }

    // Mostra um menu com as opções de ordenação da lista
    private fun abrirMenuOrdenacao() {
        val menu = PopupMenu(this, binding.btnOrdenar)
        menu.menu.add(0, 0, 0, "Mais recentes primeiro")
        menu.menu.add(0, 1, 1, "Mais antigas primeiro")
        menu.menu.add(0, 2, 2, "Título (A-Z)")

        menu.setOnMenuItemClickListener { item ->
            ordemAtual = when (item.itemId) {
                1 -> Ordem.MAIS_ANTIGAS
                2 -> Ordem.TITULO_AZ
                else -> Ordem.MAIS_RECENTES
            }
            exibirLista()
            true
        }
        menu.show()
    }

    private fun configurarBotoes() {
        binding.btnVoltar.setOnClickListener { finish() }

        // FAB (+) → abre tela de nova anotação passando o ID da matéria
        binding.fabNovaAnotacao.setOnClickListener {
            val intent = Intent(this, NovaAnotacaoActivity::class.java)
            intent.putExtra(NovaAnotacaoActivity.EXTRA_MATERIA_ID, materiaId)
            startActivity(intent)
        }

        // Botão de busca — abre diálogo para filtrar pelo título
        binding.btnBuscar.setOnClickListener { abrirDialogoBusca() }

        // Ordenação — abre menu com as opções de ordenação da lista
        binding.btnOrdenar.setOnClickListener { abrirMenuOrdenacao() }
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
