package com.example.smartnotebook

import com.example.smartnotebook.models.Anotacao
import com.example.smartnotebook.models.Atividade
import com.example.smartnotebook.models.Materia
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

// Endpoints PostgREST do Supabase (/rest/v1) — filtros no formato "eq.<valor>" do PostgREST
interface RestApi {

    // ── Matérias ──────────────────────────────────────────────────────────────

    // View "materias_com_pendentes" já traz o campo "pendentes" calculado via subquery
    @GET("rest/v1/materias_com_pendentes?select=*")
    fun listarMaterias(
        @Query("user_id") userIdFiltro: String,
        @Query("order") order: String = "created_at.desc"
    ): Call<List<Materia>>

    @Headers("Prefer: return=representation")
    @POST("rest/v1/materias")
    fun inserirMateria(@Body body: MateriaInsert): Call<List<Materia>>

    @DELETE("rest/v1/materias")
    fun excluirMateria(@Query("id") idFiltro: String): Call<ResponseBody>

    // ── Anotações ─────────────────────────────────────────────────────────────

    @GET("rest/v1/anotacoes?select=*")
    fun listarAnotacoes(
        @Query("materia_id") materiaIdFiltro: String,
        @Query("order") order: String = "created_at.desc"
    ): Call<List<Anotacao>>

    @Headers("Prefer: return=representation")
    @POST("rest/v1/anotacoes")
    fun inserirAnotacao(@Body body: AnotacaoInsert): Call<List<Anotacao>>

    @DELETE("rest/v1/anotacoes")
    fun excluirAnotacao(@Query("id") idFiltro: String): Call<ResponseBody>

    // ── Atividades ────────────────────────────────────────────────────────────

    // View "atividades_com_status" já traz o campo "atrasada" calculado a partir da data
    @GET("rest/v1/atividades_com_status?select=*")
    fun listarAtividades(
        @Query("materia_id") materiaIdFiltro: String,
        @Query("order") order: String = "data_entrega.asc"
    ): Call<List<Atividade>>

    @GET("rest/v1/atividades_com_status?select=*")
    fun listarTodasAtividades(
        @Query("user_id") userIdFiltro: String,
        @Query("order") order: String = "data_entrega.asc"
    ): Call<List<Atividade>>

    @Headers("Prefer: return=representation")
    @POST("rest/v1/atividades")
    fun inserirAtividade(@Body body: AtividadeInsert): Call<List<Atividade>>

    @DELETE("rest/v1/atividades")
    fun excluirAtividade(@Query("id") idFiltro: String): Call<ResponseBody>
}

// Constrói o filtro PostgREST "eq.<valor>" — ex: id=eq.5
fun eq(valor: Any): String = "eq.$valor"
