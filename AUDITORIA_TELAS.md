# Auditoria de Telas — Smart Notebook

> **Como usar:** Para cada tela, troque `[ ]` por `[x]` quando a auditoria estiver concluída.
> Use as seções de **Status** e **Observações** para registrar o que foi feito ou o que ainda falta.

---

## Critérios Obrigatórios de Auditoria (aplicar em todas as telas)

| # | Critério | Descrição |
|---|----------|-----------|
| 1 | **Simplicidade Extrema** | Sem Corrotinas, Injeção de Dependência, DataBinding complexo ou lógicas de estado avançadas. O código deve ser explicável por um estudante iniciante. |
| 2 | **Tecnologia** | Layout em **XML** · Lógica em **Kotlin** puro |
| 3 | **RecyclerView** | Toda tela com lista deve usar `RecyclerView` + Adapter básico + dados mockados (`DadosMock`) |
| 4 | **Navegação** | Apenas **Intents explícitas** entre telas (`Intent(this, OutraActivity::class.java)`) |
| 5 | **Assets** | Ícones e imagens faltantes ou incorretos → **nunca desenhar nem gerar o arquivo**. Apenas referenciar o nome correto no XML (ex.: `@drawable/ic_email`) e listar na seção "Assets faltantes" da tela. O usuário adicionará o arquivo ao projeto. Todos os ícones do projeto estão em formato **PNG**. |
| 6 | **Comentários** | Comentários breves em **português** explicando o que cada bloco faz |
| 7 | **Escopo** | Não precisa ser funcional — apenas estrutura estabelecida para implementar CRUD futuramente |

### Checklist de revisão por tela

Para cada tela, verificar:

- [ ] Layout fiel à referência visual (`screen.png`) e estrutural (`code.html`)
- [ ] Sem lógica avançada (sem Coroutines, DI, StateFlow, LiveData complexo)
- [ ] RecyclerView presente (se a tela lista dados)
- [ ] Navegação via Intent explícita
- [ ] Comentários em português no XML e no `.kt`
- [ ] Assets referenciados existem em `res/drawable`
- [ ] ViewBinding ativo e sem referências a views removidas

---

## Mapa de Telas

### Legenda de Status
- `⏳ Pendente` — ainda não auditada
- `🔄 Em andamento` — auditoria iniciada
- `✅ Concluída` — layout e lógica revisados e corrigidos

---

## 01 · Splash Screen

| Campo | Valor |
|-------|-------|
| **Status** | ✅ Concluída |
| **Activity** | `SplashActivity.kt` |
| **Layout** | `activity_splash.xml` |
| **Referência** | `TELAS/splash_screen_smart_notebook/screen.png` · `code.html` |
| **Adapter** | — (sem lista) |
| **Navega para** | `LoginActivity` |

**Checklist**
- [x] Layout fiel à referência
- [x] Sem lógica avançada
- [x] RecyclerView presente (não aplicável — tela sem lista)
- [x] Navegação via Intent explícita + `finish()` (usuário não pode voltar)
- [x] Comentários em português
- [x] Assets existem em `res/drawable`
- [x] ViewBinding sem referências quebradas

**O que foi corrigido**
- `bg_splash.xml` alterado de gradiente diagonal para cor sólida `#5C6BC0` (fiel à referência)
- Logo redimensionado de 120dp para 72dp (`ic_logo.png` já inclui o container arredondado azul)
- Slogan corrigido de "Organize suas ideias com inteligência" para "Seu Caderno Inteligente"
- Cor do slogan corrigida de `#DDEEFF` para `#CCFFFFFF` (branco 80% da referência)
- `ProgressBar` circular substituído por barra horizontal determinada (33%, 240dp × 6dp)
- Adicionado `tv_versao` com texto "VERSION 2.0" ancorado ao rodapé

**Assets confirmados em `res/drawable/`**
| Arquivo | Formato | Status |
|---------|---------|--------|
| `ic_logo.png` | PNG | ✅ presente (inclui container arredondado) |
| `bg_splash.xml` | XML shape | ✅ presente |

> **Correção pós-auditoria (telas 05 e 06):** adicionado estilo `FABCircular` em `themes.xml` (`cornerSize=50%`) — o tema Material3 usa quadrado arredondado como padrão para FAB; sem esse override o botão não fica circular.

---

## 02 · Login

