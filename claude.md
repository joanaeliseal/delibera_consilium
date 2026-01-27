# 📊 STATUS DO PROJETO DELIBERA CONSILIUM

**Última atualização:** 27 de Janeiro de 2026
**Versão:** 1.4.0
**Disciplina:** Programação Web II (PWEB2) - IFPB

---

## 🎯 VISÃO GERAL DO PROJETO

Sistema web para gerenciamento de processos acadêmicos julgados por um colegiado de professores. Desenvolvido com Spring Boot 3.5.7, PostgreSQL e Bootstrap 5.

### Tecnologias Utilizadas
- **Backend:** Java 17, Spring Boot 3.5.7, Spring Data JPA, Spring Security
- **Frontend:** Thymeleaf, Bootstrap 5.3.0, Bootstrap Icons
- **Banco de Dados:** PostgreSQL
- **Build:** Maven 3.6+
- **IDE:** VSCode com extensões Spring Boot

---

## 📈 PROGRESSO GERAL

### Etapa I
✅ **Status:** COMPLETA
✅ **Pontuação:** 100/100 pontos
✅ **Taxa de Conclusão:** 100%

### Etapa II
🟢 **Status:** QUASE COMPLETA
🟢 **Pontuação:** 80/100 pontos
🟢 **Taxa de Conclusão:** 80%

### Progresso Total
```
██████████████████████████████████████░░ 90% (180/200 pontos)
```

---

## ✅ ETAPA I - REQUISITOS IMPLEMENTADOS (100/100)

### 🟢 REQFUNC 1 - Aluno cadastra processo (10 pts) ✅

**Status:** Implementado e funcional

**Localização:**
- Controller: `ProcessoAlunoController.java`
- Service: `ProcessoService.salvar()`
- Repository: `ProcessoRepository.java`
- Template: `aluno/processo/form.html`

**Funcionalidades:**
- ✅ Formulário de criação de processo
- ✅ Validação de campos obrigatórios (texto requerimento, assunto)
- ✅ Geração automática de número de protocolo (formato: ANO-HASH)
- ✅ Data de recepção definida automaticamente
- ✅ Status inicial: "CRIADO"
- ✅ Associação automática com aluno logado

---

### 🟢 REQFUNC 2 - Aluno consulta processos com filtros (10 pts) ✅

**Status:** Implementado e funcional

**Localização:**
- Controller: `ProcessoAlunoController.listar()`
- Service: `ProcessoService.listarPorInteressado()`
- Repository: `ProcessoRepository.findByAlunoFiltros()`
- Template: `aluno/processo/list.html`

---

### 🟢 REQFUNC 3 - Professor consulta processos designados (15 pts) ✅

**Status:** Implementado e funcional

**Localização:**
- Controller: `ProcessoProfessorController.listarAtribuidos()`
- Service: `ProcessoService.listarPorRelator()`
- Repository: `ProcessoRepository.findByRelator()`
- Template: `professor/processo/list.html`

---

### 🟢 REQFUNC 7 - Coordenador filtra processos (15 pts) ✅

**Status:** Implementado e funcional

**Localização:**
- Controller: `ProcessoCoordenadorController.listar()`
- Service: `ProcessoService.listarComFiltros()`
- Repository: `ProcessoRepository.findByFiltros()`
- Template: `coord/processo/list.html`

---

### 🟢 REQFUNC 8 - Coordenador distribui processo (10 pts) ✅

**Status:** Implementado e funcional

**Localização:**
- Controller: `ProcessoCoordenadorController.distribuir()`
- Service: `ProcessoService.distribuirProcesso()`
- Template: `coord/processo/list.html`

---

### 🟢 REQFUNC 13 - Admin CRUD Colegiados (5 pts) ✅

**Status:** Implementado e funcional

**Localização:**
- Controller: `ColegiadoController.java`
- Service: `ColegiadoService.java`
- Repository: `ColegiadoRepository.java`
- Templates: `admin/colegiado/list.html`, `admin/colegiado/form.html`

---

### 🟢 REQFUNC 14 - Admin CRUD Pessoas (15 pts) ✅

**Status:** Implementado e funcional

- Alunos: `AlunoController.java`, `AlunoService.java`
- Professores: `ProfessorController.java`, `ProfessorService.java`

---

### 🟢 REQFUNC 15 - Admin CRUD Assuntos (5 pts) ✅

