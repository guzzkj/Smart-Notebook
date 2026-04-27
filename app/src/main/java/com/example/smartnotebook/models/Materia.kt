package com.example.smartnotebook.models

// Modelo de uma matéria acadêmica do semestre
data class Materia(
    val id: Int,
    val nome: String,
    val professor: String,
    val diasAula: List<String>,       // Ex: ["Segunda", "Quarta"]
    val pendentes: Int,               // Quantidade de atividades pendentes
    val corHex: String,               // Cor reservada para customização futura
    val ultimaAtualizacao: String     // Ex: "Hoje, 14:30" ou "Ontem"
)
