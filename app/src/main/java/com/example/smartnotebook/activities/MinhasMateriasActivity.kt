package com.example.smartnotebook.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartnotebook.R
import com.example.smartnotebook.adapters.MateriasAdapter
import com.example.smartnotebook.databinding.ActivityMinhasMateriasBinding
import com.example.smartnotebook.models.DadosMock

// TELA 4: Minhas Matérias — tela principal exibida após o login
class MinhasMateriasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMinhasMateriasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMinhasMateriasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarListaMaterias()
        // configurarBottomNav()
        configurarBotoes()
    }

    // Preenche o RecyclerView com a lista de matérias do mock
    private fun configurarListaMaterias() {
        val adapter = MateriasAdapter(DadosMock.materias) { materia ->
            // Intent explícita ao clicar em uma matéria → abre os Detalhes
            val intent = Intent(this, DetalhesMateriaActivity::class.java)
            intent.putExtra(DetalhesMateriaActivity.EXTRA_MATERIA_ID, materia.id)
            startActivity(intent)
        }
        binding.rvMaterias.layoutManager = LinearLayoutManager(this)
        binding.rvMaterias.adapter = adapter
    }

    // Configura as 3 abas do BottomNavigationView — comentado temporariamente
    /*
    private fun configurarBottomNav() {
        binding.bottomNav.selectedItemId = R.id.nav_inicio

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio    -> true // já está na Home
                R.id.nav_calendario -> {
                    // Intent explícita para o Calendário
                    startActivity(Intent(this, CalendarioActivity::class.java))
                    true
                }
                R.id.nav_menu -> {
                    // Intent explícita para o Menu Institucional
                    val intent = Intent(this, MenuInstitucionalActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
    */

    private fun configurarBotoes() {
        // FAB (+) → abre tela para cadastrar nova matéria
        binding.fabNovaMateria.setOnClickListener {
            startActivity(Intent(this, NovaMateriaActivity::class.java))
        }

        // Busca no header → futura tela de busca
        binding.btnBuscarHeader.setOnClickListener {
            // Toast para feedback (funcionalidade a ser implementada)
            android.widget.Toast.makeText(this, "Busca em breve", android.widget.Toast.LENGTH_SHORT).show()
        }
    }
}