**Status:** Implementado e funcional

**Localização:**
- Controller: `AssuntoController.java`
- Service: `AssuntoService.java`
- Repository: `AssuntoRepository.java`

---

### 🟢 REQNAOFUNC 2 - Framework CSS (5 pts) ✅

- ✅ Bootstrap 5.3.0 via CDN
- ✅ Bootstrap Icons 1.10.0

---

### 🟢 REQNAOFUNC 5 - Validações e mensagens (5 pts) ✅

- ✅ Bean Validation (@Valid, @NotBlank, @NotNull)
- ✅ Validação customizada: `@Matricula`

---

### 🟢 REQNAOFUNC 6 - Post-Redirect-Get (5 pts) ✅

- ✅ Todos os métodos POST retornam `redirect:`
- ✅ Uso de `RedirectAttributes` para flash messages

---

## ✅ ETAPA II - REQUISITOS IMPLEMENTADOS (80/100)

### 🟢 REQNAOFUNC 7 - Layouts e Fragmentos Thymeleaf (10 pts) ✅ **NOVO**

**Status:** Implementado e funcional

**Localização:**
- `templates/fragments/navbar.html` - Barra de navegação
- `templates/fragments/footer.html` - Rodapé
- `templates/fragments/alerts.html` - Mensagens flash
- `templates/fragments/pagination.html` - Paginação (preparado)
- `templates/layout.html` - Layout principal atualizado

**Funcionalidades:**
- ✅ Fragmentos extraídos em arquivos separados
- ✅ Navbar com menu dinâmico por role (`sec:authorize`)
- ✅ Footer reutilizável
- ✅ Alertas unificados (success, error, info, warning)
- ✅ Fragmento de paginação preparado para uso futuro

---

### 🟢 REQNAOFUNC 8 - Spring Security (10 pts) ✅

**Status:** Implementado e funcional

**Localização:**
- Config: `config/SecurityConfig.java`
- Controller: `controller/AuthController.java`
- Templates: `auth/login.html`, `auth/acesso-negado.html`

**Usuários de Teste:**
| Usuário | Senha | Roles |
|---------|-------|-------|
| `aluno` | `123456` | ROLE_ALUNO |
| `professor` | `123456` | ROLE_PROFESSOR |
| `coordenador` | `123456` | ROLE_COORDENADOR, ROLE_PROFESSOR |
| `admin` | `123456` | ROLE_ADMIN |

---

### 🟢 REQNAOFUNC 10 - Validador customizado matrícula (5 pts) ✅

**Localização:**
- Anotação: `validator/Matricula.java`
- Validador: `validator/MatriculaValidator.java`

---

### 🟢 REQFUNC 4 - Professor consulta reuniões com filtro (10 pts) ✅

**Localização:**
- Controller: `ReuniaoProfessorController.java`
- Service: `ReuniaoService.java`
- Template: `professor/reuniao/list.html`

---

### 🟢 REQFUNC 5 - Professor vota com justificativa (5 pts) ✅

**Localização:**
- Controller: `VotoProfessorController.java`
- Service: `VotoService.java`
- Template: `professor/voto/form.html`

---

### 🟢 REQFUNC 9 - Coordenador cria sessão e define pauta (15 pts) ✅ **NOVO**

**Status:** Implementado e funcional

**Localização:**
- Controller: `ReuniaoCoordenadorController.java`
- Service: `ReuniaoService.java` (métodos adicionados)
- Model: `Reuniao.java` (campos adicionados)
- Model: `StatusReuniao.java` (enum atualizado)
- Templates: `coord/reuniao/list.html`, `form.html`, `pauta.html`

**Funcionalidades:**
- ✅ CRUD completo de reuniões
- ✅ Filtro por status e colegiado
- ✅ Gerenciamento de pauta (adicionar/remover processos)
- ✅ Processos disponíveis: DISTRIBUIDOS sem reunião
- ✅ Status: PROGRAMADA → EM_ANDAMENTO → ENCERRADA

**Endpoints:**
```
GET  /coord/reunioes              - Lista reuniões
GET  /coord/reunioes/nova         - Formulário nova reunião
POST /coord/reunioes/salvar       - Salva reunião
GET  /coord/reunioes/{id}/pauta   - Gerencia pauta
POST /coord/reunioes/{id}/pauta/adicionar    - Adiciona processo
POST /coord/reunioes/{id}/pauta/remover/{pid} - Remove processo
```

