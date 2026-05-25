package com.example.smartnotebook.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Modelo de uma atividade acadêmica (tarefa, prova ou entrega)
@Serializable
data class Atividade(
    val id: String = "",
    val titulo: String,
    val tipo: String,                                     // "TAREFA", "PROVA" ou "ENTREGA"
    val status: String,                                   // "ATRASADA", "URGENTE", "EM ANDAMENTO" ou "CONCLUIDA"
    @SerialName("data_entrega") val dataEntrega: String,  // formato AAAA-MM-DD
    val hora: String = "23:59",
    @SerialName("materia_id") val materiaId: String,
    @SerialName("user_id") val userId: String = "",
    val atrasada: Boolean = false                         // true = ponto vermelho | false = ponto verde
)
