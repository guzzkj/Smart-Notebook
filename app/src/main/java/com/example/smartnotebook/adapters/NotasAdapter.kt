package com.example.smartnotebook.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
            binding.tvNomeMateria.text  = materia.nome
            binding.tvProfessor.text    = materia.professor
            binding.tvDiasAula.text     = materia.diasAula.joinToString(", ")
            binding.tvPendentes.text    = "${materia.pendentes} pendentes"

            // Clique no card ou no botão "Ver Matéria" — abre os detalhes
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

    // Vincula os dados ao ViewHolder na posição correta
    override fun onBindViewHolder(holder: MateriaViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    // Informa ao RecyclerView o total de matérias
    override fun getItemCount(): Int = lista.size
}
