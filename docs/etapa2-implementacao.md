# Documentação da Etapa II - Delibera Consilium

**Data:** 27 de Janeiro de 2026
**Versão:** 1.0.0

---

## Sumário

1. [Visão Geral](#visão-geral)
2. [Requisitos Implementados](#requisitos-implementados)
3. [Arquivos Criados](#arquivos-criados)
4. [Arquivos Modificados](#arquivos-modificados)
5. [Detalhamento das Modificações](#detalhamento-das-modificações)
6. [Fluxo de Funcionamento](#fluxo-de-funcionamento)
7. [Como Testar](#como-testar)

---

## Visão Geral

Esta documentação detalha as implementações realizadas na **Etapa II** do projeto Delibera Consilium, focando nos requisitos de gerenciamento de sessões do colegiado.

### Pontuação Alcançada
- **Implementado:** 80/100 pontos
- **Pendente:** 20/100 pontos (REQFUNC 6, REQFUNC 16, REQNAOFUNC 9)

---

## Requisitos Implementados

### REQNAOFUNC 7 - Layouts e Fragmentos Thymeleaf (10 pts) ✅

**Objetivo:** Extrair componentes reutilizáveis do layout em fragmentos separados.

**O que foi feito:**
- Criada pasta `templates/fragments/` para organizar fragmentos
- Extraída navbar em arquivo separado com menu dinâmico por role
- Extraído footer em arquivo separado
- Criado fragmento de alertas unificado (success, error, info, warning)
- Criado fragmento de paginação preparado para uso futuro
- Atualizado `layout.html` para usar `th:replace` nos fragmentos

---

### REQFUNC 9 - Coordenador cria sessão e define pauta (15 pts) ✅

**Objetivo:** Permitir que o coordenador crie reuniões e defina quais processos serão julgados.

**O que foi feito:**
- Atualizado enum `StatusReuniao` com novo estado `EM_ANDAMENTO`
- Adicionados campos `dataHoraInicio` e `dataHoraFim` na entidade `Reuniao`
- Criado `ReuniaoCoordenadorController` com CRUD completo
- Implementada página de gerenciamento de pauta
- Criados métodos no service para adicionar/remover processos da pauta
- Lista processos disponíveis (DISTRIBUIDOS sem reunião vinculada)

---

### REQFUNC 10 - Coordenador inicia sessão (5 pts) ✅

**Objetivo:** Permitir que o coordenador inicie uma sessão agendada.

**O que foi feito:**
- Implementado endpoint `POST /coord/reunioes/{id}/iniciar`
- Criado método `iniciarSessao()` no `ReuniaoService`
- Altera status de PROGRAMADA para EM_ANDAMENTO
- Registra timestamp de início (`dataHoraInicio`)
- Redireciona para página de condução da sessão

---

### REQFUNC 11 - Coordenador apregoa e julga processos (15 pts) ✅

**Objetivo:** Permitir que o coordenador conduza a sessão, visualize votos e registre decisões.

**O que foi feito:**
- Adicionados campos `resultado` e `dataJulgamento` na entidade `Processo`
- Criado método `contarVotos()` no `ProcessoService`
- Criado método `julgarProcesso()` no `ProcessoService`
- Criada página `conduzir.html` com interface completa
- Implementado accordion com processos da pauta
- Exibição de contagem de votos (COM_RELATOR / DIVERGENTE / AUSENTES)
- Botões para registrar decisão: Deferir, Indeferir, Retirar de Pauta
- Barra de progresso mostrando processos julgados

---

### REQFUNC 12 - Coordenador finaliza sessão (5 pts) ✅

**Objetivo:** Permitir que o coordenador encerre a sessão após julgar todos os processos.

**O que foi feito:**
- Implementado endpoint `POST /coord/reunioes/{id}/finalizar`
- Criado método `finalizarSessao()` no `ReuniaoService`
- Altera status de EM_ANDAMENTO para ENCERRADA
- Registra timestamp de fim (`dataHoraFim`)
- Botão habilitado apenas quando todos os processos foram julgados

---

## Arquivos Criados

### Templates (8 arquivos)

| Arquivo | Descrição |
|---------|-----------|
| `templates/fragments/navbar.html` | Fragmento da barra de navegação com menus por role |
| `templates/fragments/footer.html` | Fragmento do rodapé |
| `templates/fragments/alerts.html` | Fragmento de mensagens flash (success, error, info, warning) |
| `templates/fragments/pagination.html` | Fragmento de paginação para Spring Data Page |
| `templates/coord/reuniao/list.html` | Lista de reuniões do coordenador com filtros |
| `templates/coord/reuniao/form.html` | Formulário para criar/editar reunião |
| `templates/coord/reuniao/pauta.html` | Página de gerenciamento da pauta |
| `templates/coord/reuniao/conduzir.html` | Página de condução da sessão |

### Java (1 arquivo)

| Arquivo | Descrição |
|---------|-----------|
| `controller/ReuniaoCoordenadorController.java` | Controller para gerenciamento de reuniões pelo coordenador |

---

## Arquivos Modificados

### Models (3 arquivos)

| Arquivo | Modificação |
|---------|-------------|
| `model/StatusReuniao.java` | Adicionado valor `EM_ANDAMENTO` ao enum |
| `model/Reuniao.java` | Adicionados campos `dataHoraInicio` e `dataHoraFim` |
| `model/Processo.java` | Adicionados campos `resultado` e `dataJulgamento` |

### Repositories (2 arquivos)

| Arquivo | Modificação |
|---------|-------------|
| `repository/ReuniaoRepository.java` | Adicionados métodos `findByColegiado()`, `findByFiltros()` |
| `repository/ProcessoRepository.java` | Adicionados métodos `findByStatusAndReuniaoIsNull()`, `findByReuniaoId()` |

### Services (2 arquivos)

| Arquivo | Modificação |
|---------|-------------|
| `service/ReuniaoService.java` | Adicionados métodos de gerenciamento de sessão |
| `service/ProcessoService.java` | Adicionados métodos `contarVotos()` e `julgarProcesso()` |

### Templates (1 arquivo)

| Arquivo | Modificação |
|---------|-------------|
| `templates/layout.html` | Atualizado para usar fragmentos via `th:replace` |

---

## Detalhamento das Modificações

### 1. StatusReuniao.java

**Antes:**
```java
public enum StatusReuniao {
    PROGRAMADA,
    ENCERRADA
}
```

**Depois:**
```java
public enum StatusReuniao {
    PROGRAMADA,     // Sessao agendada, ainda nao iniciada
    EM_ANDAMENTO,   // Sessao em curso
    ENCERRADA       // Sessao finalizada
}
```

**Justificativa:** O estado intermediário `EM_ANDAMENTO` é necessário para identificar sessões que estão sendo conduzidas, diferenciando-as de sessões agendadas e finalizadas.

---

### 2. Reuniao.java

**Campos adicionados:**
```java
// Momento em que a sessao foi iniciada
private LocalDateTime dataHoraInicio;

// Momento em que a sessao foi encerrada
private LocalDateTime dataHoraFim;
```

**Validações adicionadas:**
```java
@NotNull(message = "A data da reuniao e obrigatoria")
private LocalDate dataReuniao;

@NotNull(message = "Selecione um colegiado")
@ManyToOne
@JoinColumn(name = "colegiado_id")
private Colegiado colegiado;
```

**Justificativa:** Os campos de data/hora permitem rastrear quando a sessão foi efetivamente iniciada e encerrada, independente da data agendada.

---

### 3. Processo.java

**Campos adicionados:**
```java
// Resultado do julgamento: DEFERIDO, INDEFERIDO, RETIRADO_DE_PAUTA
private String resultado;

// Data em que o processo foi julgado
private LocalDate dataJulgamento;
```

**Justificativa:** Armazena o resultado da decisão do colegiado e a data em que foi tomada, complementando o campo `status` que indica o estado do processo no fluxo.

---

### 4. ReuniaoRepository.java

**Métodos adicionados:**
```java
List<Reuniao> findByColegiado(Colegiado colegiado);

List<Reuniao> findByColegiadoAndStatus(Colegiado colegiado, StatusReuniao status);

@Query("SELECT r FROM Reuniao r WHERE " +
       "(:status IS NULL OR r.status = :status) AND " +
       "(:colegiadoId IS NULL OR r.colegiado.id = :colegiadoId) " +
       "ORDER BY r.dataReuniao DESC")
List<Reuniao> findByFiltros(@Param("status") StatusReuniao status,
                             @Param("colegiadoId") Long colegiadoId);
```

**Justificativa:** Permite filtrar reuniões por status e colegiado na listagem do coordenador.

---

### 5. ProcessoRepository.java

**Métodos adicionados:**
```java
// Processos disponiveis para pauta (distribuidos sem reuniao)
List<Processo> findByStatusAndReuniaoIsNull(String status);

// Processos de uma reuniao especifica
List<Processo> findByReuniaoId(Long reuniaoId);
```

**Justificativa:** O primeiro método lista processos que podem ser adicionados à pauta (status DISTRIBUIDO e sem reunião vinculada). O segundo lista processos de uma reunião específica.

---

### 6. ReuniaoService.java

**Métodos adicionados:**

```java
/**
 * Cria uma nova sessao com status PROGRAMADA
 */
@Transactional
public Reuniao criarSessao(Reuniao reuniao) {
    reuniao.setStatus(StatusReuniao.PROGRAMADA);
    return reuniaoRepository.save(reuniao);
}

/**
 * Inicia uma sessao, mudando o status para EM_ANDAMENTO
 */
@Transactional
public Reuniao iniciarSessao(Long reuniaoId) {
    Reuniao reuniao = buscarPorId(reuniaoId);
    if (reuniao != null && reuniao.getStatus() == StatusReuniao.PROGRAMADA) {
        reuniao.setStatus(StatusReuniao.EM_ANDAMENTO);
        reuniao.setDataHoraInicio(LocalDateTime.now());
        return reuniaoRepository.save(reuniao);
    }
    return reuniao;
}

/**
 * Finaliza uma sessao, mudando o status para ENCERRADA
 */
@Transactional
public Reuniao finalizarSessao(Long reuniaoId) {
    Reuniao reuniao = buscarPorId(reuniaoId);
    if (reuniao != null && reuniao.getStatus() == StatusReuniao.EM_ANDAMENTO) {
        reuniao.setStatus(StatusReuniao.ENCERRADA);
        reuniao.setDataHoraFim(LocalDateTime.now());
        return reuniaoRepository.save(reuniao);
    }
    return reuniao;
}

/**
 * Adiciona um processo a pauta da reuniao
 */
@Transactional
public void adicionarProcessoAPauta(Long reuniaoId, Long processoId) {
    Reuniao reuniao = buscarPorId(reuniaoId);
    Processo processo = processoRepository.findById(processoId).orElse(null);
    if (reuniao != null && processo != null) {
        processo.setReuniao(reuniao);
        processoRepository.save(processo);
    }
}

/**
 * Remove um processo da pauta da reuniao
 */
@Transactional
public void removerProcessoDaPauta(Long reuniaoId, Long processoId) {
    Processo processo = processoRepository.findById(processoId).orElse(null);
    if (processo != null && processo.getReuniao() != null
            && processo.getReuniao().getId().equals(reuniaoId)) {
        processo.setReuniao(null);
        processoRepository.save(processo);
    }
}

/**
 * Lista processos disponiveis para adicionar a pauta
 */
public List<Processo> listarProcessosDisponiveis() {
    return processoRepository.findByStatusAndReuniaoIsNull("DISTRIBUIDO");
}
```

**Justificativa:** Centraliza a lógica de negócio do ciclo de vida das sessões e gerenciamento de pauta.

---

### 7. ProcessoService.java

**Métodos adicionados:**

```java
/**
 * Conta os votos de um processo
 * @return Map com contagem: {"COM_RELATOR": x, "DIVERGENTE": y, "AUSENTES": z}
 */
public Map<String, Long> contarVotos(Long processoId) {
    List<Voto> votos = votoRepository.findByProcessoId(processoId);

    long comRelator = votos.stream()
            .filter(v -> !v.isAusente() && v.getVoto() == TipoVoto.COM_RELATOR)
            .count();

    long divergente = votos.stream()
            .filter(v -> !v.isAusente() && v.getVoto() == TipoVoto.DIVERGENTE)
            .count();

    long ausentes = votos.stream()
            .filter(Voto::isAusente)
            .count();

    Map<String, Long> resultado = new HashMap<>();
    resultado.put("COM_RELATOR", comRelator);
    resultado.put("DIVERGENTE", divergente);
    resultado.put("AUSENTES", ausentes);
    resultado.put("TOTAL", (long) votos.size());

    return resultado;
}

/**
 * Julga um processo, definindo o resultado
 */
@Transactional
public void julgarProcesso(Long processoId, String resultado) {
    Processo processo = buscarPorId(processoId);
    if (processo != null) {
        processo.setResultado(resultado);
        processo.setDataJulgamento(LocalDate.now());
        processo.setStatus("JULGADO");
        processoRepository.save(processo);
    }
}
```

**Justificativa:** `contarVotos()` agrega os votos por tipo para exibição na página de condução. `julgarProcesso()` registra a decisão final do colegiado.

---

### 8. layout.html

**Antes:**
```html
<nav class="navbar ...">
    <!-- 60+ linhas de código do navbar inline -->
</nav>

<main>
    <div th:if="${msg}" class="alert ...">...</div>
    <div th:if="${errorMsg}" class="alert ...">...</div>
    <div th:replace="${content}"></div>
</main>

<footer>...</footer>
```

**Depois:**
```html
<!-- Navbar -->
<div th:replace="~{fragments/navbar :: navbar}"></div>

<main class="container my-4 flex-grow-1">
    <!-- Alertas -->
    <div th:replace="~{fragments/alerts :: alerts}"></div>
    <div th:replace="${content}"></div>
</main>

<!-- Footer -->
<div th:replace="~{fragments/footer :: footer}"></div>
```

**Justificativa:** Layout mais limpo e manutenível. Alterações no navbar ou footer são feitas em um único lugar.

---

### 9. ReuniaoCoordenadorController.java (Novo)

**Endpoints implementados:**

| Método | URL | Descrição |
|--------|-----|-----------|
| GET | `/coord/reunioes` | Lista reuniões com filtros |
| GET | `/coord/reunioes/nova` | Formulário nova reunião |
| GET | `/coord/reunioes/editar/{id}` | Formulário editar reunião |
| POST | `/coord/reunioes/salvar` | Salva reunião |
| POST | `/coord/reunioes/excluir/{id}` | Exclui reunião |
| GET | `/coord/reunioes/{id}/pauta` | Gerencia pauta |
| POST | `/coord/reunioes/{id}/pauta/adicionar` | Adiciona processo |
| POST | `/coord/reunioes/{id}/pauta/remover/{pid}` | Remove processo |
| POST | `/coord/reunioes/{id}/iniciar` | Inicia sessão |
| GET | `/coord/reunioes/{id}/conduzir` | Página de condução |
| POST | `/coord/reunioes/{rid}/julgar/{pid}` | Julga processo |
| POST | `/coord/reunioes/{id}/finalizar` | Finaliza sessão |

---

## Fluxo de Funcionamento

### Ciclo de Vida de uma Sessão

```
┌─────────────────────────────────────────────────────────────────┐
│                    FLUXO DA SESSÃO                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. CRIAR REUNIÃO                                               │
│     └─> Status: PROGRAMADA                                      │
│         └─> Coord seleciona data e colegiado                    │
│                                                                 │
│  2. DEFINIR PAUTA                                               │
│     └─> Coord adiciona processos DISTRIBUIDOS                   │
│         └─> Processos são vinculados à reunião                  │
│                                                                 │
│  3. INICIAR SESSÃO                                              │
│     └─> Status: EM_ANDAMENTO                                    │
│         └─> Registra dataHoraInicio                             │
│                                                                 │
│  4. CONDUZIR SESSÃO                                             │
│     └─> Para cada processo:                                     │
│         ├─> Visualizar votos dos membros                        │
│         ├─> Contar: COM_RELATOR / DIVERGENTE / AUSENTES         │
│         └─> Registrar decisão: DEFERIDO/INDEFERIDO/RETIRADO     │
│                                                                 │
│  5. FINALIZAR SESSÃO                                            │
│     └─> Status: ENCERRADA                                       │
│         └─> Registra dataHoraFim                                │
│         └─> (Habilitado só após julgar todos os processos)      │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### Estados do Processo

```
CRIADO ──> DISTRIBUIDO ──> JULGADO
  │            │              │
  │            │              └─> resultado: DEFERIDO
  │            │                             INDEFERIDO
  │            │                             RETIRADO_DE_PAUTA
  │            │
  │            └─> Processo vinculado a uma reunião (pauta)
  │
  └─> Processo criado pelo aluno
```

---

## Como Testar

### Pré-requisitos
1. PostgreSQL rodando com banco configurado
2. Projeto compilado (`mvn compile`)

### Passo a Passo

#### 1. Criar dados de teste
- Login como `admin` / `123456`
- Criar um colegiado com membros
- Criar alunos e processos

#### 2. Distribuir processos
- Login como `coordenador` / `123456`
- Menu: Painel do Coordenador > Processos
- Distribuir processos para professores relatores

#### 3. Registrar votos (opcional)
- Login como `professor` / `123456`
- Menu: Painel do Professor > Meus Processos
- Clicar em "Votar" e registrar voto

#### 4. Criar e conduzir sessão
- Login como `coordenador` / `123456`
- Menu: Painel do Coordenador > Reuniões
- Clicar em "Nova Reunião"
- Preencher data e selecionar colegiado
- Clicar em "Salvar"
- Na lista, clicar no ícone de pauta
- Adicionar processos à pauta
- Voltar à lista e clicar em "Iniciar"
- Na página de condução, expandir cada processo
- Visualizar votos e clicar em Deferir/Indeferir/Retirar
- Após julgar todos, clicar em "Finalizar Sessão"

### URLs de Teste

```
http://localhost:8080/auth/login           - Login
http://localhost:8080/coord/reunioes       - Lista reuniões
http://localhost:8080/coord/reunioes/nova  - Nova reunião
http://localhost:8080/coord/reunioes/1/pauta    - Gerenciar pauta (ID=1)
http://localhost:8080/coord/reunioes/1/conduzir - Conduzir sessão (ID=1)
```

---

## Arquivos Excluídos

Nenhum arquivo foi excluído nesta implementação.

---

## Observações Técnicas

### Decisões de Design

1. **Resultado como String:** O campo `resultado` em Processo é String ao invés de enum para flexibilidade futura.

2. **Contagem de votos no controller:** A contagem é feita no controller e passada como Map para o template, evitando lógica complexa no Thymeleaf.

3. **Validação de finalização:** O botão de finalizar só é habilitado quando `totalJulgados >= totalProcessos`, garantindo que todos os processos sejam julgados.

4. **Fragmentos com namespace:** Os fragmentos mantêm o namespace `sec:authorize` para funcionar corretamente com Spring Security.

### Possíveis Melhorias Futuras

1. Implementar REQFUNC 6 (reuniões agendadas para o professor)
2. Implementar REQFUNC 16 (upload de PDF)
3. Implementar REQNAOFUNC 9 (paginação)
4. Adicionar geração automática de ata em PDF
5. Notificações por email para membros do colegiado

---

**Documento gerado em:** 27/01/2026
**Autores:** Felipe de Brito, Joana Elise