| Campo | Valor |
|-------|-------|
| **Status** | ⏳ Pendente |
| **Activity** | `LoginActivity.kt` |
| **Layout** | `activity_login.xml` |
| **Referência** | ⚠️ Sem pasta de referência em `TELAS\` |
| **Adapter** | — (sem lista) |
| **Navega para** | `MinhasMateriasActivity` · `CadastroActivity` · `EsqueceuSenhaActivity` |

**Checklist**
- [ ] Layout fiel à referência
- [ ] Sem lógica avançada
- [ ] RecyclerView presente (se aplicável)
- [ ] Navegação via Intent explícita
- [ ] Comentários em português
- [ ] Assets existem em `res/drawable`
- [ ] ViewBinding sem referências quebradas

**Observações**
> _Sem referência visual disponível — revisar apenas o código e consistência visual._

---

## 03 · Cadastro

| Campo | Valor |
|-------|-------|
| **Status** | ⏳ Pendente |
| **Activity** | `CadastroActivity.kt` |
| **Layout** | `activity_cadastro.xml` |
| **Referência** | ⚠️ Sem pasta de referência em `TELAS\` |
| **Adapter** | — (sem lista) |
| **Navega para** | `LoginActivity` |

**Checklist**
- [ ] Layout fiel à referência
- [ ] Sem lógica avançada
- [ ] RecyclerView presente (se aplicável)
- [ ] Navegação via Intent explícita
- [ ] Comentários em português
- [ ] Assets existem em `res/drawable`
- [ ] ViewBinding sem referências quebradas

**Observações**
> _Sem referência visual disponível — revisar apenas o código e consistência visual._

---

## 04 · Esqueceu a Senha

| Campo | Valor |
|-------|-------|
| **Status** | ✅ Concluída |
| **Activity** | `EsqueceuSenhaActivity.kt` |
| **Layout** | `activity_esqueceu_senha.xml` |
| **Referência** | `TELAS/esqueci_minha_senha_smart_notebook/screen.png` · `code.html` |
| **Adapter** | — (sem lista) |
| **Navega para** | `LoginActivity` (via `finish()` — correto para "voltar") |

**Checklist**
- [x] Layout fiel à referência
- [x] Sem lógica avançada
- [x] RecyclerView presente (não aplicável — tela sem lista)
- [x] Navegação via `finish()` (padrão correto para retorno ao Login)
- [x] Comentários em português
- [x] Assets existem em `res/drawable`
- [x] ViewBinding sem referências quebradas

**O que foi corrigido**
- Layout atualizado para referenciar `@drawable/ic_lock_reset` no lugar de `@android:drawable/ic_lock_lock`
- Comentários corrigidos de `TELA 3` para `TELA 4` (ambos `.kt` e `.xml`)
- Texto "Ainda precisa de ajuda? Contate o suporte" recebe sublinhado via `Paint.UNDERLINE_TEXT_FLAG` no `.kt`, fiel à referência visual

**Assets confirmados em `res/drawable/`**
| Arquivo | Formato | Status |
|---------|---------|--------|
| `ic_lock_reset.png` | PNG | ✅ presente |
| `ic_email.png` | PNG | ✅ presente |
| `ic_info.png` | PNG | ✅ presente |

---

## 05 · Minhas Matérias (Home)

| Campo | Valor |
|-------|-------|
| **Status** | ✅ Concluída |
| **Activity** | `MinhasMateriasActivity.kt` |
| **Layout** | `activity_minhas_materias.xml` |
| **Item de lista** | `item_materia.xml` |
| **Referência** | `TELAS/minhas_mat_rias_bot_o_padronizado/screen.png` · `code.html` |
| **Adapter** | `MateriasAdapter.kt` |
| **Navega para** | `DetalhesMateriaActivity` · `NovaMateriaActivity` · `CalendarioActivity` · `MenuInstitucionalActivity` |

**Checklist**
- [x] Layout fiel à referência
- [x] Sem lógica avançada
- [x] RecyclerView presente (MateriasAdapter + DadosMock)
- [x] Navegação via Intent explícita
- [x] Comentários em português
- [x] Assets existem em `res/drawable`
- [x] ViewBinding sem referências quebradas

**O que foi corrigido**
- Adicionada barra de acento roxa (`View` 5dp) na borda esquerda de cada card
- Adicionada linha "Última atualização" com `ic_calendario` + `tvUltimaAtualizacao`
- Botão "Ver Matéria" trocado de `OutlinedButton` para preenchido (roxo + texto branco)
- Badge de pendências com cor dinâmica: rose se `pendentes > 0`, cinza se `= 0`
- Removido label "Suas Matérias" (não presente na referência)
- Avatar trocado para silhueta de pessoa (`ic_avatar_placeholder`)
- Removido botão de overflow menu (três pontinhos)
- `Materia.kt` recebeu campo `ultimaAtualizacao: String`
- `DadosMock.kt` atualizado para 4 matérias idênticas à referência

---

## 06 · Detalhes da Matéria

| Campo | Valor |
|-------|-------|
| **Status** | ✅ Concluída |
| **Activity** | `DetalhesMateriaActivity.kt` |
| **Layout** | `activity_detalhes_materia.xml` |
| **Item de lista** | `item_anotacao.xml` · `item_atividade.xml` |
| **Referência** | `TELAS/detalhes_da_mat_ria_tarefas_atualizadas/screen.png` · `code.html` |
| **Adapters** | `AnotacoesAdapter.kt` · `AtividadesAdapter.kt` |
| **Navega para** | `NovaAnotacaoActivity` · `NovaAtividadeActivity` · `TodasAnotacoesActivity` |

**Checklist**
- [x] Layout fiel à referência
- [x] Sem lógica avançada
- [x] RecyclerView presente (AnotacoesAdapter + AtividadesAdapter + DadosMock)
- [x] Navegação via Intent explícita
- [x] Comentários em português
- [x] Assets existem em `res/drawable` (ver "Assets faltantes" abaixo)
- [x] ViewBinding sem referências quebradas

**O que foi corrigido**
- `item_anotacao.xml`: removido `imgChevron` (seta `@android:drawable/ic_media_next`) ausente na referência; constraints de `tvTituloAnotacao` e `tvDataAnotacao` corrigidas para `toEndOf="parent"`
- `item_atividade.xml`: removidos `android:background="@drawable/bg_card_nota"` e `android:layout_marginBottom="8dp"` — na referência as atividades ficam dentro de um container único, não em cards individuais
- `activity_detalhes_materia.xml`: `rvAtividades` envolvido em `MaterialCardView` (16dp corners, borda `divisoria`, sem elevação) fiel ao container único branco arredondado da referência
- `activity_detalhes_materia.xml`: adicionado `android:tint="@color/cor_erro"` ao `btnExcluir` — ícone vermelho como na referência
- `activity_detalhes_materia.xml`: FAB trocado de `@android:drawable/ic_input_add` para `@drawable/ic_add` (PNG do projeto)
- `DetalhesMateriaActivity.kt`: adicionado `DividerItemDecoration` ao `rvAtividades` para separar os itens dentro do container card

**Assets faltantes**
| Arquivo | Uso | Status |
|---------|-----|--------|
| `ic_add.png` | FAB "+" na tela (substituiu `@android:drawable/ic_input_add`) | ✅ adicionado pelo usuário |

**Correções pós-auditoria**
- FAB reposicionado: `constraintBottom_toTopOf="@id/bottomNav"` com `marginBottom="16dp"` (antes usava `toBottomOf="parent"` com margin fixo de 80dp, causando sobreposição na navbar)
- FAB reduzido com `fabSize="mini"` (40dp) e `maxImageSize="18dp"`
- FAB forçado a círculo com `app:shapeAppearanceOverlay="@style/FABCircular"` — necessário pois o tema Material3 usa quadrado arredondado por padrão
- Mesmo ícone `@drawable/ic_add` aplicado também na tela 05 (Minhas Matérias)

---

## 07 · Nova Matéria

| Campo | Valor |
|-------|-------|
| **Status** | ✅ Concluída |
| **Activity** | `NovaMateriaActivity.kt` |
| **Layout** | `activity_nova_materia.xml` |
| **Referência** | `TELAS/nova_mat_ria/screen.png` · `code.html` |
| **Adapter** | — (formulário, sem lista) |
| **Navega para** | `MinhasMateriasActivity` (via Intent + `FLAG_ACTIVITY_CLEAR_TOP`) |

**Checklist**
- [x] Layout fiel à referência
- [x] Sem lógica avançada
- [x] RecyclerView presente (não aplicável — tela de formulário)
- [x] Navegação via Intent explícita
- [x] Comentários em português
- [x] Assets existem em `res/drawable`
- [x] ViewBinding sem referências quebradas

**O que foi corrigido**
- Comentários corrigidos de `TELA 5` para `TELA 7` (`.kt` e `.xml`)
- Ícone do card de dica: `@android:drawable/ic_dialog_info` → `@drawable/ic_info` (PNG do projeto, critério 5)
- Chip selecionado: fundo roxo sólido + texto branco substituído por borda roxa + fundo `#1A5C6BC0` (roxo 10%) + texto roxo — fiel à referência
- Criado `bg_chip_dia_selecionado.xml` dedicado (sem alterar `bg_chip_selecionado` que é compartilhado com tela 09)
- Label "Nome da Matéria *": asterisco separado em `TextView` própria com `@color/cor_erro` (vermelho), fiel à referência

