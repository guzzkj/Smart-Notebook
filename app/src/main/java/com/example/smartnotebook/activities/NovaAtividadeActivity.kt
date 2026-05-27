package com.example.smartnotebook.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartnotebook.R
import com.example.smartnotebook.RetrofitClient
import com.example.smartnotebook.SessionManager
import com.example.smartnotebook.databinding.ActivityNovaAtividadeBinding
import com.example.smartnotebook.models.Atividade
import com.example.smartnotebook.models.Materia
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TELA 9: Nova Atividade — formulário para cadastrar tarefa ou prova
class NovaAtividadeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNovaAtividadeBinding

    // Controla qual tipo de atividade está selecionado (Tarefa ou Prova)
    private var tipoSelecionado = "TAREFA"

    // Lista de matérias carregadas para o Spinner
    private var materias = listOf<Materia>()

    companion object {
        // Chave opcional para pré-selecionar uma matéria via Intent
        const val EXTRA_MATERIA_ID = "extra_materia_id"
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

    // Carrega as matérias do servidor PHP e popula o Spinner
    private fun carregarMateriasSpinner() {
        val userId = SessionManager.getUserId(this)

        RetrofitClient.apiService.listarMaterias(userId)
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
                }
                override fun onFailure(call: Call<List<Materia>>, t: Throwable) {
                    Toast.makeText(this@NovaAtividadeActivity, "Sem conexão. Verifique se o XAMPP está ativo.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // Valida os campos obrigatórios e salva a atividade via PHP/MySQL
    private fun configurarBotaoSalvar() {
        binding.btnSalvarAtividade.setOnClickListener {
            val titulo  = binding.etTituloAtividade.text.toString().trim()
            val data    = binding.etData.text.toString().trim()
            val posicao = binding.spinnerMateria.selectedItemPosition

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
            // Posição 0 é o prompt "Selecione a matéria"
            if (posicao == 0 || materias.isEmpty()) {
                Toast.makeText(this, "Selecione uma matéria", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val materiaId = materias[posicao - 1].id
            val userId    = SessionManager.getUserId(this)

            // Desabilita o botão durante a operação para evitar cliques duplicados
            binding.btnSalvarAtividade.isEnabled = false

            RetrofitClient.apiService.inserirAtividade(
                userId      = userId,
                materiaId   = materiaId,
                titulo      = titulo,
                tipo        = tipoSelecionado,
                status      = "EM ANDAMENTO",
                dataEntrega = data,
                hora        = "23:59"
            ).enqueue(object : Callback<Atividade> {
                override fun onResponse(call: Call<Atividade>, response: Response<Atividade>) {
                    binding.btnSalvarAtividade.isEnabled = true
                    if (response.isSuccessful && response.body() != null) {
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this@NovaAtividadeActivity, "Erro ao salvar atividade", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Atividade>, t: Throwable) {
                    binding.btnSalvarAtividade.isEnabled = true
                    Toast.makeText(this@NovaAtividadeActivity, "Sem conexão. Verifique se o XAMPP está ativo.", Toast.LENGTH_SHORT).show()
                }
            })
        }
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
