package com.example.smartnotebook

data class UserResponse(
    val id:    Int,
    val nome:  String,
    val email: String
)

data class CadastroResponse(
    val sucesso:  Boolean,
    val mensagem: String
)

data class GenericResponse(
    val sucesso:  Boolean,
    val mensagem: String
)
