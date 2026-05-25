package com.example.smartnotebook.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

// Modelo de uma matéria acadêmica do semestre
@Serializable
data class Materia(
    val id: String = "",
    val nome: String,
    val professor: String = "",
    @SerialName("dias_aula") val diasAula: List<String> = emptyList(),
    @SerialName("cor_hex") val corHex: String = "#5C6BC0",
    @SerialName("user_id") val userId: String = "",
    @SerialName("created_at") val createdAt: String = "",
    @Transient val pendentes: Int = 0  // calculado localmente via contarPendentes()
)
