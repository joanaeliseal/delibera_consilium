# Arquitetura do Projeto Delibera Consilium

**Versão:** 1.0.0
**Data:** 27 de Janeiro de 2026

---

## 1. Visão Geral da Arquitetura

O projeto segue o padrão **MVC (Model-View-Controller)** com uma arquitetura em camadas:

```
┌─────────────────────────────────────────────────────────────────┐
│                         BROWSER                                 │
│                    (HTML/CSS/JavaScript)                        │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                     SPRING SECURITY                             │
│              (Autenticação e Autorização)                       │
│         SecurityConfig.java + Tabelas users/authorities         │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                      CONTROLLERS                                │
│     (Recebem requisições HTTP e retornam Views/Redirects)       │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                       SERVICES                                  │
│            (Lógica de negócio e transações)                     │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                     REPOSITORIES                                │
│              (Acesso a dados via Spring Data JPA)               │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                      POSTGRESQL                                 │
│                  (Banco de Dados Relacional)                    │
└─────────────────────────────────────────────────────────────────┘
```

---

## 2. Estrutura de Pacotes

```
br.edu.ifpb.pweb2.delibera_consilium/
├── config/
│   └── SecurityConfig.java          # Configuração Spring Security
├── controller/
│   ├── AuthController.java          # Login/Logout
│   ├── HomeController.java          # Página inicial
│   ├── AlunoController.java         # CRUD Alunos (Admin)
│   ├── ProfessorController.java     # CRUD Professores (Admin)
│   ├── AssuntoController.java       # CRUD Assuntos (Admin)
│   ├── ColegiadoController.java     # CRUD Colegiados (Admin)
│   ├── ProcessoAlunoController.java      # Processos (Aluno)
│   ├── ProcessoProfessorController.java  # Processos (Professor)
│   ├── ProcessoCoordenadorController.java # Processos (Coordenador)
│   ├── ReuniaoProfessorController.java   # Reuniões (Professor)
│   ├── ReuniaoCoordenadorController.java # Reuniões (Coordenador)
│   └── VotoProfessorController.java      # Votação (Professor)
├── model/
│   ├── Aluno.java
│   ├── Professor.java
│   ├── Assunto.java
│   ├── Colegiado.java
│   ├── Processo.java
│   ├── Reuniao.java
│   ├── Voto.java
│   ├── TipoVoto.java           # Enum
│   ├── TipoDecisao.java        # Enum
│   └── StatusReuniao.java      # Enum
├── repository/
│   ├── AlunoRepository.java
│   ├── ProfessorRepository.java
│   ├── AssuntoRepository.java
│   ├── ColegiadoRepository.java
│   ├── ProcessoRepository.java
│   ├── ReuniaoRepository.java
│   └── VotoRepository.java
├── service/
│   ├── AlunoService.java
│   ├── ProfessorService.java
│   ├── AssuntoService.java
│   ├── ColegiadoService.java
│   ├── ProcessoService.java
│   ├── ReuniaoService.java
│   └── VotoService.java
├── util/
│   └── PasswordUtil.java        # Utilitários de senha
└── validator/
    ├── Matricula.java           # Anotação customizada
    └── MatriculaValidator.java  # Validador
```

---

## 3. Diagrama de Entidades e Relacionamentos

