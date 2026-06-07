package com.example.smartnotebook.models

import com.google.gson.annotations.SerializedName

// Modelo de uma anotação vinculada a uma matéria
data class Anotacao(
    val id: Int = 0,
    val titulo: String,
    val conteudo: String = "",
    @SerializedName("created_at") val createdAt: String = "",
    @SerializedName("materia_id") val materiaId: Int,
    @SerializedName("user_id") val userId: String = ""
)
