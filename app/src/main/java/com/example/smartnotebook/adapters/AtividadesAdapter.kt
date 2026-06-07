package com.example.smartnotebook.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartnotebook.databinding.ItemAtividadeBinding
import com.example.smartnotebook.models.Atividade

// Adapter do RecyclerView para exibir atividades na tela de Detalhes da Matéria
class AtividadesAdapter(
    private val lista: List<Atividade>,
    private val aoClicar: ((Atividade) -> Unit)? = null,
    private val aoExcluir: ((Atividade) -> Unit)? = null
) : RecyclerView.Adapter<AtividadesAdapter.AtividadeViewHolder>() {

    // ViewHolder: guarda as referências das views de cada item de atividade
    inner class AtividadeViewHolder(private val binding: ItemAtividadeBinding)
        : RecyclerView.ViewHolder(binding.root) {

        // Preenche as views com os dados da atividade
        fun bind(atividade: Atividade) {
            binding.tvTituloAtividade.text = atividade.titulo
            binding.tvTipoStatus.text      = "${atividade.tipo} • ${atividade.status}"
            binding.tvDataAtividade.text   = atividade.dataEntrega
            binding.tvHoraAtividade.text   = atividade.hora

            // Ponto de cor: vermelho = atrasada/urgente | verde = em andamento
            val cor = if (atividade.atrasada) Color.parseColor("#EF4444")
                      else                    Color.parseColor("#22C55E")
            binding.viewDot.setBackgroundColor(cor)
            binding.tvTipoStatus.setTextColor(cor)

            // Toque → abre a edição | Toque longo → solicita exclusão (quando habilitados)
            binding.root.setOnClickListener { aoClicar?.invoke(atividade) }
            binding.root.setOnLongClickListener {
                if (aoExcluir != null) {
                    aoExcluir.invoke(atividade)
                    true
                } else {
                    false
                }
            }
        }
    }

    // Infla o layout item_atividade.xml e cria o ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AtividadeViewHolder {
        val binding = ItemAtividadeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AtividadeViewHolder(binding)
    }

    // Vincula os dados ao ViewHolder na posição correta
    override fun onBindViewHolder(holder: AtividadeViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    // Informa ao RecyclerView o total de atividades
    override fun getItemCount(): Int = lista.size
}
