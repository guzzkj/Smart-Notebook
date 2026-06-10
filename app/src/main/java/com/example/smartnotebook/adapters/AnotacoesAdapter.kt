package com.example.smartnotebook.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartnotebook.databinding.ItemAnotacaoBinding
import com.example.smartnotebook.models.Anotacao

// Adapter do RecyclerView para exibir anotações (Detalhes da Matéria e Todas as Anotações)
class AnotacoesAdapter(
    private val lista: List<Anotacao>,
    private val aoClicar: (Anotacao) -> Unit,
    private val aoExcluir: ((Anotacao) -> Unit)? = null
) : RecyclerView.Adapter<AnotacoesAdapter.AnotacaoViewHolder>() {

    inner class AnotacaoViewHolder(private val binding: ItemAnotacaoBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(anotacao: Anotacao) {
            binding.tvTituloAnotacao.text = anotacao.titulo
            binding.tvDataAnotacao.text   = anotacao.createdAt.take(10)

            binding.root.setOnClickListener { aoClicar(anotacao) }
            binding.root.setOnLongClickListener {
                if (aoExcluir != null) {
                    aoExcluir.invoke(anotacao)
                    true
                } else {
                    false
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnotacaoViewHolder {
        val binding = ItemAnotacaoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AnotacaoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnotacaoViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    override fun getItemCount(): Int = lista.size
}
