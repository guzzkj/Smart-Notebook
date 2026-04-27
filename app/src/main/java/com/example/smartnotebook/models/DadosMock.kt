package com.example.smartnotebook.models

// Dados fictícios para simulação do app sem banco de dados
object DadosMock {

    // Matérias do semestre atual (dados de exemplo para a tela)
    val materias = listOf(
        Materia(1, "Web Services", "Prof. Roberto Silva",
            listOf("Segunda", "Quarta"), 5, "#5C6BC0", "Hoje, 14:30"),
        Materia(2, "Banco de Dados", "Prof. Ana Oliveira",
            listOf("Terça", "Quinta"), 0, "#5C6BC0", "Ontem"),
        Materia(3, "Inteligência Artificial", "Prof. Marcos Souza",
            listOf("Quarta", "Sexta"), 2, "#5C6BC0", "2 dias atrás"),
        Materia(4, "Sistemas Distribuídos", "Prof. Carlos Mendes",
            listOf(), 0, "#5C6BC0", "1 semana atrás")
    )

    // Anotações vinculadas a cada matéria
    val anotacoes = listOf(
        Anotacao(1, "REST API Concepts",
            "REST é um estilo arquitetural para sistemas distribuídos baseado em HTTP.",
            "24 de Outubro, 2023", 1),
        Anotacao(2, "JSON vs XML Deep Dive",
            "JSON é mais leve e fácil de parsear. XML é mais verboso, mas amplamente suportado.",
            "22 de Outubro, 2023", 1),
        Anotacao(3, "OAuth2 and Security",
            "OAuth2 é um protocolo de autorização que permite acesso delegado a recursos.",
            "18 de Outubro, 2023", 1),
        Anotacao(4, "Derivadas e Regras",
            "A derivada de f(x) = x² é f'(x) = 2x. Regra da cadeia: (f∘g)' = f'(g)·g'",
            "23 de Outubro, 2023", 2),
        Anotacao(5, "Limites e Continuidade",
            "O limite de f(x) quando x→a descreve o comportamento da função próximo a a.",
            "20 de Outubro, 2023", 2),
        Anotacao(6, "Ordenação e Complexidade",
            "BubbleSort O(n²), QuickSort O(n log n) médio, MergeSort O(n log n) garantido.",
            "19 de Outubro, 2023", 3)
    )

    // Atividades (tarefas, provas, entregas) por matéria
    val atividades = listOf(
        Atividade(1, "Relatório de Microserviços", "TAREFA", "ATRASADA",
            "Out 20", "23:59", 1, true),
        Atividade(2, "P1: Fundamentos de SOA", "PROVA", "URGENTE",
            "Nov 12", "08:00", 1, true),
        Atividade(3, "Projeto: API de E-commerce", "ENTREGA", "EM ANDAMENTO",
            "Nov 18", "23:59", 1, false),
        Atividade(4, "Lista 3 - Integrais", "TAREFA", "URGENTE",
            "Out 25", "23:59", 2, true),
        Atividade(5, "P2: Cálculo Diferencial", "PROVA", "EM ANDAMENTO",
            "Nov 10", "14:00", 2, false),
        Atividade(6, "Trabalho de Grafos", "ENTREGA", "ATRASADA",
            "Out 18", "23:59", 3, true)
    )

    // Retorna anotações de uma matéria pelo ID
    fun anotacoesDaMateria(materiaId: Int) =
        anotacoes.filter { it.materiaId == materiaId }

    // Retorna atividades de uma matéria pelo ID
    fun atividadesDaMateria(materiaId: Int) =
        atividades.filter { it.materiaId == materiaId }

    // Retorna uma matéria pelo ID
    fun materiaPorId(id: Int) = materias.find { it.id == id }
}