---

### 🟢 REQFUNC 10 - Coordenador inicia sessão (5 pts) ✅ **NOVO**

**Status:** Implementado e funcional

**Localização:**
- Controller: `ReuniaoCoordenadorController.iniciarSessao()`
- Service: `ReuniaoService.iniciarSessao()`

**Funcionalidades:**
- ✅ Botão "Iniciar" na lista de reuniões (apenas PROGRAMADA)
- ✅ Muda status para EM_ANDAMENTO
- ✅ Registra dataHoraInicio
- ✅ Redireciona para página de condução

**Endpoint:**
```
POST /coord/reunioes/{id}/iniciar - Inicia a sessão
```

---

### 🟢 REQFUNC 11 - Coordenador apregoa e julga processos (15 pts) ✅ **NOVO**

**Status:** Implementado e funcional

**Localização:**
- Controller: `ReuniaoCoordenadorController.conduzirSessao()`, `julgarProcesso()`
- Service: `ProcessoService.contarVotos()`, `julgarProcesso()`
- Model: `Processo.java` (campos `resultado`, `dataJulgamento`)
- Template: `coord/reuniao/conduzir.html`

**Funcionalidades:**
- ✅ Página de condução da sessão
- ✅ Accordion com processos da pauta
- ✅ Contagem de votos em tempo real (COM_RELATOR / DIVERGENTE / AUSENTES)
- ✅ Botões de julgamento: Deferir, Indeferir, Retirar de Pauta
- ✅ Barra de progresso do julgamento
- ✅ Visualização do texto do requerimento
- ✅ Status visual do processo (Pendente/Julgado)

**Endpoints:**
```
GET  /coord/reunioes/{id}/conduzir           - Página de condução
POST /coord/reunioes/{rid}/julgar/{pid}      - Julga processo
```

---

### 🟢 REQFUNC 12 - Coordenador finaliza sessão (5 pts) ✅ **NOVO**

**Status:** Implementado e funcional

**Localização:**
- Controller: `ReuniaoCoordenadorController.finalizarSessao()`
- Service: `ReuniaoService.finalizarSessao()`

**Funcionalidades:**
- ✅ Botão "Finalizar Sessão" na página de condução
- ✅ Habilitado apenas quando todos os processos foram julgados
- ✅ Confirmação antes de finalizar
- ✅ Muda status para ENCERRADA
- ✅ Registra dataHoraFim

**Endpoint:**
```
POST /coord/reunioes/{id}/finalizar - Finaliza a sessão
```

---

## ⏳ ETAPA II - REQUISITOS PENDENTES (20/100)

### 🔴 REQFUNC 6 - Professor consulta reuniões agendadas (10 pts) ❌

**Status:** NÃO IMPLEMENTADO

**Observação:** Funcionalidade similar ao REQFUNC 4, pode ser adicionado filtro específico.

---

### 🔴 REQFUNC 16 - Upload de PDF do requerimento (5 pts) ❌

**Status:** NÃO IMPLEMENTADO

---

### 🔴 REQNAOFUNC 9 - Paginação com reflexo no banco (5 pts) ❌

**Status:** NÃO IMPLEMENTADO

**Observação:** Fragmento de paginação já criado, falta implementar nos repositórios.

---

## 📊 RESUMO DE PONTUAÇÃO

### Etapa I - Completa
| Requisito | Descrição | Pontos | Status |
|-----------|-----------|--------|--------|
| REQFUNC 1 | Aluno cadastra processo | 10 | ✅ |
| REQFUNC 2 | Aluno consulta processos | 10 | ✅ |
| REQFUNC 3 | Professor consulta atribuídos | 15 | ✅ |
| REQFUNC 7 | Coordenador filtra processos | 15 | ✅ |
| REQFUNC 8 | Coordenador distribui processo | 10 | ✅ |
| REQFUNC 13 | Admin CRUD Colegiados | 5 | ✅ |
| REQFUNC 14 | Admin CRUD Pessoas | 15 | ✅ |
| REQFUNC 15 | Admin CRUD Assuntos | 5 | ✅ |
| REQNAOFUNC 2 | Framework CSS | 5 | ✅ |
| REQNAOFUNC 5 | Validações | 5 | ✅ |
| REQNAOFUNC 6 | Post-Redirect-Get | 5 | ✅ |
| **TOTAL** | | **100** | **✅** |