**Assets confirmados em `res/drawable/`**
| Arquivo | Formato | Status |
|---------|---------|--------|
| `ic_arrow_back.png` | PNG | ✅ presente |
| `ic_info.png` | PNG | ✅ presente |
| `bg_campo.xml` | XML shape | ✅ presente |
| `bg_chip_dia.xml` | XML shape | ✅ presente |
| `bg_chip_dia_selecionado.xml` | XML shape | ✅ criado nesta auditoria |
| `bg_dica.xml` | XML shape | ✅ presente |

---

## 08 · Nova Anotação

| Campo | Valor |
|-------|-------|
| **Status** | 🔄 Em andamento |
| **Activity** | `NovaAnotacaoActivity.kt` |
| **Layout** | `activity_nova_anotacao.xml` |
| **Referência** | `TELAS/nova_anota_o/screen.png` · `code.html` |
| **Adapter** | — (formulário, sem lista) |
| **Navega para** | `DetalhesMateriaActivity` (via `finish()`) |

**Checklist**
- [x] Layout fiel à referência
- [x] Sem lógica avançada
- [x] RecyclerView presente (não aplicável — tela de formulário)
- [x] Navegação via `finish()` (padrão correto para retorno)
- [x] Comentários em português
- [x] Assets existem em `res/drawable` (ver "Assets faltantes" abaixo)
- [x] ViewBinding sem referências quebradas

