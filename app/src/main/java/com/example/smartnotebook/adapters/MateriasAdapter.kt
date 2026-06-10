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

    inner class MateriaViewHolder(private val binding: ItemMateriaBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(materia: Materia) {
            binding.tvNomeMateria.text = materia.nome
            binding.tvProfessor.text   = materia.professor
            binding.tvPendentes.text   = "${materia.pendentes} pendentes"
            // Exibe apenas a data (primeiros 10 caracteres do ISO timestamp)
            binding.tvUltimaAtualizacao.text = "Última atualização: ${materia.createdAt.take(10)}"

            if (materia.diasAula.isEmpty()) {
                binding.tvDiasAula.visibility = View.GONE
            } else {
                binding.tvDiasAula.visibility = View.VISIBLE
                binding.tvDiasAula.text = materia.diasAula.joinToString(" e ")
            }

            val ctx = binding.root.context
            if (materia.pendentes > 0) {
                binding.tvPendentes.setBackgroundResource(R.drawable.bg_badge_pendente)
                binding.tvPendentes.setTextColor(ContextCompat.getColor(ctx, R.color.cor_erro))
            } else {
                binding.tvPendentes.setBackgroundResource(R.drawable.bg_pendentes)
                binding.tvPendentes.setTextColor(ContextCompat.getColor(ctx, R.color.texto_secundario))
            }

            binding.root.setOnClickListener { aoClicar(materia) }
            binding.btnVerMateria.setOnClickListener { aoClicar(materia) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MateriaViewHolder {
        val binding = ItemMateriaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MateriaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MateriaViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    override fun getItemCount(): Int = lista.size
}
