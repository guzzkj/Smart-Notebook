package com.example.smartnotebook.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.smartnotebook.R
import com.example.smartnotebook.adapters.AtividadesAdapter
import com.example.smartnotebook.databinding.ActivityCalendarioBinding
import com.example.smartnotebook.models.DadosMock

// TELA 10: Calendário — visualização mensal com eventos e atividades
class CalendarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preencherGradeCalendario()
        carregarEventosDoDia()
        configurarBottomNav()
    }

    // Cria as células dos dias de Outubro 2023 (mês começa no domingo)
    private fun preencherGradeCalendario() {
        val grid = binding.gridCalendario

        // Datas com marcação: vermelho = avaliação, verde = atividade
        val datasAvaliacao = setOf(12, 25)
        val datasAtividade = setOf(18, 20, 25)
        val alturaCelulaPx = (44 * resources.displayMetrics.density).toInt()

        for (dia in 1..31) {
            val tv = TextView(this)

            // Configura peso igual para cada célula na grade de 7 colunas
            val params = GridLayout.LayoutParams(
                GridLayout.spec(GridLayout.UNDEFINED),
                GridLayout.spec(GridLayout.UNDEFINED, 1f)
            )
            params.width  = 0
            params.height = alturaCelulaPx
            tv.layoutParams = params
            tv.text    = dia.toString()
            tv.gravity = Gravity.CENTER
            tv.textSize = 13f
            tv.setTextColor(Color.parseColor("#0F172A"))

            // Destaque para o dia 20 (selecionado no exemplo do Figma)
            if (dia == 20) {
                tv.setBackgroundColor(Color.parseColor("#E8EAF6"))
                tv.setTypeface(tv.typeface, Typeface.BOLD)
            }

            // Ponto vermelho: dia de avaliação
            if (datasAvaliacao.contains(dia)) {
                tv.setTextColor(Color.parseColor("#EF4444"))
            }

            // Ponto verde: dia de atividade (se não for avaliação)
            if (datasAtividade.contains(dia) && !datasAvaliacao.contains(dia)) {
                tv.setTextColor(Color.parseColor("#22C55E"))
            }

            grid.addView(tv)
        }
    }

    // Mostra atividades atrasadas como eventos do dia selecionado
    private fun carregarEventosDoDia() {
        binding.tvDiaSelecionado.text = "Segunda-feira, 20 de Outubro"

        val eventos = DadosMock.atividades.filter { it.atrasada }
        val adapter = AtividadesAdapter(eventos)

        binding.rvEventos.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(this)
        binding.rvEventos.adapter = adapter
    }

    // Configura as 3 abas do BottomNavigationView — Calendário já está selecionado
    private fun configurarBottomNav() {
        binding.bottomNav.selectedItemId = R.id.nav_calendario

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio -> {
                    // Intent explícita para a tela inicial
                    val intent = Intent(this, MinhasMateriasActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    true
                }
                R.id.nav_calendario -> true // já está no Calendário
                R.id.nav_menu       -> {
                    // Intent explícita para o Menu Institucional
                    startActivity(Intent(this, MenuInstitucionalActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