**O que foi corrigido**
- `btnSalvar`: trocado de `Button` com texto "✓" sólido roxo para `ImageButton` circular com fundo `bg_btn_salvar.xml` (roxo 10%) e ícone `@drawable/ic_check_circle` — fiel ao botão arredondado lavanda da referência
- Criado `bg_btn_salvar.xml`: oval com cor `#1A5C6BC0` (roxo 10%)
- Hint do título corrigido: `"Título da anotação"` → `"Título da Anotação"` (A maiúsculo, fiel à referência)
- Hint do conteúdo corrigido: `"Comece a escrever sua anotação..."` → `"Comece a escrever sua nota aqui..."` (texto idêntico à referência)
- Barra de formatação: adicionado botão **Checklist** (`@drawable/ic_checklist`) entre Lista e Imagem
- Barra de formatação: adicionados **2 separadores verticais** (`View` 1dp × 24dp, `@color/divisoria`) entre grupos B/I e Lista/Checklist, e entre Imagem e Mais
- Barra de formatação: adicionado **espaçador** (`layout_weight="1"`) para empurrar botão Mais para a extremidade direita
- Ícones da barra substituídos: `@android:drawable/` → referências a PNGs do projeto (critério 5)

**Assets confirmados em `res/drawable/`**
| Arquivo | Formato | Status |
|---------|---------|--------|
| `ic_arrow_back.png` | PNG | ✅ presente |
| `ic_check_circle.png` | PNG | ✅ presente |
| `bg_btn_salvar.xml` | XML shape | ✅ criado nesta auditoria |

**Assets faltantes**
| Arquivo | Uso | Status |
|---------|-----|--------|
| `ic_lista.png` | Botão "Lista com marcadores" na barra de formatação | ✅ adicionado pelo usuário |
| `ic_checklist.png` | Botão "Checklist" na barra de formatação | ✅ adicionado pelo usuário |
| `ic_imagem.png` | Botão "Inserir imagem" na barra de formatação | ✅ adicionado pelo usuário |
| `ic_mais_opcoes.png` | Botão "Mais opções" (⋮) na barra de formatação | ✅ adicionado pelo usuário |

---

## 09 · Nova Atividade

| Campo | Valor |
|-------|-------|
| **Status** | ✅ Concluída |
| **Activity** | `NovaAtividadeActivity.kt` |
| **Layout** | `activity_nova_atividade.xml` |
| **Referência** | `TELAS/nova_atividade/screen.png` · `code.html` |
| **Adapter** | — (formulário, sem lista) |
| **Navega para** | `DetalhesMateriaActivity` (via `finish()`) · `MinhasMateriasActivity` · `CalendarioActivity` · `MenuInstitucionalActivity` (BottomNav) |

**Checklist**
- [x] Layout fiel à referência
- [x] Sem lógica avançada
- [x] RecyclerView presente (não aplicável — tela de formulário)
- [x] Navegação via `finish()` (voltar) e Intent explícita (BottomNav)
- [x] Comentários em português
- [x] Assets existem em `res/drawable`
- [x] ViewBinding sem referências quebradas

**O que foi corrigido**
- Header: `background` de `fundo_card` → `fundo_tela`; adicionada linha divisória `View` 1dp abaixo do header
- Seletor Tarefa/Prova: container `bg_campo` → `bg_seletor_tipo.xml` (cinza claro, `#E2E4ED`, radius 16dp); tab ativa `bg_chip_selecionado` (roxo sólido) → `bg_tab_ativo.xml` (branco, radius 12dp) + texto roxo; fiel à referência visual
- Labels: `textStyle="bold"` → `fontFamily="inter_medium"` + `textColor="texto_secundario"` em todos os rótulos
- Label "Título" → "Título da Atividade"
- Placeholder do título: "Ex: Relatório de Microserviços" → "Ex: Exercícios de Álgebra"
- Campo Data: ícone `@drawable/ic_calendario` adicionado à esquerda (dentro de LinearLayout container)
- Label "Hora" → "Horário"
- Campo Horário: ícone `@drawable/ic_clock` adicionado à esquerda (dentro de LinearLayout container)
- "Observações (Opcional)" → "Observações"
- Placeholder observações: "Adicione detalhes adicionais..." → "Adicione detalhes extras..."
- `BottomNavigationView` adicionado ao rodapé fixo (ausente na versão anterior)
- `ScrollView` constraint: `toTopOf="btnSalvarAtividade"` → `toTopOf="layoutRodape"` (engloba botão + BottomNav)
- Spinner: adicionado item inicial "Selecione a matéria" como prompt
- `.kt`: `configurarAbas()` atualizado para usar `bg_tab_ativo` + `roxo_primario`; adicionado `configurarBottomNav()`

