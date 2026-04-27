package com.example.smartnotebook.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartnotebook.databinding.ItemAnotacaoBinding
import com.example.smartnotebook.models.Anotacao

// Adapter do RecyclerView para exibir anotações (Detalhes da Matéria e Todas as Anotações)
class AnotacoesAdapter(
    private val lista: List<Anotacao>,
    private val aoClicar: (Anotacao) -> Unit
) : RecyclerView.Adapter<AnotacoesAdapter.AnotacaoViewHolder>() {

    // ViewHolder: guarda as referências das views de cada item de anotação
    inner class AnotacaoViewHolder(private val binding: ItemAnotacaoBinding)
        : RecyclerView.ViewHolder(binding.root) {

        // Preenche as views com os dados da anotação
        fun bind(anotacao: Anotacao) {
            binding.tvTituloAnotacao.text = anotacao.titulo
            binding.tvDataAnotacao.text   = anotacao.data

            // Dispara o callback ao clicar — navega para os detalhes
            binding.root.setOnClickListener { aoClicar(anotacao) }
        }
    }

    // Infla o layout item_anotacao.xml e cria o ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnotacaoViewHolder {
        val binding = ItemAnotacaoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AnotacaoViewHolder(binding)
    }

    // Vincula os dados ao ViewHolder na posição correta
    override fun onBindViewHolder(holder: AnotacaoViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    // Informa ao RecyclerView o total de anotações
    override fun getItemCount(): Int = lista.size
}
