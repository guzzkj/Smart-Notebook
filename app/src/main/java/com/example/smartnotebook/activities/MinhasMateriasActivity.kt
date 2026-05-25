package com.example.smartnotebook.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartnotebook.R
import com.example.smartnotebook.SupabaseRepository
import com.example.smartnotebook.adapters.MateriasAdapter
import com.example.smartnotebook.databinding.ActivityMinhasMateriasBinding
import kotlinx.coroutines.launch

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

    // Busca matérias do Supabase e preenche o RecyclerView
    private fun carregarMaterias() {
        lifecycleScope.launch {
            try {
                val materias = SupabaseRepository.listarMaterias()

                // Busca a contagem de pendentes de cada matéria e cria cópias atualizadas
                val materiasComPendentes = materias.map { materia ->
                    val count = SupabaseRepository.contarPendentes(materia.id)
                    materia.copy(pendentes = count)
                }

                val adapter = MateriasAdapter(materiasComPendentes) { materia ->
                    // Intent explícita ao clicar em uma matéria → abre os Detalhes
                    val intent = Intent(this@MinhasMateriasActivity, DetalhesMateriaActivity::class.java)
                    intent.putExtra(DetalhesMateriaActivity.EXTRA_MATERIA_ID, materia.id)
                    intent.putExtra(DetalhesMateriaActivity.EXTRA_MATERIA_NOME, materia.nome)
                    startActivity(intent)
                }
                binding.rvMaterias.layoutManager = LinearLayoutManager(this@MinhasMateriasActivity)
                binding.rvMaterias.adapter = adapter

            } catch (e: Exception) {
                Toast.makeText(this@MinhasMateriasActivity,
                    "Erro ao carregar matérias: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
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
