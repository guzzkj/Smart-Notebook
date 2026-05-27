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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartnotebook.R
import com.example.smartnotebook.RetrofitClient
import com.example.smartnotebook.SessionManager
import com.example.smartnotebook.adapters.EventosCalendarioAdapter
import com.example.smartnotebook.databinding.ActivityCalendarioBinding
import com.example.smartnotebook.models.Atividade
import com.example.smartnotebook.models.Materia
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

// TELA 11: Calendário — visualização mensal com eventos e atividades
class CalendarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarioBinding

    // Calendário que rastreia o mês e ano sendo exibido
    private val calAtual = Calendar.getInstance()

    // Nomes dos meses em português para o título (indexado por Calendar.MONTH, começa em 0)
    private val nomesMeses = arrayOf(
        "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
        "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
    )

    // Atividades e mapa de nomes carregados do servidor PHP
    private var todasAtividades   = listOf<Atividade>()
    private var materiaIdParaNome = mapOf<Int, String>()

    // Conjuntos de dias com eventos no mês atual (recalculados ao trocar de mês)
    private var datasAvaliacao = setOf<Int>()
    private var datasAtividade = setOf<Int>()

    // Controla se os dois requests já terminaram antes de renderizar o calendário
    private var atividadesCarregadas = false
    private var materiasCarregadas   = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarNavegacaoMes()
        configurarBottomNav()
        carregarDados()
    }

    // Busca atividades e matérias do servidor PHP e atualiza o calendário
    private fun carregarDados() {
        val userId = SessionManager.getUserId(this)

        // Request 1: todas as atividades do usuário
        RetrofitClient.apiService.listarTodasAtividades(userId)
            .enqueue(object : Callback<List<Atividade>> {
                override fun onResponse(call: Call<List<Atividade>>, response: Response<List<Atividade>>) {
                    todasAtividades = response.body() ?: emptyList()
                    atividadesCarregadas = true
                    if (materiasCarregadas) atualizarCalendario()
                }
                override fun onFailure(call: Call<List<Atividade>>, t: Throwable) {
                    Toast.makeText(this@CalendarioActivity, "Sem conexão. Verifique se o XAMPP está ativo.", Toast.LENGTH_SHORT).show()
                }
            })

        // Request 2: matérias para montar o mapa id→nome do adapter
        RetrofitClient.apiService.listarMaterias(userId)
            .enqueue(object : Callback<List<Materia>> {
                override fun onResponse(call: Call<List<Materia>>, response: Response<List<Materia>>) {
                    val materias = response.body() ?: emptyList()
                    // Monta mapa materiaId → nome para o adapter usar no subtítulo
                    materiaIdParaNome = materias.associate { it.id to it.nome }
                    materiasCarregadas = true
                    if (atividadesCarregadas) atualizarCalendario()
                }
                override fun onFailure(call: Call<List<Materia>>, t: Throwable) {
                    // Calendário pode mostrar eventos sem nome da matéria
                    materiasCarregadas = true
                    if (atividadesCarregadas) atualizarCalendario()
                }
            })
    }

    // Recalcula os sets de dias e redesenha a grade + eventos do mês atual
    private fun atualizarCalendario() {
        val anoAtual = calAtual.get(Calendar.YEAR)
        val mesAtual = calAtual.get(Calendar.MONTH) // 0-indexado

        // Filtra atividades do mês/ano visível e extrai o dia do campo data_entrega (AAAA-MM-DD)
        val atividadesDoMes = todasAtividades.filter { atividade ->
            val partes = atividade.dataEntrega.split("-")
            if (partes.size == 3) {
                val ano = partes[0].toIntOrNull() ?: 0
                val mes = (partes[1].toIntOrNull() ?: 0) - 1 // ajusta para índice 0
                ano == anoAtual && mes == mesAtual
            } else false
        }

        // Dias com prova → ponto vermelho | demais → ponto teal
        datasAvaliacao = atividadesDoMes
            .filter { it.tipo == "PROVA" }
            .mapNotNull { it.dataEntrega.split("-").getOrNull(2)?.toIntOrNull() }
            .toSet()

        datasAtividade = atividadesDoMes
            .filter { it.tipo != "PROVA" }
            .mapNotNull { it.dataEntrega.split("-").getOrNull(2)?.toIntOrNull() }
            .toSet()

        preencherGradeCalendario()
        carregarEventosDoDia(atividadesDoMes)
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
        val diaDaSemana   = calTemp.get(Calendar.DAY_OF_WEEK)
        val celulasVazias = diaDaSemana - 1

        val totalDias = calTemp.getActualMaximum(Calendar.DAY_OF_MONTH)
        val alturaCelulaPx = (44 * resources.displayMetrics.density).toInt()

        // Adiciona células vazias para empurrar o dia 1 até a coluna certa
        repeat(celulasVazias) {
            val vazia = TextView(this)
            vazia.layoutParams = criarParamsGrade(alturaCelulaPx)
            grid.addView(vazia)
        }

        // Hoje — para destacar visualmente se o mês atual estiver sendo exibido
        val hoje = Calendar.getInstance()
        val ehMesAtual = calAtual.get(Calendar.YEAR)  == hoje.get(Calendar.YEAR) &&
                         calAtual.get(Calendar.MONTH) == hoje.get(Calendar.MONTH)

        // Cria uma célula para cada dia do mês
        for (dia in 1..totalDias) {
            /*
             * Cada célula é um LinearLayout vertical com dois filhos:
             *   1) TextView  → número do dia
             *   2) View (4dp oval) → ponto de evento, invisível por padrão
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

            // Destaca o dia de hoje no mês corrente
            if (ehMesAtual && dia == hoje.get(Calendar.DAY_OF_MONTH)) {
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

            // Ponto vermelho: dia tem prova
            if (datasAvaliacao.contains(dia)) {
                ponto.visibility = android.view.View.VISIBLE
                ponto.setBackgroundResource(R.drawable.bg_dot_vermelho)
            }

            // Ponto teal: dia tem atividade (apenas se não for também prova)
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
            atualizarCalendario()
        }
        binding.btnProximoMes.setOnClickListener {
            calAtual.add(Calendar.MONTH, 1)
            atualizarCalendario()
        }
    }

    // Exibe todas as atividades do mês atual na lista de eventos
    private fun carregarEventosDoDia(atividadesDoMes: List<Atividade>) {
        val mes = nomesMeses[calAtual.get(Calendar.MONTH)]
        binding.tvDiaSelecionado.text = "Atividades • $mes"

        val adapter = EventosCalendarioAdapter(atividadesDoMes, materiaIdParaNome)
        binding.rvEventos.layoutManager = LinearLayoutManager(this)
        binding.rvEventos.adapter = adapter
    }

    // Configura as 3 abas do BottomNavigationView — Calendário já está selecionado
    private fun configurarBottomNav() {
        binding.bottomNav.selectedItemId = R.id.nav_calendario

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio -> {
                    val intent = Intent(this, MinhasMateriasActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    true
                }
                R.id.nav_calendario -> true // já está no Calendário
                R.id.nav_menu -> {
                    startActivity(Intent(this, MenuInstitucionalActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
