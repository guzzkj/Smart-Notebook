package com.example.smartnotebook.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartnotebook.AtividadeInsert
import com.example.smartnotebook.AtividadeUpdate
import com.example.smartnotebook.R
import com.example.smartnotebook.SessionManager
import com.example.smartnotebook.SupabaseClient
import com.example.smartnotebook.databinding.ActivityNovaAtividadeBinding
import com.example.smartnotebook.eq
import com.example.smartnotebook.models.Atividade
import com.example.smartnotebook.models.Materia
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TELA 9: Nova Atividade — formulário para cadastrar tarefa ou prova (também usado para editar)
class NovaAtividadeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNovaAtividadeBinding

    // Controla qual tipo de atividade está selecionado (Tarefa ou Prova)
    private var tipoSelecionado = "TAREFA"

    // Lista de matérias carregadas para o Spinner
    private var materias = listOf<Materia>()

    // ID > -1 indica modo de edição de uma atividade existente
    private var atividadeId = -1
    private var statusAtual = "EM ANDAMENTO"
    private var horaAtual = "23:59"

    companion object {
        // Chave para indicar a matéria (pré-seleção na criação | matéria fixa na edição)
        const val EXTRA_MATERIA_ID = "extra_materia_id"

        // Chaves para abrir a tela em modo de edição com os dados já preenchidos
        const val EXTRA_ATIVIDADE_ID     = "extra_atividade_id"
        const val EXTRA_ATIVIDADE_TITULO = "extra_atividade_titulo"
        const val EXTRA_ATIVIDADE_TIPO   = "extra_atividade_tipo"
        const val EXTRA_ATIVIDADE_STATUS = "extra_atividade_status"
        const val EXTRA_ATIVIDADE_DATA   = "extra_atividade_data"
        const val EXTRA_ATIVIDADE_HORA   = "extra_atividade_hora"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNovaAtividadeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVoltar.setOnClickListener { finish() }
        configurarAbas()
        carregarMateriasSpinner()
        configurarBotaoSalvar()
        configurarBottomNav()
        preencherSeEdicao()
    }

    // Em modo de edição, preenche os campos com os dados recebidos via Intent e trava a matéria
    private fun preencherSeEdicao() {
        atividadeId = intent.getIntExtra(EXTRA_ATIVIDADE_ID, -1)
        if (atividadeId == -1) return

        binding.btnSalvarAtividade.text = "Salvar Alterações"
        binding.etTituloAtividade.setText(intent.getStringExtra(EXTRA_ATIVIDADE_TITULO) ?: "")
        binding.etData.setText(intent.getStringExtra(EXTRA_ATIVIDADE_DATA) ?: "")
        binding.etHora.setText(intent.getStringExtra(EXTRA_ATIVIDADE_HORA) ?: "23:59")
        statusAtual = intent.getStringExtra(EXTRA_ATIVIDADE_STATUS) ?: "EM ANDAMENTO"
        horaAtual   = intent.getStringExtra(EXTRA_ATIVIDADE_HORA) ?: "23:59"

        // Não dá para trocar a matéria de uma atividade existente — mantém a selecionada na criação
        binding.spinnerMateria.isEnabled = false

        if ((intent.getStringExtra(EXTRA_ATIVIDADE_TIPO) ?: "TAREFA") == "PROVA") {
            binding.tabProva.performClick()
        } else {
            binding.tabTarefa.performClick()
        }
    }

    // Alterna entre as abas "Tarefa" e "Prova" com feedback visual
    private fun configurarAbas() {
        binding.tabTarefa.setOnClickListener {
            tipoSelecionado = "TAREFA"
            // Tarefa selecionada: fundo branco + texto roxo
            binding.tabTarefa.setBackgroundResource(R.drawable.bg_tab_ativo)
            binding.tabTarefa.setTextColor(getColor(R.color.roxo_primario))
            // Prova não selecionada: sem fundo + texto secundário
            binding.tabProva.background = null
            binding.tabProva.setTextColor(getColor(R.color.texto_secundario))
        }
        binding.tabProva.setOnClickListener {
            tipoSelecionado = "PROVA"
            // Prova selecionada: fundo branco + texto roxo
            binding.tabProva.setBackgroundResource(R.drawable.bg_tab_ativo)
            binding.tabProva.setTextColor(getColor(R.color.roxo_primario))
            // Tarefa não selecionada: sem fundo + texto secundário
            binding.tabTarefa.background = null
            binding.tabTarefa.setTextColor(getColor(R.color.texto_secundario))
        }
    }

    // Carrega as matérias do Supabase e popula o Spinner
    private fun carregarMateriasSpinner() {
        val userId = SessionManager.getUserId(this)

        SupabaseClient.restApi.listarMaterias(eq(userId))
            .enqueue(object : Callback<List<Materia>> {
                override fun onResponse(call: Call<List<Materia>>, response: Response<List<Materia>>) {
                    materias = response.body() ?: emptyList()
                    val itens = listOf("Selecione a matéria") + materias.map { it.nome }
                    val adapter = android.widget.ArrayAdapter(
                        this@NovaAtividadeActivity,
                        android.R.layout.simple_spinner_item, itens
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerMateria.adapter = adapter

                    // Em edição, pré-seleciona a matéria fixa da atividade no Spinner (apenas visual, está travado)
                    val materiaFixaId = intent.getIntExtra(EXTRA_MATERIA_ID, -1)
                    if (atividadeId != -1 && materiaFixaId != -1) {
                        val posicao = materias.indexOfFirst { it.id == materiaFixaId }
                        if (posicao != -1) binding.spinnerMateria.setSelection(posicao + 1)
                    }
                }
                override fun onFailure(call: Call<List<Materia>>, t: Throwable) {
                    Toast.makeText(this@NovaAtividadeActivity, "Sem conexão com o Supabase. Verifique sua internet.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // Valida os campos e salva (insere ou atualiza, dependendo do modo) a atividade no Supabase
    private fun configurarBotaoSalvar() {
        binding.btnSalvarAtividade.setOnClickListener {
            val titulo = binding.etTituloAtividade.text.toString().trim()
            val data   = binding.etData.text.toString().trim()
            val hora   = binding.etHora.text.toString().trim().ifEmpty { "23:59" }

            if (titulo.isEmpty()) {
                binding.etTituloAtividade.error = "Informe o título"
                binding.etTituloAtividade.requestFocus()
                return@setOnClickListener
            }
            if (data.isEmpty()) {
                binding.etData.error = "Informe a data (AAAA-MM-DD)"
                binding.etData.requestFocus()
                return@setOnClickListener
            }

            if (atividadeId != -1) {
                salvarEdicao(titulo, data, hora)
            } else {
                salvarNova(titulo, data, hora)
            }
        }
    }

    // Insere uma nova atividade vinculada à matéria escolhida no Spinner
    private fun salvarNova(titulo: String, data: String, hora: String) {
        val posicao = binding.spinnerMateria.selectedItemPosition

        // Posição 0 é o prompt "Selecione a matéria"
        if (posicao == 0 || materias.isEmpty()) {
            Toast.makeText(this, "Selecione uma matéria", Toast.LENGTH_SHORT).show()
            return
        }

        val materiaId = materias[posicao - 1].id
        val userId    = SessionManager.getUserId(this)

        // Desabilita o botão durante a operação para evitar cliques duplicados
        binding.btnSalvarAtividade.isEnabled = false

        val body = AtividadeInsert(
            userId      = userId,
            materiaId   = materiaId,
            titulo      = titulo,
            tipo        = tipoSelecionado,
            status      = "EM ANDAMENTO",
            dataEntrega = data,
            hora        = hora
        )

        SupabaseClient.restApi.inserirAtividade(body).enqueue(object : Callback<List<Atividade>> {
            override fun onResponse(call: Call<List<Atividade>>, response: Response<List<Atividade>>) {
                binding.btnSalvarAtividade.isEnabled = true
                if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this@NovaAtividadeActivity, "Erro ao salvar atividade", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Atividade>>, t: Throwable) {
                binding.btnSalvarAtividade.isEnabled = true
                Toast.makeText(this@NovaAtividadeActivity, "Sem conexão com o Supabase. Verifique sua internet.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Atualiza título, tipo, data e hora de uma atividade existente via PATCH (status é preservado)
    private fun salvarEdicao(titulo: String, data: String, hora: String) {
        binding.btnSalvarAtividade.isEnabled = false

        val body = AtividadeUpdate(
            titulo      = titulo,
            tipo        = tipoSelecionado,
            status      = statusAtual,
            dataEntrega = data,
            hora        = hora
        )

        SupabaseClient.restApi.atualizarAtividade(eq(atividadeId), body).enqueue(object : Callback<List<Atividade>> {
            override fun onResponse(call: Call<List<Atividade>>, response: Response<List<Atividade>>) {
                binding.btnSalvarAtividade.isEnabled = true
                if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this@NovaAtividadeActivity, "Erro ao salvar alterações", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Atividade>>, t: Throwable) {
                binding.btnSalvarAtividade.isEnabled = true
                Toast.makeText(this@NovaAtividadeActivity, "Sem conexão com o Supabase. Verifique sua internet.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Configura as 3 abas do BottomNavigationView
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
