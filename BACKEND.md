# Smart Notebook — Backend & Integrações

## Visão Geral da Arquitetura

```
[Android App]
     │
     ├── Retrofit (XAMPP) ──► PHP API ──► MySQL (dados do app)
     │
     └── Retrofit (Gemini) ──► Google Gemini API (IA)
```

O app usa **dois clientes Retrofit separados**:
- `RetrofitClient` → servidor local XAMPP (login, matérias, anotações, atividades)
- `GeminiClient` → API do Google Gemini (melhoria de anotações com IA)

---

## 1. Banco de Dados — MySQL via XAMPP

### Pré-requisitos

- [XAMPP](https://www.apachefriends.org/) instalado na máquina
- Apache e MySQL iniciados no painel do XAMPP
- Emulador Android rodando na mesma máquina

### Criando o Banco

1. Abra o navegador: `http://localhost/phpmyadmin`
2. Clique em **SQL** e cole o conteúdo do arquivo:

```
C:\xampp\htdocs\smartnotebook_api\criar_banco.sql
```

3. Clique em **Executar**

### Estrutura das Tabelas

```sql
users
├── id          INT AUTO_INCREMENT PK
├── name        VARCHAR(255)
├── email       VARCHAR(255) UNIQUE
├── password    VARCHAR(255)
└── created_at  TIMESTAMP

materias
├── id          INT AUTO_INCREMENT PK
├── user_id     INT → users.id
├── nome        VARCHAR(255)
├── professor   VARCHAR(255)
├── dias_aula   TEXT  -- JSON array: ["Segunda","Quarta"]
├── cor_hex     VARCHAR(20)
└── created_at  TIMESTAMP

anotacoes
├── id          INT AUTO_INCREMENT PK
├── user_id     INT → users.id
├── materia_id  INT → materias.id
├── titulo      VARCHAR(255)
├── conteudo    TEXT
└── created_at  TIMESTAMP

atividades
├── id           INT AUTO_INCREMENT PK
├── user_id      INT → users.id
├── materia_id   INT → materias.id
├── titulo       VARCHAR(255)
├── tipo         VARCHAR(20)   -- TAREFA | PROVA | ENTREGA
├── status       VARCHAR(30)   -- EM ANDAMENTO | CONCLUIDA | ATRASADA | URGENTE
├── data_entrega DATE
├── hora         VARCHAR(5)
├── atrasada     TINYINT(1)
└── created_at   TIMESTAMP
```

> **Chaves estrangeiras com CASCADE:** excluir um usuário remove todas as matérias, anotações e atividades dele. Excluir uma matéria remove as anotações e atividades ligadas a ela.

---

## 2. API PHP

### Localização dos Arquivos

```
C:\xampp\htdocs\smartnotebook_api\
├── conexao.php                  ← conexão PDO (incluído por todos)
├── login.php
├── cadastro.php
├── listar_materias.php
├── inserir_materia.php
├── excluir_materia.php
├── listar_anotacoes.php
├── inserir_anotacao.php
├── excluir_anotacao.php
├── listar_atividades.php
├── listar_todas_atividades.php
├── inserir_atividade.php
├── excluir_atividade.php
└── criar_banco.sql
```

### Padrão das Requisições

Todos os endpoints usam **GET com parâmetros na URL** (sem POST body, sem autenticação JWT).

```
http://10.0.2.2/smartnotebook_api/login.php?email=a@b.com&senha=123456
```

> `10.0.2.2` é o endereço especial que o **emulador Android** usa para acessar o `localhost` da máquina host. Não muda entre máquinas — funciona em qualquer PC com XAMPP rodando.

### Tabela de Endpoints

| Arquivo | Parâmetros GET | Retorno |
|---|---|---|
| `login.php` | `email`, `senha` | `List<UserResponse>` (vazio = inválido) |
| `cadastro.php` | `nome`, `email`, `senha` | `CadastroResponse` |
| `listar_materias.php` | `user_id` | `List<Materia>` com campo `pendentes` |
| `inserir_materia.php` | `user_id`, `nome`, `professor`, `dias_aula`, `cor_hex` | `Materia` |
| `excluir_materia.php` | `id` | `GenericResponse` |
| `listar_anotacoes.php` | `materia_id` | `List<Anotacao>` |
| `inserir_anotacao.php` | `user_id`, `materia_id`, `titulo`, `conteudo` | `Anotacao` |
| `excluir_anotacao.php` | `id` | `GenericResponse` |
| `listar_atividades.php` | `materia_id` | `List<Atividade>` |
| `listar_todas_atividades.php` | `user_id` | `List<Atividade>` |
| `inserir_atividade.php` | `user_id`, `materia_id`, `titulo`, `tipo`, `status`, `data_entrega`, `hora` | `Atividade` |
| `excluir_atividade.php` | `id` | `GenericResponse` |

### Detalhes Importantes

**`dias_aula`** — enviado como string separada por vírgula no GET:
```
dias_aula=Segunda,Quarta,Sexta
```
O PHP converte para array e armazena como JSON no banco. Na resposta, retorna como array JSON para o Gson deserializar como `List<String>`.

**`pendentes`** — calculado no servidor via JOIN, sem N+1 calls:
```sql
SELECT m.*, COUNT(a.id) AS pendentes
FROM materias m
LEFT JOIN atividades a ON a.materia_id = m.id AND a.status != 'CONCLUIDA'
WHERE m.user_id = ?
GROUP BY m.id
```

**`atrasada`** — armazenado como `TINYINT(1)` no MySQL. O PHP converte para `bool` antes de retornar (`(bool)$row['atrasada']`), para o Gson mapear corretamente para `Boolean` no Kotlin.

---

## 3. Sessão do Usuário

Sem JWT ou token. Após o login bem-sucedido, o app salva localmente via `SharedPreferences`:

```
user_id   → Int
user_nome → String
user_email → String
```

Gerenciado por `SessionManager.kt`:

```kotlin
SessionManager.salvar(context, userId, nome, email)  // após login
SessionManager.getUserId(context)                     // em qualquer activity
SessionManager.estaLogado(context)                    // na SplashActivity
SessionManager.logout(context)                        // no botão Sair
```

A `SplashActivity` usa `Handler` (sem coroutines) para aguardar 2s e verificar `SessionManager.estaLogado()` antes de redirecionar.

---

## 4. Integração com Google Gemini (IA)

### O que faz

Na tela **Nova Anotação**, o botão **"✨ Melhorar com IA"** envia o texto escrito pelo usuário para o Google Gemini. A IA retorna a anotação organizada em tópicos, com exemplos e resumo. O texto melhorado substitui o conteúdo do campo.

### Credenciais e Modelo

```
Modelo:   gemini-1.5-flash
Endpoint: https://generativelanguage.googleapis.com/
```

A API key é enviada como query param (`?key=...`), padrão do Gemini para projetos sem OAuth.

### Fluxo da Requisição

```
[Botão clicado]
      │
      ▼
Monta GeminiRequest
      │  contents[0].parts[0].text = prompt + texto do usuário
      ▼
POST /v1beta/models/gemini-1.5-flash:generateContent?key=API_KEY
      │
      ▼
GeminiResponse
      │  candidates[0].content.parts[0].text = texto melhorado
      ▼
etConteudoAnotacao.setText(textoMelhorado)
```

### Arquivos Kotlin

| Arquivo | Responsabilidade |
|---|---|
| `GeminiClient.kt` | Singleton Retrofit com base URL do Gemini |
| `GeminiService.kt` | Interface com o endpoint `@POST` |
| `GeminiModels.kt` | Data classes para request e response |

### Prompt Enviado

```
Você é um assistente de estudos. O usuário escreveu a seguinte anotação:

"""
[texto do usuário]
"""

Melhore esta anotação seguindo estas regras:
- Organize o conteúdo em tópicos com títulos
- Complemente com informações relevantes e corretas
- Corrija erros gramaticais e melhore a clareza
- Adicione exemplos práticos se aplicável
- Inclua um resumo curto no final
- Responda APENAS com a anotação melhorada, sem explicações adicionais
- Mantenha o idioma original do texto
```

### Diferença entre os dois Retrofits

| | `RetrofitClient` | `GeminiClient` |
|---|---|---|
| **Base URL** | `http://10.0.2.2/` | `https://generativelanguage.googleapis.com/` |
| **Protocolo** | HTTP (local) | HTTPS (internet) |
| **Auth** | Nenhuma | API Key no query param |
| **Método** | GET + @Query | POST + @Body |
| **Usado em** | Todas as activities | Só em `NovaAnotacaoActivity` |

> O app precisa de **internet** apenas para o Gemini. O XAMPP funciona totalmente offline.

---

## 5. Checklist de Setup

```
[ ] XAMPP instalado
[ ] Apache iniciado no painel XAMPP
[ ] MySQL iniciado no painel XAMPP
[ ] criar_banco.sql executado no phpMyAdmin
[ ] Arquivos PHP em C:\xampp\htdocs\smartnotebook_api\
[ ] RetrofitClient.kt com BASE_URL = "http://10.0.2.2/"
[ ] Emulador Android rodando na mesma máquina
[ ] Internet disponível (para o Gemini)
```