```
┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│   ALUNO     │       │  PROFESSOR  │       │   ASSUNTO   │
├─────────────┤       ├─────────────┤       ├─────────────┤
│ id (PK)     │       │ id (PK)     │       │ id (PK)     │
│ nome        │       │ nome        │       │ nome        │
│ matricula   │       │ matricula   │       └──────┬──────┘
│ fone        │       │ fone        │              │
│ login       │       │ login       │              │
│ senha       │       │ senha       │              │
└──────┬──────┘       │ coordenador │              │
       │              └──────┬──────┘              │
       │                     │                     │
       │    ┌────────────────┼─────────────────────┤
       │    │                │                     │
       ▼    ▼                ▼                     ▼
┌──────────────────────────────────────────────────────────┐
│                        PROCESSO                          │
├──────────────────────────────────────────────────────────┤
│ id (PK)                                                  │
│ numero (único, formato: ANO-HASH)                        │
│ textoRequerimento (TEXT)                                 │
│ dataRecepcao                                             │
│ dataDistribuicao                                         │
│ dataParecer                                              │
│ parecer (BLOB)                                           │
│ status (CRIADO | DISTRIBUIDO | JULGADO)                  │
│ resultado (DEFERIDO | INDEFERIDO | RETIRADO_DE_PAUTA)    │
│ dataJulgamento                                           │
│ interessado_id (FK -> Aluno)                             │
│ assunto_id (FK -> Assunto)                               │
│ relator_id (FK -> Professor)                             │
│ reuniao_id (FK -> Reuniao)                               │
└──────────────────────────────────────────────────────────┘
       │                                    │
       │                                    │
       ▼                                    ▼
┌─────────────┐                    ┌─────────────────┐
│    VOTO     │                    │    REUNIAO      │
├─────────────┤                    ├─────────────────┤
│ id (PK)     │                    │ id (PK)         │
│ voto (ENUM) │                    │ dataReuniao     │
│ ausente     │                    │ status (ENUM)   │
│ justificativa│                   │ dataHoraInicio  │
│ processo_id │                    │ dataHoraFim     │
│ professor_id│                    │ ata (BLOB)      │
└─────────────┘                    │ colegiado_id    │
                                   └────────┬────────┘
                                            │
                                            ▼
                                   ┌─────────────────┐
                                   │   COLEGIADO     │
                                   ├─────────────────┤
                                   │ id (PK)         │
                                   │ curso           │
                                   │ descricao       │
                                   │ portaria        │
                                   │ dataInicio      │
                                   │ dataFim         │
                                   └────────┬────────┘
                                            │
                                            │ ManyToMany
                                            ▼
                                   ┌─────────────────┐
                                   │COLEGIADO_MEMBROS│
                                   ├─────────────────┤
                                   │ colegiado_id    │
                                   │ professor_id    │
                                   └─────────────────┘
```

---

## 4. Relacionamentos entre Classes

### 4.1 Aluno → Processo (OneToMany)
```java
// Aluno.java
@OneToMany(mappedBy = "interessado")
private List<Processo> processos;

// Processo.java
@ManyToOne
@JoinColumn(name = "aluno_id")
private Aluno interessado;
```
**Significado:** Um aluno pode ter vários processos. Cada processo pertence a um aluno.

---

### 4.2 Professor → Processo (OneToMany)
```java
// Professor.java
@OneToMany(mappedBy = "relator")
private List<Processo> processosRelator;

// Processo.java
@ManyToOne
@JoinColumn(name = "relator_id")
private Professor relator;
```
**Significado:** Um professor pode ser relator de vários processos. Cada processo tem um relator.

---

### 4.3 Assunto → Processo (OneToMany)
```java
// Processo.java
@ManyToOne
@JoinColumn(name = "assunto_id")
private Assunto assunto;
```
**Significado:** Cada processo tem um assunto. Um assunto pode estar em vários processos.

---

### 4.4 Colegiado ↔ Professor (ManyToMany)
```java
// Colegiado.java
@ManyToMany
@JoinTable(
    name = "colegiado_membros",
    joinColumns = @JoinColumn(name = "colegiado_id"),
    inverseJoinColumns = @JoinColumn(name = "professor_id")
)
private List<Professor> membros;

// Professor.java
@ManyToMany(mappedBy = "membros")
private List<Colegiado> colegiados;
```
**Significado:** Um colegiado tem vários professores membros. Um professor pode participar de vários colegiados.

---

### 4.5 Colegiado → Reuniao (OneToMany)
```java
// Colegiado.java
@OneToMany(mappedBy = "colegiado")
private List<Reuniao> reunioes;

// Reuniao.java
@ManyToOne
@JoinColumn(name = "colegiado_id")
private Colegiado colegiado;
```
**Significado:** Um colegiado pode ter várias reuniões. Cada reunião pertence a um colegiado.

---

### 4.6 Reuniao → Processo (OneToMany)
```java
// Reuniao.java
@OneToMany(mappedBy = "reuniao")
private List<Processo> processosEmPauta;

// Processo.java
@ManyToOne
@JoinColumn(name = "reuniao_id")
private Reuniao reuniao;
```
**Significado:** Uma reunião pode ter vários processos em pauta. Um processo pode estar em uma reunião.

---

