# Smart Notebook

App acadêmico Android (Kotlin) para organização de matérias, anotações, atividades e calendário, com geração de resumos por IA (Gemini).

## Stack

- **Linguagem:** Kotlin
- **UI:** Activities + ViewBinding (XML), sem Jetpack Compose
- **Rede:** Retrofit2 + OkHttp + Gson, chamadas via `Call.enqueue` (sem coroutines)
- **Backend:** [Supabase](https://supabase.com) — Postgres via PostgREST + Auth via GoTrue
- **IA:** Google Gemini 2.5 Flash (`generateContent`) para melhorar/resumir anotações
- **minSdk:** 24 / **targetSdk:** 35 / **compileSdk:** 35

## Estrutura do projeto

```
app/src/main/java/com/example/smartnotebook/
├── activities/        # 16 telas (Login, Cadastro, MinhasMaterias, NovaMateria,
│                       #  EditarMateria, DetalhesMateria, TodasAnotacoes,
│                       #  NovaAnotacao, NovaAtividade, Calendario,
│                       #  MenuInstitucional, EditarPerfil, Sobre, TermosUso,
│                       #  GuiaProdutividade, Splash)
├── adapters/           # Adapters de RecyclerView
├── models/             # Materia, Anotacao, Atividade, Categoria, Nota (Gson)
├── ui/                 # Recursos de tema
├── SupabaseClient.kt   # Retrofit singleton p/ Supabase (interceptors: apikey, auth, refresh token)
├── AuthApi.kt          # Endpoints GoTrue (signup, login, refresh, update user)
├── RestApi.kt          # Endpoints PostgREST (/rest/v1/...), views materias_com_pendentes e atividades_com_status
├── SessionManager.kt   # Persistência de sessão (SharedPreferences)
├── GeminiClient.kt / GeminiService.kt / GeminiModels.kt  # Cliente Retrofit p/ Gemini API
└── SmartNotebookApp.kt # Application class
```

## Pré-requisitos

- [Android Studio](https://developer.android.com/studio) (Koala ou superior)
- JDK 11
- Conta/projeto Supabase (URL + anon key)
- Chave de API do Google Gemini

## Como rodar

1. Clone o repositório:
   ```bash
   git clone https://github.com/<seu-usuario>/Smart-Notebook.git
   ```
2. Abra a pasta no Android Studio (aguarde o sync do Gradle).
3. Crie o arquivo `local.properties` na raiz do projeto (veja seção [Configuração do ambiente](#configuração-do-ambiente)).
4. Selecione um emulador ou conecte um dispositivo físico (USB debugging ativado).
5. Rode o app com o botão **Run** ▶ ou:
   ```bash
   ./gradlew installDebug
   ```

## Configuração do ambiente

O projeto lê chaves sensíveis de `local.properties` (não versionado). Crie/edite o arquivo na raiz do projeto com:

```properties
SUPABASE_URL=https://<seu-projeto>.supabase.co
SUPABASE_ANON_KEY=<sua-anon-key>
GEMINI_API_KEY=<sua-chave-gemini>
```

Essas chaves são expostas ao app via `BuildConfig` (ver `app/build.gradle.kts`).

## Build

```bash
./gradlew assembleDebug
```

## Funcionalidades

- Cadastro/login de usuário (Supabase Auth, com renovação automática de token)
- CRUD de matérias, anotações e atividades
- Listagem de todas as anotações com busca e ordenação
- Calendário mensal com eventos/atividades
- Geração e melhoria de anotações com Gemini 2.5 Flash
- Edição de perfil (nome/e-mail)
- Telas institucionais: Sobre, Termos de Uso, Guia de Produtividade

## Pontos de atenção

- `local.properties` contém credenciais — nunca commitar
- `usesCleartextTraffic="true"` no `AndroidManifest.xml` é resquício; comunicação real é via HTTPS (Supabase)
- `GEMINI_API_KEY` embutida no APK via BuildConfig — limitação conhecida; mitigação recomendada é proxy via Supabase Edge Function
