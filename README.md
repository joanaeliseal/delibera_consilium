# Delibera Consilium

Sistema de gerenciamento de processos e deliberações para colegiados acadêmicos.

## Contexto do Projeto

**Delibera Consilium** é uma aplicação web desenvolvida como projeto acadêmico para a disciplina de **Programacao Web II (PWEB2)** do Instituto Federal de Educacao, Ciencia e Tecnologia da Paraiba (IFPB).

O nome "Delibera Consilium" e uma expressao em latim que significa "Deliberacao do Conselho", refletindo o proposito da aplicacao de gerenciar o fluxo de julgamento de processos em colegiados de professores.

## Tecnologias Utilizadas

| Tecnologia | Versao | Proposito |
|------------|--------|-----------|
| Java | 17 | Linguagem de programacao |
| Spring Boot | 3.5.7 | Framework principal |
| Spring Data JPA | - | Persistencia de dados |
| Spring Security | 6.x | Autenticacao e autorizacao |
| Thymeleaf | 3.x | Template engine |
| PostgreSQL | 42.x | Banco de dados |
| Bootstrap | 5.3 | Framework CSS |
| Lombok | - | Reducao de boilerplate |
| Maven | 3.6+ | Gerenciamento de build |

## Funcionalidades

### Perfis de Usuario

| Perfil | Funcionalidades |
|--------|-----------------|
| **Admin** | CRUD de alunos, professores, assuntos e colegiados |
| **Coordenador** | Distribuir processos, gerenciar reunioes, conduzir sessoes, julgar processos |
| **Professor** | Visualizar processos atribuidos, votar, consultar reunioes |
| **Aluno** | Criar processos, fazer upload de PDF, acompanhar andamento |

### Fluxo Principal

```
1. Aluno cria processo com requerimento
           |
2. Coordenador distribui para um relator
           |
3. Relator registra sua decisao (DEFERIMENTO/INDEFERIMENTO)
           |
4. Membros do colegiado votam (COM_RELATOR/DIVERGENTE)
           |
5. Coordenador cria sessao e define pauta
           |
6. Coordenador conduz sessao e julga processos
           |
7. Sistema calcula resultado automaticamente pela maioria
```

## Estrutura do Projeto

```
delibera_consilium/
├── src/main/java/br/edu/ifpb/pweb2/delibera_consilium/
│   ├── config/
│   │   ├── DataInitializer.java      # Dados iniciais
│   │   ├── SecurityConfig.java       # Configuracao Spring Security
│   │   └── WebConfig.java            # Converters do Spring MVC
│   ├── controller/
│   │   ├── AlunoController.java           # CRUD Alunos (Admin)
│   │   ├── AssuntoController.java         # CRUD Assuntos (Admin)
│   │   ├── AuthController.java            # Login/Logout
│   │   ├── ColegiadoController.java       # CRUD Colegiados (Admin)
│   │   ├── HomeController.java            # Pagina inicial
│   │   ├── ProcessoAlunoController.java   # Processos (Aluno)
│   │   ├── ProcessoCoordenadorController.java  # Processos (Coordenador)
│   │   ├── ProcessoProfessorController.java    # Processos (Professor)
│   │   ├── ProfessorController.java       # CRUD Professores (Admin)
│   │   ├── ReuniaoCoordenadorController.java   # Reunioes (Coordenador)
│   │   ├── ReuniaoProfessorController.java     # Reunioes (Professor)
│   │   └── VotoProfessorController.java        # Votacao (Professor)
│   ├── model/
│   │   ├── Aluno.java
│   │   ├── Assunto.java
│   │   ├── Colegiado.java
│   │   ├── Processo.java
│   │   ├── Professor.java
│   │   ├── Reuniao.java
│   │   ├── StatusReuniao.java    # PROGRAMADA, EM_ANDAMENTO, ENCERRADA
│   │   ├── TipoDecisao.java      # DEFERIMENTO, INDEFERIMENTO
│   │   ├── TipoVoto.java         # COM_RELATOR, DIVERGENTE
│   │   └── Voto.java
│   ├── repository/
│   │   ├── AlunoRepository.java
│   │   ├── AssuntoRepository.java
│   │   ├── ColegiadoRepository.java
│   │   ├── ProcessoRepository.java
│   │   ├── ProfessorRepository.java
│   │   ├── ReuniaoRepository.java
│   │   └── VotoRepository.java
│   ├── security/
│   │   ├── CustomUserDetailsService.java
│   │   └── SecurityUtils.java
│   ├── service/
│   │   ├── AlunoService.java
│   │   ├── AssuntoService.java
│   │   ├── ColegiadoService.java
│   │   ├── ProcessoService.java
│   │   ├── ProfessorService.java
│   │   ├── ReuniaoService.java
│   │   └── VotoService.java
│   └── validator/
│       ├── Matricula.java            # Anotacao customizada
│       └── MatriculaValidator.java   # Validador de matricula
├── src/main/resources/
│   ├── application.properties
│   └── templates/
│       ├── layout.html
│       ├── fragments/
│       │   ├── navbar.html
│       │   ├── footer.html
│       │   ├── alerts.html
│       │   └── pagination.html
│       ├── admin/
│       │   ├── aluno/
│       │   ├── assunto/
│       │   ├── colegiado/
│       │   └── professor/
│       ├── aluno/
│       │   └── processo/
│       ├── coord/
│       │   ├── processo/
│       │   └── reuniao/
│       └── professor/
│           ├── processo/
│           ├── reuniao/
│           └── voto/
├── docs/
│   ├── Projeto Collegialis.pdf       # Especificacao do projeto
│   ├── arquitetura-projeto.md        # Documentacao da arquitetura
│   ├── etapa2-implementacao.md       # Detalhes da Etapa II
│   └── relatorio-analise-erros-etapa2.md  # Analise e correcoes
└── pom.xml
```

