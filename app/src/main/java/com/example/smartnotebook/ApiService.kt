package com.example.smartnotebook

import com.example.smartnotebook.models.Anotacao
import com.example.smartnotebook.models.Atividade
import com.example.smartnotebook.models.Materia
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    // ── Autenticação ──────────────────────────────────────────────────────────

    @GET("smartnotebook_api/login.php")
    fun login(
        @Query("email") email: String,
        @Query("senha") senha: String
    ): Call<List<UserResponse>>

    @GET("smartnotebook_api/cadastro.php")
    fun cadastrar(
        @Query("nome")  nome:  String,
        @Query("email") email: String,
        @Query("senha") senha: String
    ): Call<CadastroResponse>

    // ── Matérias ──────────────────────────────────────────────────────────────

    // Retorna matérias com campo "pendentes" calculado por JOIN no PHP
    @GET("smartnotebook_api/listar_materias.php")
    fun listarMaterias(
        @Query("user_id") userId: Int
    ): Call<List<Materia>>

    @GET("smartnotebook_api/inserir_materia.php")
    fun inserirMateria(
        @Query("user_id")   userId:    Int,
        @Query("nome")      nome:      String,
        @Query("professor") professor: String,
        @Query("dias_aula") diasAula:  String,
        @Query("cor_hex")   corHex:    String
    ): Call<Materia>

    @GET("smartnotebook_api/excluir_materia.php")
    fun excluirMateria(
        @Query("id") id: Int
    ): Call<GenericResponse>

    // ── Anotações ─────────────────────────────────────────────────────────────

    @GET("smartnotebook_api/listar_anotacoes.php")
    fun listarAnotacoes(
        @Query("materia_id") materiaId: Int
    ): Call<List<Anotacao>>

    @GET("smartnotebook_api/inserir_anotacao.php")
    fun inserirAnotacao(
        @Query("user_id")    userId:    Int,
        @Query("materia_id") materiaId: Int,
        @Query("titulo")     titulo:    String,
        @Query("conteudo")   conteudo:  String
    ): Call<Anotacao>

    @GET("smartnotebook_api/excluir_anotacao.php")
    fun excluirAnotacao(
        @Query("id") id: Int
    ): Call<GenericResponse>

    // ── Atividades ────────────────────────────────────────────────────────────

    @GET("smartnotebook_api/listar_atividades.php")
    fun listarAtividades(
        @Query("materia_id") materiaId: Int
    ): Call<List<Atividade>>

    @GET("smartnotebook_api/listar_todas_atividades.php")
    fun listarTodasAtividades(
        @Query("user_id") userId: Int
    ): Call<List<Atividade>>

    @GET("smartnotebook_api/inserir_atividade.php")
    fun inserirAtividade(
        @Query("user_id")      userId:      Int,
        @Query("materia_id")   materiaId:   Int,
        @Query("titulo")       titulo:      String,
        @Query("tipo")         tipo:        String,
        @Query("status")       status:      String,
        @Query("data_entrega") dataEntrega: String,
        @Query("hora")         hora:        String
    ): Call<Atividade>

    @GET("smartnotebook_api/excluir_atividade.php")
    fun excluirAtividade(
        @Query("id") id: Int
    ): Call<GenericResponse>
}
