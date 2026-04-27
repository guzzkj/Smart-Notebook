package com.example.smartnotebook.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.smartnotebook.R
import com.example.smartnotebook.databinding.ItemMateriaBinding
import com.example.smartnotebook.models.Materia

// Adapter do RecyclerView para exibir a lista de matérias na tela Home
class MateriasAdapter(
    private val lista: List<Materia>,
    private val aoClicar: (Materia) -> Unit
) : RecyclerView.Adapter<MateriasAdapter.MateriaViewHolder>() {

    // ViewHolder: guarda as referências das views de cada card de matéria
    inner class MateriaViewHolder(private val binding: ItemMateriaBinding)
        : RecyclerView.ViewHolder(binding.root) {

        // Preenche o card com os dados da matéria
        fun bind(materia: Materia) {
            binding.tvNomeMateria.text = materia.nome
            binding.tvProfessor.text   = materia.professor
            binding.tvPendentes.text   = "${materia.pendentes} pendentes"
            binding.tvUltimaAtualizacao.text = "Última atualização: ${materia.ultimaAtualizacao}"

            // Dias de aula: oculta a linha se a matéria não tiver dias cadastrados
            if (materia.diasAula.isEmpty()) {
                binding.tvDiasAula.visibility = View.GONE
            } else {
                binding.tvDiasAula.visibility = View.VISIBLE
                binding.tvDiasAula.text = materia.diasAula.joinToString(" e ")
            }

            // Badge: vermelho com fundo rose se houver pendências, cinza se não houver
            val ctx = binding.root.context
            if (materia.pendentes > 0) {
                binding.tvPendentes.setBackgroundResource(R.drawable.bg_badge_pendente)
                binding.tvPendentes.setTextColor(ContextCompat.getColor(ctx, R.color.cor_erro))
            } else {
                binding.tvPendentes.setBackgroundResource(R.drawable.bg_pendentes)
                binding.tvPendentes.setTextColor(ContextCompat.getColor(ctx, R.color.texto_secundario))
            }

            // Clique no card ou no botão "Ver Matéria" — ambos abrem os detalhes
            binding.root.setOnClickListener { aoClicar(materia) }
            binding.btnVerMateria.setOnClickListener { aoClicar(materia) }
        }
    }

    // Infla o layout item_materia.xml e cria o ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MateriaViewHolder {
        val binding = ItemMateriaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MateriaViewHolder(binding)
    }

    // Vincula os dados ao ViewHolder na posição correta da lista
    override fun onBindViewHolder(holder: MateriaViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    // Informa ao RecyclerView o total de itens na lista
    override fun getItemCount(): Int = lista.size
}
