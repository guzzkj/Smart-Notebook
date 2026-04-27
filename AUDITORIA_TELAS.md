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
| 5 | **Assets** | Imagens faltantes → referenciar no XML e listar ao final. Nunca gerar PNG/SVG manualmente. |
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
| **Status** | ⏳ Pendente |
| **Activity** | `SplashActivity.kt` |
| **Layout** | `activity_splash.xml` |
| **Referência** | `TELAS/splash_screen_smart_notebook/screen.png` · `code.html` |
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
> _Registre aqui o que foi alterado ou o que falta._

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
| **Status** | ⏳ Pendente |
| **Activity** | `EsqueceuSenhaActivity.kt` |
| **Layout** | `activity_esqueceu_senha.xml` |
| **Referência** | `TELAS/esqueci_minha_senha_smart_notebook/screen.png` · `code.html` |
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
> _Registre aqui o que foi alterado ou o que falta._

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
| **Status** | ⏳ Pendente |
| **Activity** | `DetalhesMateriaActivity.kt` |
| **Layout** | `activity_detalhes_materia.xml` |
| **Item de lista** | `item_anotacao.xml` · `item_atividade.xml` |
| **Referência** | `TELAS/detalhes_da_mat_ria_tarefas_atualizadas/screen.png` · `code.html` |
| **Adapters** | `AnotacoesAdapter.kt` · `AtividadesAdapter.kt` |
| **Navega para** | `NovaAnotacaoActivity` · `NovaAtividadeActivity` · `TodasAnotacoesActivity` |

**Checklist**
- [ ] Layout fiel à referência
- [ ] Sem lógica avançada
- [ ] RecyclerView presente (se aplicável)
- [ ] Navegação via Intent explícita
- [ ] Comentários em português
- [ ] Assets existem em `res/drawable`
- [ ] ViewBinding sem referências quebradas

**Observações**
> _Registre aqui o que foi alterado ou o que falta._

---

## 07 · Nova Matéria

| Campo | Valor |
|-------|-------|
| **Status** | ⏳ Pendente |
| **Activity** | `NovaMateriaActivity.kt` |
| **Layout** | `activity_nova_materia.xml` |
| **Referência** | `TELAS/nova_mat_ria/screen.png` · `code.html` |
| **Adapter** | — (formulário, sem lista) |
| **Navega para** | `MinhasMateriasActivity` (volta) |

**Checklist**
- [ ] Layout fiel à referência
- [ ] Sem lógica avançada
- [ ] RecyclerView presente (se aplicável)
- [ ] Navegação via Intent explícita
- [ ] Comentários em português
- [ ] Assets existem em `res/drawable`
- [ ] ViewBinding sem referências quebradas

**Observações**
> _Registre aqui o que foi alterado ou o que falta._

---

## 08 · Nova Anotação

| Campo | Valor |
|-------|-------|
| **Status** | ⏳ Pendente |
| **Activity** | `NovaAnotacaoActivity.kt` |
| **Layout** | `activity_nova_anotacao.xml` |
| **Referência** | `TELAS/nova_anota_o/screen.png` · `code.html` |
| **Adapter** | — (formulário, sem lista) |
| **Navega para** | `DetalhesMateriaActivity` (volta) |

**Checklist**
- [ ] Layout fiel à referência
- [ ] Sem lógica avançada
- [ ] RecyclerView presente (se aplicável)
- [ ] Navegação via Intent explícita
- [ ] Comentários em português
- [ ] Assets existem em `res/drawable`
- [ ] ViewBinding sem referências quebradas

**Observações**
> _Registre aqui o que foi alterado ou o que falta._

---

## 09 · Nova Atividade

| Campo | Valor |
|-------|-------|
| **Status** | ⏳ Pendente |
| **Activity** | `NovaAtividadeActivity.kt` |
| **Layout** | `activity_nova_atividade.xml` |
| **Referência** | `TELAS/nova_atividade/screen.png` · `code.html` |
| **Adapter** | — (formulário, sem lista) |
| **Navega para** | `DetalhesMateriaActivity` (volta) |

**Checklist**
- [ ] Layout fiel à referência
- [ ] Sem lógica avançada
- [ ] RecyclerView presente (se aplicável)
- [ ] Navegação via Intent explícita
- [ ] Comentários em português
- [ ] Assets existem em `res/drawable`
- [ ] ViewBinding sem referências quebradas

