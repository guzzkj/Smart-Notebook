package com.example.smartnotebook.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
                    val materias = response.body() ?: emptyList()
                    val adapter = MateriasAdapter(materias) { materia ->
                        // Intent explícita ao clicar em uma matéria → abre os Detalhes
                        val intent = Intent(this@MinhasMateriasActivity, DetalhesMateriaActivity::class.java)
                        intent.putExtra(DetalhesMateriaActivity.EXTRA_MATERIA_ID, materia.id)
                        intent.putExtra(DetalhesMateriaActivity.EXTRA_MATERIA_NOME, materia.nome)
                        startActivity(intent)
                    }
                    binding.rvMaterias.layoutManager = LinearLayoutManager(this@MinhasMateriasActivity)
                    binding.rvMaterias.adapter = adapter
                }
                override fun onFailure(call: Call<List<Materia>>, t: Throwable) {
                    Toast.makeText(this@MinhasMateriasActivity, "Sem conexão com o Supabase. Verifique sua internet.", Toast.LENGTH_SHORT).show()
                }
            })
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

        // Busca no header → futura tela de busca
        binding.btnBuscarHeader.setOnClickListener {
            Toast.makeText(this, "Busca em breve", Toast.LENGTH_SHORT).show()
        }
    }
}
