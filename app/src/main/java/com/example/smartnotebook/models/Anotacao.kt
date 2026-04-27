package com.example.smartnotebook.models

// Modelo de uma anotação vinculada a uma matéria
data class Anotacao(
    val id: Int,
    val titulo: String,
    val conteudo: String,
    val data: String,      // Ex: "24 de Outubro, 2023"
    val materiaId: Int
)