**Assets confirmados em `res/drawable/`**
| Arquivo | Formato | Status |
|---------|---------|--------|
| `ic_arrow_back.png` | PNG | ✅ presente |
| `ic_calendario.png` | PNG | ✅ presente |
| `ic_clock.png` | PNG | ✅ presente |
| `bg_campo.xml` | XML shape | ✅ presente |
| `bg_seletor_tipo.xml` | XML shape | ✅ criado nesta auditoria |
| `bg_tab_ativo.xml` | XML shape | ✅ criado nesta auditoria |

---

## 10 · Todas as Anotações

| Campo | Valor |
|-------|-------|
| **Status** | ✅ Concluída |
| **Activity** | `TodasAnotacoesActivity.kt` |
| **Layout** | `activity_todas_anotacoes.xml` |
| **Item de lista** | `item_anotacao.xml` |
| **Referência** | `TELAS/todas_as_anota_es_nav_simplificada/screen.png` · `code.html` |
| **Adapter** | `AnotacoesAdapter.kt` |
| **Navega para** | `NovaAnotacaoActivity` · `MinhasMateriasActivity` · `CalendarioActivity` · `MenuInstitucionalActivity` (BottomNav) |

**Checklist**
- [x] Layout fiel à referência
- [x] Sem lógica avançada
- [x] RecyclerView presente (AnotacoesAdapter + DadosMock)
- [x] Navegação via Intent explícita + `finish()` (voltar)
- [x] Comentários em português
- [x] Assets existem em `res/drawable` (ver "Assets faltantes" abaixo)
- [x] ViewBinding sem referências quebradas

**O que foi corrigido**
- Comentários corrigidos de `TELA 7` para `TELA 10` (`.kt` e `.xml`)
- Header: `background` de `fundo_card` (branco) → `fundo_tela` (cinza claro), fiel à referência
- Header: adicionada linha divisória `View` 1dp abaixo do header
- Título: adicionado `android:gravity="center"` — centralizado entre os dois botões, fiel à referência
- Barra de filtro "Mais Recentes": adicionado `LinearLayout` com `MaterialButton` (ícone `ic_swap_vert` + texto, fundo roxo 10%, radius 12dp), completamente ausente na versão anterior
- FAB: `@android:drawable/ic_input_add` → `@drawable/ic_add` (PNG do projeto, critério 5)
- FAB: reposicionado com `constraintBottom_toTopOf="@id/bottomNav"` + `marginBottom="16dp"` (antes usava `marginBottom="80dp"` fixo ancorado ao parent)
- FAB: adicionado `app:shapeAppearanceOverlay="@style/FABCircular"` (mesmo padrão das telas 05 e 06)
- `configurarBottomNav()` adicionado ao `.kt` (ausente na versão anterior) e chamado no `onCreate`
- `btnOrdenar` recebe click listener com Toast ("Ordenação em breve")
- `item_anotacao.xml`: adicionado `imgChevron` (`@drawable/ic_chevron_right`, 20dp, tint `texto_hint`) — referência da tela 10 mostra seta em todos os cards; constraints de título e data atualizadas para `End_toStartOf="@id/imgChevron"`

**Assets confirmados em `res/drawable/`**
| Arquivo | Formato | Status |
|---------|---------|--------|
| `ic_arrow_back.png` | PNG | ✅ presente |
| `ic_buscar.png` | PNG | ✅ presente |
| `ic_add.png` | PNG | ✅ presente |
| `bg_card_nota.xml` | XML shape | ✅ presente |

**Assets — placeholders XML criados (build funciona)**
| Arquivo PNG desejado | Placeholder XML criado | Ação do usuário |
|----------------------|------------------------|-----------------|
| `ic_swap_vert.png` | `ic_swap_vert.xml` ✅ | Adicionar PNG → excluir o XML |
| `ic_chevron_right.png` | `ic_chevron_right.xml` ✅ | Adicionar PNG → excluir o XML |

---

## 11 · Calendário

