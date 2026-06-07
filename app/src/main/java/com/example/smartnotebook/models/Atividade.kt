package com.example.smartnotebook.models

import com.google.gson.annotations.SerializedName

// Modelo de uma atividade acadêmica (tarefa, prova ou entrega)
data class Atividade(
    val id: Int = 0,
    val titulo: String,
    val tipo: String,                                      // "TAREFA", "PROVA" ou "ENTREGA"
    val status: String,                                    // "ATRASADA", "URGENTE", "EM ANDAMENTO" ou "CONCLUIDA"
    @SerializedName("data_entrega") val dataEntrega: String,  // formato AAAA-MM-DD
    val hora: String = "23:59",
    @SerializedName("materia_id") val materiaId: Int,
    @SerializedName("user_id") val userId: String = "",
    val atrasada: Boolean = false                          // true = ponto vermelho | false = ponto verde
)