### 4.7 Processo → Voto (OneToMany)
```java
// Processo.java
@OneToMany(mappedBy = "processo")
private List<Voto> votos;

// Voto.java
@ManyToOne
@JoinColumn(name = "processo_id")
private Processo processo;
```
**Significado:** Um processo pode ter vários votos (um de cada membro). Cada voto é para um processo.

---

### 4.8 Professor → Voto (OneToMany)
```java
// Voto.java
@ManyToOne
@JoinColumn(name = "professor_id")
private Professor professor;
```
**Significado:** Um professor pode dar vários votos (em diferentes processos). Cada voto é de um professor.

---

## 5. Fluxo de Dados Principal

### Ciclo de Vida de um Processo

```
┌──────────────────────────────────────────────────────────────────┐
│                    FLUXO DO PROCESSO                             │
└──────────────────────────────────────────────────────────────────┘

1. CRIAÇÃO (Aluno)
   ┌─────────────────────────────────────────────────────────────┐
   │ Aluno preenche formulário → ProcessoAlunoController.salvar()│
   │ → ProcessoService.salvar()                                  │
   │   • Gera número único: ANO-HASH                             │
   │   • Define dataRecepcao = hoje                              │
   │   • Define status = "CRIADO"                                │
   │ → ProcessoRepository.save()                                 │
   └─────────────────────────────────────────────────────────────┘
                              │
                              ▼
2. DISTRIBUIÇÃO (Coordenador)
   ┌─────────────────────────────────────────────────────────────┐
   │ Coordenador seleciona relator → ProcessoCoordenador.        │
   │ distribuir() → ProcessoService.distribuirProcesso()         │
   │   • Define relator = professor selecionado                  │
   │   • Define dataDistribuicao = hoje                          │
   │   • Define status = "DISTRIBUIDO"                           │
   │ → ProcessoRepository.save()                                 │
   └─────────────────────────────────────────────────────────────┘
                              │
                              ▼
3. VOTAÇÃO (Professores)
   ┌─────────────────────────────────────────────────────────────┐
   │ Professor acessa processo → VotoProfessorController.        │
   │ formVotar() → VotoService.registrarVoto()                   │
   │   • Cria/atualiza Voto                                      │
   │   • Tipo: COM_RELATOR ou DIVERGENTE                         │
   │   • Opcional: justificativa                                 │
   │ → VotoRepository.save()                                     │
   └─────────────────────────────────────────────────────────────┘
                              │
                              ▼
4. CRIAÇÃO DE SESSÃO (Coordenador)
   ┌─────────────────────────────────────────────────────────────┐
   │ Coordenador cria reunião → ReuniaoCoordenador.salvar()      │
   │ → ReuniaoService.criarSessao()                              │
   │   • Define status = "PROGRAMADA"                            │
   │ → ReuniaoRepository.save()                                  │
   └─────────────────────────────────────────────────────────────┘
                              │
                              ▼
5. DEFINIÇÃO DE PAUTA (Coordenador)
   ┌─────────────────────────────────────────────────────────────┐
   │ Coordenador adiciona processos → gerenciarPauta()           │
   │ → ReuniaoService.adicionarProcessoAPauta()                  │
   │   • processo.reuniao = reuniao                              │
   │ → ProcessoRepository.save()                                 │
   └─────────────────────────────────────────────────────────────┘
                              │
                              ▼
6. INÍCIO DA SESSÃO (Coordenador)
   ┌─────────────────────────────────────────────────────────────┐
   │ Coordenador inicia → ReuniaoCoordenador.iniciarSessao()     │
   │ → ReuniaoService.iniciarSessao()                            │
   │   • Define status = "EM_ANDAMENTO"                          │
   │   • Define dataHoraInicio = agora                           │
   │ → ReuniaoRepository.save()                                  │
   └─────────────────────────────────────────────────────────────┘
                              │
                              ▼
7. JULGAMENTO (Coordenador)
   ┌─────────────────────────────────────────────────────────────┐
   │ Para cada processo na pauta:                                │
   │ Coordenador visualiza votos → ProcessoService.contarVotos() │
   │ Coordenador julga → ReuniaoCoordenador.julgarProcesso()     │
   │ → ProcessoService.julgarProcesso()                          │
   │   • Define resultado (DEFERIDO/INDEFERIDO/RETIRADO)         │
   │   • Define dataJulgamento = hoje                            │
   │   • Define status = "JULGADO"                               │
   │ → ProcessoRepository.save()                                 │
   └─────────────────────────────────────────────────────────────┘
                              │
                              ▼
8. FINALIZAÇÃO (Coordenador)
   ┌─────────────────────────────────────────────────────────────┐
   │ Coordenador finaliza → ReuniaoCoordenador.finalizarSessao() │
   │ → ReuniaoService.finalizarSessao()                          │
   │   • Define status = "ENCERRADA"                             │
   │   • Define dataHoraFim = agora                              │
   │ → ReuniaoRepository.save()                                  │
   └─────────────────────────────────────────────────────────────┘
```