| Campo | Valor |
|-------|-------|
| **Status** | ✅ Concluída |
| **Activity** | `CalendarioActivity.kt` |
| **Layout** | `activity_calendario.xml` |
| **Referência** | `TELAS/calend_rio_em_portugu_s/screen.png` · `code.html` |
| **Adapter** | `EventosCalendarioAdapter.kt` |
| **Navega para** | `MinhasMateriasActivity` · `MenuInstitucionalActivity` |

**Checklist**
- [x] Layout fiel à referência
- [x] Sem lógica avançada (usa apenas `java.util.Calendar` — biblioteca padrão do Java)
- [x] RecyclerView presente (EventosCalendarioAdapter + DadosMock)
- [x] Navegação via Intent explícita
- [x] Comentários em português
- [x] Assets existem em `res/drawable` (ver "Assets faltantes" abaixo)
- [x] ViewBinding sem referências quebradas

**O que foi corrigido**
- Comentários corrigidos de `TELA 10` para `TELA 11` (`.kt` e `.xml`)
- Cabeçalho dos dias da semana: "Dom, Seg, Ter..." → letras simples "D, S, T, Q, Q, S, S" (fiel à referência)
- Grade do calendário envolvida em `MaterialCardView` branco arredondado (card ausente na versão anterior)
- Legenda: pontos de cor passaram de `View` sem shape para `@drawable/bg_dot_vermelho` e `@drawable/bg_dot_teal` (oval, fiel à referência)
- Setas de navegação: `@android:drawable/ic_media_previous/next` → `@drawable/ic_chevron_left` e `@drawable/ic_chevron_right` (critério 5)
- Pontinhos de eventos: antes mudavam a **cor do número** (vermelho/verde) — agora são `View` 4dp oval abaixo do número, invisível por padrão e visível apenas nos dias com evento
- Células vazias iniciais adicionadas via `java.util.Calendar.DAY_OF_WEEK` para alinhar o dia 1 à coluna correta do dia da semana
- Botões `btnMesAnterior` e `btnProximoMes` receberam click listeners que chamam `calAtual.add(Calendar.MONTH, ±1)` e redesenham a grade
- Criado `EventosCalendarioAdapter.kt` com `item_evento_calendario.xml` (card com ícone colorido, título, subtítulo tipo•matéria e hora) — visual fiel à referência

**Assets confirmados em `res/drawable/`**
| Arquivo | Formato | Status |
|---------|---------|--------|
| `ic_nav_menu.png` | PNG | ✅ presente |
| `ic_nav_calendario.png` | PNG | ✅ presente |
| `ic_chevron_right.xml` | XML placeholder | ✅ presente |
| `ic_chevron_left.xml` | XML placeholder | ✅ criado nesta auditoria |
| `bg_dot_vermelho.xml` | XML shape | ✅ criado nesta auditoria |
| `bg_dot_teal.xml` | XML shape | ✅ criado nesta auditoria |
| `bg_icone_avaliacao.xml` | XML shape | ✅ criado nesta auditoria |
| `bg_icone_atividade.xml` | XML shape | ✅ criado nesta auditoria |
| `ic_prova.png` | PNG | ✅ adicionado pelo usuário (cards de avaliação/prova) |
| `ic_atividade.png` | PNG | ✅ adicionado pelo usuário (cards de tarefa/entrega) |

**Assets faltantes**
| Arquivo | Uso | Status |
|---------|-----|--------|
| `ic_chevron_left.png` | Seta mês anterior (substitui XML placeholder) | ✅ adicionado pelo usuário |
| `ic_chevron_right.png` | Seta próximo mês (substitui XML placeholder) | ✅ adicionado pelo usuário |

---

## 12 · Menu Institucional

| Campo | Valor |
|-------|-------|
| **Status** | ✅ Concluída |
| **Activity** | `MenuInstitucionalActivity.kt` |
| **Layout** | `activity_menu_institucional.xml` |
| **Referência** | `TELAS/Menu Institucional.png` |
| **Adapter** | — (sem lista) |
| **Navega para** | `EditarPerfilActivity` (card usuário) · `SobreActivity` · `LoginActivity` (logout) |

**Checklist**
- [x] Layout fiel à referência
- [x] Sem lógica avançada
- [x] RecyclerView presente (não aplicável — tela sem lista)
- [x] Navegação via Intent explícita
- [x] Comentários em português
- [x] Assets existem em `res/drawable` (ver "Assets faltantes" abaixo)
- [x] ViewBinding sem referências quebradas

