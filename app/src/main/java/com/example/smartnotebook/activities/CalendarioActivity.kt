package com.example.smartnotebook.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartnotebook.R
import com.example.smartnotebook.adapters.EventosCalendarioAdapter
import com.example.smartnotebook.databinding.ActivityCalendarioBinding
import com.example.smartnotebook.models.DadosMock
import java.util.Calendar

// TELA 11: Calendário — visualização mensal com eventos e atividades
class CalendarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarioBinding

    // Calendário que rastreia o mês e ano sendo exibido (começa em Outubro 2023)
    private val calAtual = Calendar.getInstance().apply {
        set(2023, Calendar.OCTOBER, 1)
    }

    // Nomes dos meses em português para o título (array indexado por Calendar.MONTH, começa em 0)
    private val nomesMeses = arrayOf(
        "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
        "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
    )

    // Dias com marcação mockados para demonstrar o visual (baseado nas atividades do DadosMock)
    private val datasAvaliacao = setOf(12, 25)    // ponto vermelho
    private val datasAtividade = setOf(18, 20, 25) // ponto teal (se não for avaliação)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preencherGradeCalendario()
        carregarEventosDoDia()
        configurarNavegacaoMes()
        configurarBottomNav()
    }

    // Preenche a grade com células vazias de alinhamento + dias do mês
    private fun preencherGradeCalendario() {
        val grid = binding.gridCalendario
        grid.removeAllViews() // limpa a grade ao trocar de mês

        /*
         * java.util.Calendar.DAY_OF_WEEK retorna:
         *   1 = Domingo, 2 = Segunda, 3 = Terça, ..., 7 = Sábado
         * Como a grade começa no Domingo (coluna 0), adicionamos (dayOfWeek - 1) células vazias
         * antes do dia 1 para alinhar o mês corretamente.
         */
        val calTemp = calAtual.clone() as Calendar
        calTemp.set(Calendar.DAY_OF_MONTH, 1)
        val diaDaSemana  = calTemp.get(Calendar.DAY_OF_WEEK) // 1 = Dom, ..., 7 = Sáb
        val celulasVazias = diaDaSemana - 1                  // domingo = 0, quarta = 3, etc.

        // Total de dias do mês (28, 29, 30 ou 31)
        val totalDias = calTemp.getActualMaximum(Calendar.DAY_OF_MONTH)

        val alturaCelulaPx = (44 * resources.displayMetrics.density).toInt()

        // Adiciona células vazias para empurrar o dia 1 até a coluna certa
        repeat(celulasVazias) {
            val vazia = TextView(this)
            vazia.layoutParams = criarParamsGrade(alturaCelulaPx)
            grid.addView(vazia)
        }

        // Cria uma célula para cada dia do mês
        for (dia in 1..totalDias) {
            /*
             * Cada célula é um LinearLayout vertical com dois filhos:
             *   1) TextView  → número do dia
             *   2) View (4dp oval) → ponto de evento, invisível por padrão
             * Assim o ponto aparece abaixo do número, fiel à referência visual.
             */
            val celula = LinearLayout(this)
            celula.layoutParams = criarParamsGrade(alturaCelulaPx)
            celula.orientation  = LinearLayout.VERTICAL
            celula.gravity      = Gravity.CENTER

            // Número do dia
            val tvDia = TextView(this)
            tvDia.text     = dia.toString()
            tvDia.textSize = 13f
            tvDia.gravity  = Gravity.CENTER
            tvDia.setTextColor(Color.parseColor("#0F172A"))
            tvDia.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            // Destaca o dia 20 (dia selecionado na referência do Figma)
            if (dia == 20) {
                tvDia.setBackgroundColor(Color.parseColor("#E8EAF6"))
                tvDia.setTypeface(tvDia.typeface, Typeface.BOLD)
            }

            // Ponto oval abaixo do número (começa invisível)
            val pontoDp = (4 * resources.displayMetrics.density).toInt()
            val ponto   = android.view.View(this)
            val pontoParams = LinearLayout.LayoutParams(pontoDp, pontoDp)
            pontoParams.topMargin = (2 * resources.displayMetrics.density).toInt()
            ponto.layoutParams = pontoParams
            ponto.visibility   = android.view.View.INVISIBLE

            // Ponto vermelho: dia tem avaliação
            if (datasAvaliacao.contains(dia)) {
                ponto.visibility = android.view.View.VISIBLE
                ponto.setBackgroundResource(R.drawable.bg_dot_vermelho)
            }

            // Ponto teal: dia tem atividade (apenas se não for também avaliação)
            if (datasAtividade.contains(dia) && !datasAvaliacao.contains(dia)) {
                ponto.visibility = android.view.View.VISIBLE
                ponto.setBackgroundResource(R.drawable.bg_dot_teal)
            }

            celula.addView(tvDia)
            celula.addView(ponto)
            grid.addView(celula)
        }

        // Atualiza o título "Outubro 2023", "Novembro 2023", etc.
        val mes = calAtual.get(Calendar.MONTH)
        val ano = calAtual.get(Calendar.YEAR)
        binding.tvMesAno.text = "${nomesMeses[mes]} $ano"
    }

    // Cria os LayoutParams de uma célula da grade (largura proporcional, altura fixa)
    private fun criarParamsGrade(alturaPx: Int): GridLayout.LayoutParams {
        val params = GridLayout.LayoutParams(
            GridLayout.spec(GridLayout.UNDEFINED),
            GridLayout.spec(GridLayout.UNDEFINED, 1f)
        )
        params.width  = 0
        params.height = alturaPx
        return params
    }

    // Configura os botões de navegar entre meses
    private fun configurarNavegacaoMes() {
        binding.btnMesAnterior.setOnClickListener {
            // Calendar.add() cuida automaticamente da virada de ano (Jan → Dez do ano anterior)
            calAtual.add(Calendar.MONTH, -1)
            preencherGradeCalendario()
        }
        binding.btnProximoMes.setOnClickListener {
            calAtual.add(Calendar.MONTH, 1)
            preencherGradeCalendario()
        }
    }

    // Carrega os eventos do dia selecionado usando o EventosCalendarioAdapter
    private fun carregarEventosDoDia() {
        binding.tvDiaSelecionado.text = "Activities • Out 14"

        // Filtra atividades atrasadas como mock dos eventos do dia
        val eventos = DadosMock.atividades.filter { it.atrasada }
        val adapter = EventosCalendarioAdapter(eventos)

        binding.rvEventos.layoutManager = LinearLayoutManager(this)
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
                R.id.nav_menu -> {
                    // Intent explícita para o Menu Institucional
                    startActivity(Intent(this, MenuInstitucionalActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