---

## 6. Sistema de Autenticação e Autorização

### 6.1 Tabelas do Spring Security (PostgreSQL)

```sql
-- Tabela de usuários
CREATE TABLE users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(500) NOT NULL,  -- BCrypt hash
    enabled BOOLEAN NOT NULL
);

-- Tabela de permissões
CREATE TABLE authorities (
    username VARCHAR(50) REFERENCES users(username),
    authority VARCHAR(50) NOT NULL
);
```

### 6.2 Papéis (Roles) e Permissões

| Role | Acesso | Funcionalidades |
|------|--------|-----------------|
| `ROLE_ADMIN` | `/admin/**` | CRUD de Alunos, Professores, Assuntos, Colegiados |
| `ROLE_COORDENADOR` | `/coord/**` | Distribuir processos, Gerenciar reuniões, Julgar |
| `ROLE_PROFESSOR` | `/professor/**` | Ver processos atribuídos, Votar, Ver reuniões |
| `ROLE_ALUNO` | `/aluno/**` | Criar e consultar próprios processos |

### 6.3 Configuração de Segurança

```java
// SecurityConfig.java
http.authorizeHttpRequests(auth -> auth
    .requestMatchers("/admin/**").hasRole("ADMIN")
    .requestMatchers("/coord/**").hasRole("COORDENADOR")
    .requestMatchers("/professor/**").hasAnyRole("PROFESSOR", "COORDENADOR")
    .requestMatchers("/aluno/**").hasRole("ALUNO")
    .anyRequest().authenticated()
);
```

---

## 7. Enumerações

### StatusReuniao
```java
public enum StatusReuniao {
    PROGRAMADA,      // Reunião agendada
    EM_ANDAMENTO,    // Reunião em curso
    ENCERRADA        // Reunião finalizada
}
```

### TipoVoto
```java
public enum TipoVoto {
    COM_RELATOR,     // Concorda com o relator
    DIVERGENTE       // Discorda do relator
}
```

### TipoDecisao
```java
public enum TipoDecisao {
    DEFERIMENTO,
    INDEFERIMENTO
}
```

---

## 8. Padrões de Projeto Utilizados

1. **MVC (Model-View-Controller)**
   - Model: Entidades JPA
   - View: Templates Thymeleaf
   - Controller: Classes @Controller

2. **Repository Pattern**
   - Interfaces que estendem JpaRepository
   - Métodos de consulta derivados e @Query

3. **Service Layer**
   - Lógica de negócio centralizada
   - Transações com @Transactional

4. **Dependency Injection**
   - @Autowired / Constructor Injection
   - Inversão de controle do Spring

5. **Post-Redirect-Get (PRG)**
   - Evita resubmissão de formulários
   - RedirectAttributes para mensagens flash

6. **Template Method**
   - Layout base com fragmentos reutilizáveis
   - th:replace para composição

---

## 9. Tecnologias e Dependências

| Tecnologia | Versão | Propósito |
|------------|--------|-----------|
| Java | 17 | Linguagem de programação |
| Spring Boot | 3.5.7 | Framework principal |
| Spring Data JPA | - | Persistência de dados |
| Spring Security | 6.5.6 | Autenticação/Autorização |
| Thymeleaf | 3.x | Template engine |
| PostgreSQL | 42.7.8 | Banco de dados |
| Bootstrap | 5.3.0 | Framework CSS |
| Lombok | - | Redução de boilerplate |
| Maven | 3.6+ | Gerenciamento de build |

---

**Documento criado em:** 27/01/2026
**Autores:** Felipe de Brito, Joana Elise