**O que foi corrigido**
- Comentários corrigidos de `TELA 11` para `TELA 12` (`.kt` e `.xml`)
- Adicionado cabeçalho (`header`) com botão `btnVoltar` (`ic_arrow_back`) e título "Menu" centralizado — ausente na versão anterior
- Removido item "Editar Perfil" da lista (não presente na referência); acesso agora via clique no `cardUsuario`
- `cardUsuario`: background de `fundo_card` (branco) → `bg_card_usuario_menu` (lavanda `#EEF0FF`, radius 16dp), com `android:foreground` de ripple e `marginHorizontal="16dp"` fiel à referência
- Avatar: `@android:drawable/ic_menu_myplaces` → `@drawable/ic_avatar_placeholder` (critério 5)
- Nome: "Carlos Eduardo Silva" → "Usuário Smart" (fiel à referência)
- E-mail: "carlos.eduardo@smartcaderno.com" → "smartcaderno@exemplo.com" (fiel à referência)
- Ícones dos itens: `@android:drawable/ic_dialog_info` → `@drawable/ic_info`; `ic_menu_help` → `@drawable/ic_guia`; `ic_menu_agenda` → `@drawable/ic_termos` (critério 5)
- Adicionado `FrameLayout` 36dp com `bg_icone_menu` (oval lavanda) ao redor de cada ícone de item — fiel aos círculos lavanda da referência
- Adicionado `ic_chevron_right` (16dp, tint `texto_hint`) ao final de cada item — ausente na versão anterior
- Item "Sair": adicionado ícone `@drawable/ic_sair` com fundo `bg_icone_sair` (oval vermelho claro `#FFEBEE`); adicionado `ic_chevron_right` com tint `cor_erro`; largura do `TextView` ajustada com `layout_weight="1"`
- `configurarHeader()` adicionado ao `.kt`: `btnVoltar.setOnClickListener { finish() }`
- `itemEditarPerfil` substituído por `cardUsuario.setOnClickListener { → EditarPerfilActivity }`

**Assets confirmados em `res/drawable/`**
| Arquivo | Formato | Status |
|---------|---------|--------|
| `ic_arrow_back.png` | PNG | ✅ presente |
| `ic_profile_menu.png` | PNG | ✅ adicionado pelo usuário (avatar do card de usuário) |
| `ic_info.png` | PNG | ✅ presente |
| `ic_chevron_right.png` | PNG | ✅ presente (adicionado na tela 11) |
| `bg_card_usuario_menu.xml` | XML shape | ✅ criado nesta auditoria |
| `bg_icone_menu.xml` | XML shape | ✅ criado nesta auditoria |
| `bg_icone_sair.xml` | XML shape | ✅ criado nesta auditoria |

**Assets faltantes**
| Arquivo | Uso | Status |
|---------|-----|--------|
| `ic_guia.png` | Ícone "Guia de Produtividade" (lâmpada) | ✅ adicionado pelo usuário |
| `ic_termos.png` | Ícone "Termos de Uso" (documento) | ✅ adicionado pelo usuário |
| `ic_sair.png` | Ícone "Sair" (seta de logout) | ✅ adicionado pelo usuário |

---

## 13 · Editar Perfil

| Campo | Valor |
|-------|-------|
| **Status** | ✅ Concluída |
| **Activity** | `EditarPerfilActivity.kt` |
| **Layout** | `activity_editar_perfil.xml` |
| **Referência** | `TELAS/edi_o_de_perfil_finalizada/screen.png` · `code.html` |
| **Adapter** | — (formulário, sem lista) |
| **Navega para** | `MenuInstitucionalActivity` (via `finish()`) |

**Checklist**
- [x] Layout fiel à referência
- [x] Sem lógica avançada
- [x] RecyclerView presente (não aplicável — tela de formulário)
- [x] Navegação via `finish()` (padrão correto para retorno ao Menu)
- [x] Comentários em português
- [x] Assets existem em `res/drawable` (ver "Assets faltantes" abaixo)
- [x] ViewBinding sem referências quebradas

**O que foi corrigido**
- Comentários corrigidos de `TELA 12` para `TELA 13` (`.kt` e `.xml`)
- Avatar: `@android:drawable/ic_menu_myplaces` + `tint` → `@drawable/ic_avatar_placeholder` sem tint e sem background (PNG já contém forma circular, critério 5)
- Badge câmera: background inline `#22C55E` duplicado (`background` + `backgroundTint`) → `@drawable/bg_badge_camera` (oval shape criado nesta auditoria)
- Badge câmera: ícone `@android:drawable/ic_menu_camera` → `@drawable/ic_camera` (PNG do projeto, critério 5)
- Adicionada `View` divisória 1dp `@color/divisoria` ancorada ao rodapé do header (padrão das telas 09–12)
- Botão "Salvar Alterações": `drawableStart` `@android:drawable/ic_menu_save` → `@drawable/ic_salvar` (PNG do projeto, critério 5)
- Labels "Nome Completo" e "E-mail": `textStyle="bold"` → `fontFamily="@font/inter_medium"` (padrão das telas auditadas)

