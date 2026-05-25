package com.example.smartnotebook.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Modelo de uma anotação vinculada a uma matéria
@Serializable
data class Anotacao(
    val id: String = "",
    val titulo: String,
    val conteudo: String = "",
    @SerialName("created_at") val createdAt: String = "",
    @SerialName("materia_id") val materiaId: String,
    @SerialName("user_id") val userId: String = ""
)