## Configuracao e Execucao

### Pre-requisitos

- Java 17 ou superior
- PostgreSQL configurado e em execucao
- Maven 3.6 ou superior

### Instalacao

1. Clone o repositorio:
```bash
git clone https://github.com/joanaeliseal/delibera_consilium.git
cd delibera_consilium
```

2. Crie o banco de dados:
```sql
CREATE DATABASE delibera_consilium;
```

3. Configure o banco de dados em `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/delibera_consilium
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

4. Compile e execute:
```bash
mvn clean install
mvn spring-boot:run
```

5. Acesse: `http://localhost:8080`

### Usuarios Padrao (DataInitializer)

| Usuario | Senha | Perfil |
|---------|-------|--------|
| admin | admin | ADMIN |
| coordenador | 123 | COORDENADOR |
| professor | 123 | PROFESSOR |
| aluno | 123 | ALUNO |

## Requisitos Implementados

### Etapa II - 100/100 pontos

| Requisito | Descricao | Status |
|-----------|-----------|:------:|
| REQFUNC 4 | Professor consulta reunioes com filtro | ✅ |
| REQFUNC 5 | Professor vota com justificativa | ✅ |
| REQFUNC 6 | Professor ve reunioes onde foi escalado | ✅ |
| REQFUNC 9 | Coordenador cria sessao, pauta e membros | ✅ |
| REQFUNC 10 | Coordenador inicia sessao (unica) | ✅ |
| REQFUNC 11 | Coordenador apregoa e julga (calculo automatico) | ✅ |
| REQFUNC 12 | Coordenador finaliza sessao | ✅ |
| REQFUNC 16 | Upload PDF do requerimento | ✅ |
| REQNAOFUNC 7 | Layouts e fragmentos Thymeleaf | ✅ |
| REQNAOFUNC 8 | Autenticacao/Autorizacao Spring Security | ✅ |
| REQNAOFUNC 9 | Paginacao com reflexo no banco | ✅ |
| REQNAOFUNC 10 | Anotacao customizada @Matricula | ✅ |

## Arquitetura

O projeto segue o padrao **MVC (Model-View-Controller)** com arquitetura em camadas:

```
Browser (HTML/CSS/JS)
        ↓
Spring Security (Autenticacao/Autorizacao)
        ↓
Controllers (Requisicoes HTTP)
        ↓
Services (Logica de negocio)
        ↓
Repositories (Spring Data JPA)
        ↓
PostgreSQL (Banco de dados)
```

## Autores

Desenvolvido por: **Felipe de Brito** e **Joana Elise**

Disciplina: Programacao para Web II (PWEB2)
Instituicao: IFPB - Instituto Federal de Educacao, Ciencia e Tecnologia da Paraiba
Professor: Frederico Costa Guedes Pereira
Periodo: 2025.2

## Licenca

Este projeto e disponibilizado sob licenca aberta para fins academicos.