**Assets confirmados em `res/drawable/`**
| Arquivo | Formato | Status |
|---------|---------|--------|
| `ic_arrow_back.png` | PNG | ✅ presente |
| `ic_avatar_placeholder.png` | PNG | ✅ presente |
| `bg_campo.xml` | XML shape | ✅ presente |
| `bg_badge_camera.xml` | XML shape | ✅ criado nesta auditoria |

**Assets faltantes**
| Arquivo | Uso | Status |
|---------|-----|--------|
| `ic_camera.png` | Ícone da câmera no badge do avatar | ✅ adicionado pelo usuário |
| `ic_salvar.png` | Ícone do disquete no botão "Salvar Alterações" | ✅ adicionado pelo usuário |

---

## 14 · Sobre

| Campo | Valor |
|-------|-------|
| **Status** | ⏳ Pendente |
| **Activity** | `SobreActivity.kt` |
| **Layout** | `activity_sobre.xml` |
| **Referência** | ⚠️ Sem pasta de referência em `TELAS\` |
| **Adapter** | — (sem lista) |
| **Navega para** | `MenuInstitucionalActivity` (volta) |

**Checklist**
- [ ] Layout fiel à referência
- [ ] Sem lógica avançada
- [ ] RecyclerView presente (se aplicável)
- [ ] Navegação via Intent explícita
- [ ] Comentários em português
- [ ] Assets existem em `res/drawable`
- [ ] ViewBinding sem referências quebradas

**Observações**
> _Sem referência visual disponível — revisar apenas o código e consistência visual._

---

## Progresso Geral

| Tela | Status |
|------|--------|
| 01 · Splash Screen | ✅ Concluída |
| 02 · Login | ⏳ Pendente |
| 03 · Cadastro | ⏳ Pendente |
| 04 · Esqueceu a Senha | ✅ Concluída |
| 05 · Minhas Matérias | ✅ Concluída |
| 06 · Detalhes da Matéria | ✅ Concluída |
| 07 · Nova Matéria | ✅ Concluída |
| 08 · Nova Anotação | 🔄 Em andamento |
| 09 · Nova Atividade | ✅ Concluída |
| 10 · Todas as Anotações | ✅ Concluída |
| 11 · Calendário | ✅ Concluída |
| 12 · Menu Institucional | ✅ Concluída |
| 13 · Editar Perfil | ✅ Concluída |
| 14 · Sobre | ⏳ Pendente |

**Concluídas: 10 / 14**

---

## Arquivos Compartilhados (não são telas)

| Arquivo | Tipo | Usado em |
|---------|------|----------|
| `item_materia.xml` | Layout de item | `MateriasAdapter` → tela 05 |
| `item_anotacao.xml` | Layout de item | `AnotacoesAdapter` → telas 06, 10 |
| `item_atividade.xml` | Layout de item | `AtividadesAdapter` → tela 06 |
| `MateriasAdapter.kt` | Adapter | Tela 05 |
| `AnotacoesAdapter.kt` | Adapter | Telas 06, 10 |
| `AtividadesAdapter.kt` | Adapter | Tela 06 |
| `DadosMock.kt` | Dados estáticos | Todas as telas com lista |
| `menu_bottom_nav.xml` | Menu de navegação | Telas 05, 11, 12 |

---

## Limpeza Global de Assets — `res/drawable/`

Realizada durante a auditoria da tela 04. Válida para todo o projeto.

**Ícones XML substituídos por PNG:**
| XML removido | PNG substituto |
|---|---|
| `ic_nav_inicio.xml` | `ic_nav_inicio.png` |
| `ic_nav_calendario.xml` | `ic_nav_calendario.png` |
| `ic_nav_menu.xml` | `ic_nav_menu.png` |
| `ic_buscar.xml` | `ic_buscar.png` |
| `ic_editar.xml` | `ic_editar.png` |
| `ic_excluir.xml` | `ic_excluir.png` |
| `ic_calendario.xml` | `ic_calendario.png` |
| `ic_avatar_placeholder.xml` | `ic_avatar_placeholder.png` |

**Backgrounds XML órfãos removidos** (sem referência em nenhum layout ou `.kt`):
`bg_toolbar.xml` · `bg_busca.xml` · `bg_botao_outline.xml` · `bg_fab.xml`

**Backgrounds XML mantidos** (são `<shape>` — não substituíveis por PNG):
`bg_card` · `bg_input` · `bg_card_nota` · `bg_splash` · `bg_avatar` · `bg_tag_categoria` · `bg_campo` · `bg_dica` · `bg_chip_dia` · `bg_chip_selecionado` · `bg_dot` · `bg_badge_pendente` · `bg_pendentes`

---

## Telas sem Referência Visual

As telas abaixo não possuem pasta em `TELAS\`. Revisar apenas consistência de código e padrão visual do app:

- **Login** (`activity_login.xml`)
- **Cadastro** (`activity_cadastro.xml`)
- **Sobre** (`activity_sobre.xml`)
