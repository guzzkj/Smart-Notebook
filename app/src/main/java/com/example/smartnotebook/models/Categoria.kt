package com.example.smartnotebook.models

// Modelo de dados de uma Categoria de notas
data class Categoria(
    val id: Int,
    val emoji: String,
    val nome: String,
    val descricao: String,
    val quantidadeNotas: Int
)
