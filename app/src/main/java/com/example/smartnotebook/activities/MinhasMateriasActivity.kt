package com.example.smartnotebook.activities

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartnotebook.R
import com.example.smartnotebook.SessionManager
import com.example.smartnotebook.SupabaseClient
import com.example.smartnotebook.adapters.MateriasAdapter
import com.example.smartnotebook.eq
import com.example.smartnotebook.databinding.ActivityMinhasMateriasBinding
import com.example.smartnotebook.models.Materia
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TELA 4: Minhas Matérias — tela principal exibida após o login
class MinhasMateriasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMinhasMateriasBinding

    // Lista completa vinda do Supabase — a busca filtra uma cópia dela sem refazer a requisição
    private var listaCompleta: List<Materia> = emptyList()
    private var filtroBusca = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMinhasMateriasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarBottomNav()
        configurarBotoes()
    }

    override fun onResume() {
        super.onResume()
        // Recarrega a lista sempre que a tela volta ao foco (ex: após cadastrar matéria)
        carregarMaterias()
    }

    // Busca matérias do Supabase (view materias_com_pendentes já traz "pendentes" calculado, sem N+1 calls)
    private fun carregarMaterias() {
        val userId = SessionManager.getUserId(this)

        SupabaseClient.restApi.listarMaterias(eq(userId))
            .enqueue(object : Callback<List<Materia>> {
                override fun onResponse(call: Call<List<Materia>>, response: Response<List<Materia>>) {
                    listaCompleta = response.body() ?: emptyList()
                    exibirLista()
                }
                override fun onFailure(call: Call<List<Materia>>, t: Throwable) {
                    Toast.makeText(this@MinhasMateriasActivity, "Sem conexão com o Supabase. Verifique sua internet.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // Aplica o filtro de busca atual sobre listaCompleta e atualiza o RecyclerView
    private fun exibirLista() {
        val lista = if (filtroBusca.isBlank()) {
            listaCompleta
        } else {
            listaCompleta.filter { it.nome.contains(filtroBusca, ignoreCase = true) }
        }

        val adapter = MateriasAdapter(lista) { materia ->
            // Intent explícita ao clicar em uma matéria → abre os Detalhes
            val intent = Intent(this@MinhasMateriasActivity, DetalhesMateriaActivity::class.java)
            intent.putExtra(DetalhesMateriaActivity.EXTRA_MATERIA_ID, materia.id)
            intent.putExtra(DetalhesMateriaActivity.EXTRA_MATERIA_NOME, materia.nome)
            startActivity(intent)
        }
        binding.rvMaterias.layoutManager = LinearLayoutManager(this@MinhasMateriasActivity)
        binding.rvMaterias.adapter = adapter
    }

    // Mostra um diálogo com campo de texto para filtrar as matérias pelo nome
    private fun abrirDialogoBusca() {
        val campo = EditText(this)
        campo.hint = "Buscar por nome da matéria..."
        campo.setText(filtroBusca)
        val padding = (16 * resources.displayMetrics.density).toInt()
        campo.setPadding(padding, padding, padding, padding)

        AlertDialog.Builder(this)
            .setTitle("Buscar matéria")
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

    // Configura as 3 abas do BottomNavigationView
    private fun configurarBottomNav() {
        binding.bottomNav.selectedItemId = R.id.nav_inicio

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio    -> true // já está na Home
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

    private fun configurarBotoes() {
        // FAB (+) → abre tela para cadastrar nova matéria
        binding.fabNovaMateria.setOnClickListener {
            startActivity(Intent(this, NovaMateriaActivity::class.java))
        }

        // Busca no header → abre diálogo para filtrar matérias pelo nome
        binding.btnBuscarHeader.setOnClickListener { abrirDialogoBusca() }
    }
}
