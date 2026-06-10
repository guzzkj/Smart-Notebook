package com.example.smartnotebook.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartnotebook.R
import com.example.smartnotebook.databinding.ItemEventoCalendarioBinding
import com.example.smartnotebook.models.Atividade

// Adapter do RecyclerView para exibir eventos na tela do Calendário
class EventosCalendarioAdapter(
    private val lista: List<Atividade>,
    private val materiaIdParaNome: Map<Int, String> = emptyMap()
) : RecyclerView.Adapter<EventosCalendarioAdapter.EventoViewHolder>() {

    inner class EventoViewHolder(private val binding: ItemEventoCalendarioBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(atividade: Atividade) {
            binding.tvTituloEvento.text = atividade.titulo
            binding.tvHoraEvento.text   = atividade.hora

            val nomeMateria = materiaIdParaNome[atividade.materiaId] ?: ""
            val tipoFormatado = when (atividade.tipo) {
                "PROVA"   -> "Avaliação"
                "TAREFA"  -> "Atividade"
                "ENTREGA" -> "Entrega"
                else      -> atividade.tipo
            }
            binding.tvSubtituloEvento.text = "$tipoFormatado • $nomeMateria"

            if (atividade.tipo == "PROVA") {
                binding.layoutIcone.setBackgroundResource(R.drawable.bg_icone_avaliacao)
                binding.imgIconeEvento.setImageResource(R.drawable.ic_prova)
                binding.imgIconeEvento.clearColorFilter()
            } else {
                binding.layoutIcone.setBackgroundResource(R.drawable.bg_icone_atividade)
                binding.imgIconeEvento.setImageResource(R.drawable.ic_atividade)
                binding.imgIconeEvento.clearColorFilter()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val binding = ItemEventoCalendarioBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return EventoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    override fun getItemCount(): Int = lista.size
}
