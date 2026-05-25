package com.example.smartnotebook

import com.example.smartnotebook.models.Anotacao
import com.example.smartnotebook.models.Atividade
import com.example.smartnotebook.models.Materia
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray

// Repositório central para operações no Supabase (matérias, anotações e atividades)
object SupabaseRepository {

    // Retorna o ID do usuário autenticado ou lança exceção
    private fun usuarioId(): String =
        supabase.auth.currentUserOrNull()?.id
            ?: throw IllegalStateException("Usuário não autenticado")

    // ── Matérias ──────────────────────────────────────────────────────────────

    // Lista todas as matérias do usuário logado, ordenadas pela mais recente
    suspend fun listarMaterias(): List<Materia> =
        supabase.from("materias").select {
            filter { eq("user_id", usuarioId()) }
            order("created_at", Order.DESCENDING)
        }.decodeList()

    // Insere uma nova matéria e retorna o objeto criado pelo banco
    suspend fun inserirMateria(
        nome: String,
        professor: String,
        diasAula: List<String>,
        corHex: String
    ): Materia =
        supabase.from("materias").insert(buildJsonObject {
            put("user_id", usuarioId())
            put("nome", nome)
            put("professor", professor)
            put("cor_hex", corHex)
            putJsonArray("dias_aula") { diasAula.forEach { add(it) } }
        }) {
            select()
        }.decodeSingle()

    // Exclui uma matéria pelo ID (RLS garante que só o dono pode excluir)
    suspend fun excluirMateria(id: String) {
        supabase.from("materias").delete {
            filter { eq("id", id) }
        }
    }

    // Conta atividades não concluídas de uma matéria (para exibir pendentes no card)
    suspend fun contarPendentes(materiaId: String): Int =
        supabase.from("atividades").select {
            filter {
                eq("materia_id", materiaId)
                neq("status", "CONCLUIDA")
            }
        }.decodeList<Atividade>().size

    // ── Anotações ─────────────────────────────────────────────────────────────

    // Lista todas as anotações de uma matéria, ordenadas pela mais recente
    suspend fun listarAnotacoes(materiaId: String): List<Anotacao> =
        supabase.from("anotacoes").select {
            filter { eq("materia_id", materiaId) }
            order("created_at", Order.DESCENDING)
        }.decodeList()

    // Insere uma nova anotação e retorna o objeto criado
    suspend fun inserirAnotacao(
        materiaId: String,
        titulo: String,
        conteudo: String
    ): Anotacao =
        supabase.from("anotacoes").insert(buildJsonObject {
            put("user_id", usuarioId())
            put("materia_id", materiaId)
            put("titulo", titulo)
            put("conteudo", conteudo)
        }) {
            select()
        }.decodeSingle()

    // Exclui uma anotação pelo ID
    suspend fun excluirAnotacao(id: String) {
        supabase.from("anotacoes").delete {
            filter { eq("id", id) }
        }
    }

    // ── Atividades ────────────────────────────────────────────────────────────

    // Lista todas as atividades de uma matéria, ordenadas pela mais recente
    suspend fun listarAtividades(materiaId: String): List<Atividade> =
        supabase.from("atividades").select {
            filter { eq("materia_id", materiaId) }
            order("created_at", Order.DESCENDING)
        }.decodeList()

    // Lista todas as atividades do usuário (para o Calendário)
    suspend fun listarTodasAtividades(): List<Atividade> =
        supabase.from("atividades").select {
            filter { eq("user_id", usuarioId()) }
            order("data_entrega", Order.ASCENDING)
        }.decodeList()

    // Insere uma nova atividade e retorna o objeto criado
    suspend fun inserirAtividade(
        materiaId: String,
        titulo: String,
        tipo: String,
        status: String,
        dataEntrega: String,
        hora: String
    ): Atividade =
        supabase.from("atividades").insert(buildJsonObject {
            put("user_id", usuarioId())
            put("materia_id", materiaId)
            put("titulo", titulo)
            put("tipo", tipo)
            put("status", status)
            put("data_entrega", dataEntrega)
            put("hora", hora)
            put("atrasada", false)
        }) {
            select()
        }.decodeSingle()

    // Exclui uma atividade pelo ID
    suspend fun excluirAtividade(id: String) {
        supabase.from("atividades").delete {
            filter { eq("id", id) }
        }
    }
}
