package com.example.smartnotebook.models

import com.google.gson.annotations.SerializedName

// Modelo de uma matéria acadêmica do semestre
data class Materia(
    val id: Int = 0,
    val nome: String,
    val professor: String = "",
    @SerializedName("dias_aula") val diasAula: List<String> = emptyList(),
    @SerializedName("cor_hex") val corHex: String = "#5C6BC0",
    @SerializedName("user_id") val userId: Int = 0,
    @SerializedName("created_at") val createdAt: String = "",
    val pendentes: Int = 0  // calculado via JOIN no PHP (listar_materias.php)
)
