package com.example.smartnotebook.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartnotebook.R
import com.example.smartnotebook.databinding.ItemEventoCalendarioBinding
import com.example.smartnotebook.models.Atividade
import com.example.smartnotebook.models.DadosMock

// Adapter do RecyclerView para exibir eventos na tela do Calendário
class EventosCalendarioAdapter(
    private val lista: List<Atividade>
) : RecyclerView.Adapter<EventosCalendarioAdapter.EventoViewHolder>() {

    // ViewHolder: guarda as referências das views de cada card de evento
    inner class EventoViewHolder(private val binding: ItemEventoCalendarioBinding)
        : RecyclerView.ViewHolder(binding.root) {

        // Preenche o card com os dados do evento
        fun bind(atividade: Atividade) {
            binding.tvTituloEvento.text = atividade.titulo
            binding.tvHoraEvento.text   = atividade.hora

            // Subtítulo: tipo formatado + nome da matéria
            val nomeMateria = DadosMock.materiaPorId(atividade.materiaId)?.nome ?: ""
            val tipoFormatado = when (atividade.tipo) {
                "PROVA"   -> "Avaliação"
                "TAREFA"  -> "Atividade"
                "ENTREGA" -> "Entrega"
                else      -> atividade.tipo
            }
            binding.tvSubtituloEvento.text = "$tipoFormatado • $nomeMateria"

            // Fundo e ícone: vermelho + ic_prova para provas, teal + ic_atividade para o resto
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

    // Infla o layout item_evento_calendario.xml e cria o ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val binding = ItemEventoCalendarioBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return EventoViewHolder(binding)
    }

    // Vincula os dados ao ViewHolder na posição correta
    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    // Informa ao RecyclerView o total de eventos
    override fun getItemCount(): Int = lista.size
}
