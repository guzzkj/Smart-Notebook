package com.example.smartnotebook.models

// Modelo de dados de uma Anotação
data class Nota(
    val id: Int,
    val titulo: String,
    val conteudo: String,
    val categoria: String,
    val data: String,
    val favorita: Boolean = false
)