**Observações**
> _Registre aqui o que foi alterado ou o que falta._

---

## 10 · Todas as Anotações

| Campo | Valor |
|-------|-------|
| **Status** | ⏳ Pendente |
| **Activity** | `TodasAnotacoesActivity.kt` |
| **Layout** | `activity_todas_anotacoes.xml` |
| **Item de lista** | `item_anotacao.xml` |
| **Referência** | `TELAS/todas_as_anota_es_nav_simplificada/screen.png` · `code.html` |
| **Adapter** | `AnotacoesAdapter.kt` |
| **Navega para** | `NovaAnotacaoActivity` · `DetalhesMateriaActivity` |

**Checklist**
- [ ] Layout fiel à referência
- [ ] Sem lógica avançada
- [ ] RecyclerView presente (se aplicável)
- [ ] Navegação via Intent explícita
- [ ] Comentários em português
- [ ] Assets existem em `res/drawable`
- [ ] ViewBinding sem referências quebradas

**Observações**
> _Registre aqui o que foi alterado ou o que falta._

---

## 11 · Calendário

| Campo | Valor |
|-------|-------|
| **Status** | ⏳ Pendente |
| **Activity** | `CalendarioActivity.kt` |
| **Layout** | `activity_calendario.xml` |
| **Referência** | `TELAS/calend_rio_em_portugu_s/screen.png` · `code.html` |
| **Adapter** | — (verificar se há lista) |
| **Navega para** | `MinhasMateriasActivity` · `MenuInstitucionalActivity` |

**Checklist**
- [ ] Layout fiel à referência
- [ ] Sem lógica avançada
- [ ] RecyclerView presente (se aplicável)
- [ ] Navegação via Intent explícita
- [ ] Comentários em português
- [ ] Assets existem em `res/drawable`
- [ ] ViewBinding sem referências quebradas

**Observações**
> _Registre aqui o que foi alterado ou o que falta._

---

## 12 · Menu Institucional

| Campo | Valor |
|-------|-------|
| **Status** | ⏳ Pendente |
| **Activity** | `MenuInstitucionalActivity.kt` |
| **Layout** | `activity_menu_institucional.xml` |
| **Referência** | `TELAS/menu_institucional_atualizado/screen.png` · `code.html` |
| **Adapter** | — (verificar se há lista) |
| **Navega para** | `EditarPerfilActivity` · `SobreActivity` · `LoginActivity` (logout) |

**Checklist**
- [ ] Layout fiel à referência
- [ ] Sem lógica avançada
- [ ] RecyclerView presente (se aplicável)
- [ ] Navegação via Intent explícita
- [ ] Comentários em português
- [ ] Assets existem em `res/drawable`
- [ ] ViewBinding sem referências quebradas

**Observações**
> _Registre aqui o que foi alterado ou o que falta._

---

## 13 · Editar Perfil

| Campo | Valor |
|-------|-------|
| **Status** | ⏳ Pendente |
| **Activity** | `EditarPerfilActivity.kt` |
| **Layout** | `activity_editar_perfil.xml` |
| **Referência** | `TELAS/edi_o_de_perfil_finalizada/screen.png` · `code.html` |
| **Adapter** | — (formulário, sem lista) |
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
> _Registre aqui o que foi alterado ou o que falta._

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
| 01 · Splash Screen | ⏳ Pendente |
| 02 · Login | ⏳ Pendente |
| 03 · Cadastro | ⏳ Pendente |
| 04 · Esqueceu a Senha | ⏳ Pendente |
| 05 · Minhas Matérias | ✅ Concluída |
| 06 · Detalhes da Matéria | ⏳ Pendente |
| 07 · Nova Matéria | ⏳ Pendente |
| 08 · Nova Anotação | ⏳ Pendente |
| 09 · Nova Atividade | ⏳ Pendente |
| 10 · Todas as Anotações | ⏳ Pendente |
| 11 · Calendário | ⏳ Pendente |
| 12 · Menu Institucional | ⏳ Pendente |
| 13 · Editar Perfil | ⏳ Pendente |
| 14 · Sobre | ⏳ Pendente |

**Concluídas: 1 / 14**

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

## Telas sem Referência Visual

As telas abaixo não possuem pasta em `TELAS\`. Revisar apenas consistência de código e padrão visual do app:

- **Login** (`activity_login.xml`)
- **Cadastro** (`activity_cadastro.xml`)
- **Sobre** (`activity_sobre.xml`)
