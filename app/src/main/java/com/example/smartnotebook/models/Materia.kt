package com.example.smartnotebook.models

// Modelo de uma matéria acadêmica do semestre
data class Materia(
    val id: Int,
    val nome: String,
    val professor: String,
    val diasAula: List<String>,  // Ex: ["Seg", "Qua", "Sex"]
    val pendentes: Int,           // Quantidade de atividades pendentes
    val corHex: String            // Cor do acento visual do card
)
