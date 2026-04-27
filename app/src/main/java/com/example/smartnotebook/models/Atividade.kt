package com.example.smartnotebook.models

// Modelo de uma atividade acadêmica (tarefa, prova ou entrega)
data class Atividade(
    val id: Int,
    val titulo: String,
    val tipo: String,       // "TAREFA", "PROVA" ou "ENTREGA"
    val status: String,     // "ATRASADA", "URGENTE" ou "EM ANDAMENTO"
    val data: String,       // Ex: "Out 20"
    val hora: String,       // Ex: "23:59"
    val materiaId: Int,
    val atrasada: Boolean   // true = ponto vermelho | false = ponto verde
)