### Etapa II - Quase Completa
| Requisito | Descrição | Pontos | Status |
|-----------|-----------|--------|--------|
| REQFUNC 4 | Professor consulta reuniões | 10 | ✅ |
| REQFUNC 5 | Professor vota com justificativa | 5 | ✅ |
| REQFUNC 6 | Professor reuniões agendadas | 10 | ❌ |
| REQFUNC 9 | Coordenador cria sessão | 15 | ✅ |
| REQFUNC 10 | Coordenador inicia sessão | 5 | ✅ |
| REQFUNC 11 | Coordenador apregoa processos | 15 | ✅ |
| REQFUNC 12 | Coordenador finaliza sessão | 5 | ✅ |
| REQFUNC 16 | Upload PDF requerimento | 5 | ❌ |
| REQNAOFUNC 7 | Layouts e Fragmentos | 10 | ✅ |
| REQNAOFUNC 8 | Spring Security | 10 | ✅ |
| REQNAOFUNC 9 | Paginação | 5 | ❌ |
| REQNAOFUNC 10 | Validador matrícula | 5 | ✅ |
| **TOTAL** | | **100** | **80/100** |

### Pontuação Total do Projeto
```
Etapa I:  100/100 (100%)
Etapa II:  80/100 (80%)
─────────────────────────
TOTAL:    180/200 (90%)
```

---

## 📁 ESTRUTURA DE ARQUIVOS

### Arquivos Criados na Etapa II (Sessão Atual)
```
src/main/resources/templates/
├── fragments/
│   ├── navbar.html          # NOVO - Fragmento navbar
│   ├── footer.html          # NOVO - Fragmento footer
│   ├── alerts.html          # NOVO - Fragmento alertas
│   └── pagination.html      # NOVO - Fragmento paginação
├── coord/reuniao/
│   ├── list.html            # NOVO - Lista reuniões
│   ├── form.html            # NOVO - Formulário reunião
│   ├── pauta.html           # NOVO - Gerenciar pauta
│   └── conduzir.html        # NOVO - Condução da sessão

src/main/java/.../
├── controller/
│   └── ReuniaoCoordenadorController.java  # NOVO
├── model/
│   ├── StatusReuniao.java   # MODIFICADO - Adicionado EM_ANDAMENTO
│   ├── Reuniao.java         # MODIFICADO - Campos de data/hora
│   └── Processo.java        # MODIFICADO - Campos resultado/dataJulgamento
├── repository/
│   ├── ReuniaoRepository.java   # MODIFICADO - Novos métodos
│   └── ProcessoRepository.java  # MODIFICADO - Novos métodos
├── service/
│   ├── ReuniaoService.java      # MODIFICADO - Métodos de sessão
│   └── ProcessoService.java     # MODIFICADO - Métodos de julgamento
└── templates/
    └── layout.html              # MODIFICADO - Usa fragmentos
```

---

## 🧪 COMO TESTAR

### Fluxo Completo de Sessão
1. Login: `coordenador` / `123456`
2. Menu: Painel do Coordenador > Reuniões
3. Criar nova reunião (botão "Nova Reunião")
4. Gerenciar pauta (adicionar processos distribuídos)
5. Iniciar sessão (botão "Iniciar")
6. Conduzir: julgar cada processo (Deferir/Indeferir/Retirar)
7. Finalizar sessão

### URLs de Teste
```
http://localhost:8080/auth/login         - Login
http://localhost:8080/coord/reunioes     - Lista reuniões
http://localhost:8080/coord/reunioes/nova - Nova reunião
http://localhost:8080/coord/reunioes/1/pauta - Gerenciar pauta
http://localhost:8080/coord/reunioes/1/conduzir - Conduzir sessão
```

---

## 🤝 CONTRIBUIDORES

- **Felipe de Brito** - Desenvolvedor
- **Joana Elise** - Desenvolvedora

**Disciplina:** Programação Web II (PWEB2)
**Professor:** Frederico Guedes Pereira
**Instituição:** IFPB - Instituto Federal de Educação, Ciência e Tecnologia da Paraíba

---

**Última atualização:** 27/01/2026
**Versão do documento:** 1.4.0
