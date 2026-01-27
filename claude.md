# 📊 STATUS DO PROJETO DELIBERA CONSILIUM

**Última atualização:** 26 de Janeiro de 2026
**Versão:** 1.2.0
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
🟡 **Status:** EM ANDAMENTO
🟡 **Pontuação:** 25/100 pontos
🟡 **Taxa de Conclusão:** 25%

### Progresso Total
```
████████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░ 57.5% (115/200 pontos)
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

**Como testar:**
1. Fazer login como aluno
2. Acessar `/aluno/processos`
3. Clicar em "Abrir Novo Processo"
4. Preencher formulário e enviar

---

### 🟢 REQFUNC 2 - Aluno consulta processos com filtros (10 pts) ✅

**Status:** Implementado e funcional

**Localização:**
- Controller: `ProcessoAlunoController.listar()`
- Service: `ProcessoService.listarPorInteressado()`
- Repository: `ProcessoRepository.findByAlunoFiltros()`
- Template: `aluno/processo/list.html`

**Funcionalidades:**
- ✅ Listagem de processos do aluno logado
- ✅ Filtro por status (CRIADO, DISTRIBUIDO, JULGADO)
- ✅ Filtro por assunto
- ✅ Ordenação por data (crescente/decrescente)
- ✅ Exibição de informações: protocolo, assunto, data, status, relator

**Filtros disponíveis:**
- Status: Todos / Criado / Distribuído / Julgado
- Assunto: Dropdown com todos os assuntos cadastrados
- Ordem: Mais Recentes / Mais Antigos

---

### 🟢 REQFUNC 3 - Professor consulta processos designados (15 pts) ✅

**Status:** Implementado e funcional

**Localização:**
- Controller: `ProcessoProfessorController.listarAtribuidos()`
- Service: `ProcessoService.listarPorRelator()`
- Repository: `ProcessoRepository.findByRelator()`
- Template: `professor/processo/list.html`

**Funcionalidades:**
- ✅ Listagem de processos onde professor é relator
- ✅ Identificação automática do professor logado via Spring Security
- ✅ Exibição de dados do processo e do aluno interessado
- ✅ Modal para leitura do requerimento
- ✅ Status do parecer (Pendente/Enviado)

---

### 🟢 REQFUNC 7 - Coordenador filtra processos (15 pts) ✅

**Status:** Implementado e funcional

**Localização:**
- Controller: `ProcessoCoordenadorController.listar()`
- Service: `ProcessoService.listarComFiltros()`
- Repository: `ProcessoRepository.findByFiltros()`
- Template: `coord/processo/list.html`

**Funcionalidades:**
- ✅ Listagem de TODOS os processos do colegiado
- ✅ Filtro por status
- ✅ Filtro por aluno interessado
- ✅ Filtro por professor relator
- ✅ Query JPQL customizada com múltiplos filtros opcionais

**Filtros disponíveis:**
- Status: Todos / Criado / Distribuído
- Aluno Interessado: Dropdown com todos os alunos
- Relator Atual: Dropdown com todos os professores

---

### 🟢 REQFUNC 8 - Coordenador distribui processo (10 pts) ✅

**Status:** Implementado e funcional

**Localização:**
- Controller: `ProcessoCoordenadorController.distribuir()`
- Service: `ProcessoService.distribuirProcesso()`
- Template: `coord/processo/list.html`

**Funcionalidades:**
- ✅ Formulário inline na lista de processos
- ✅ Seleção de professor relator via dropdown
- ✅ Atualização automática de:
  - Campo `relator` do processo
  - Data de distribuição (data atual)
  - Status do processo para "DISTRIBUIDO"
- ✅ Validação: processo e professor devem existir

**Como funciona:**
1. Coordenador acessa `/coord/processos`
2. Seleciona professor no dropdown ao lado do processo
3. Clica no botão de distribuir
4. Processo é atribuído e status atualizado

---

### 🟢 REQFUNC 13 - Admin CRUD Colegiados (5 pts) ✅

**Status:** Implementado e funcional

**Localização:**
- Controller: `ColegiadoController.java`
- Service: `ColegiadoService.java`
- Repository: `ColegiadoRepository.java`
- Templates: `admin/colegiado/list.html`, `admin/colegiado/form.html`

**Funcionalidades:**
- ✅ CREATE: Cadastrar novo colegiado
- ✅ READ: Listar todos os colegiados
- ✅ UPDATE: Editar colegiado existente
- ✅ DELETE: Excluir colegiado (com tratamento de erro)
- ✅ Seleção múltipla de professores como membros
- ✅ Validações: datas, portaria, curso, membros obrigatórios

**Campos do Colegiado:**
- Nome do Curso
- Data Início / Data Fim
- Descrição
- Portaria
- Membros (professores)

---

### 🟢 REQFUNC 14 - Admin CRUD Pessoas (15 pts) ✅

**Status:** Implementado e funcional

#### Alunos
**Localização:**
- Controller: `AlunoController.java`
- Service: `AlunoService.java`
- Repository: `AlunoRepository.java`
- Templates: `admin/aluno/list.html`, `admin/aluno/form.html`

**Funcionalidades:**
- ✅ CRUD completo de alunos
- ✅ Validação customizada de matrícula (@Matricula)
- ✅ Verificação de matrícula duplicada
- ✅ Campos: nome, matrícula, telefone, login, senha

#### Professores
**Localização:**
- Controller: `ProfessorController.java`
- Service: `ProfessorService.java`
- Repository: `ProfessorRepository.java`
- Templates: `admin/professor/list.html`, `admin/professor/form.html`

**Funcionalidades:**
- ✅ CRUD completo de professores
- ✅ Flag "É Coordenador?" (boolean)
- ✅ Campos: nome, matrícula, telefone, login, senha, coordenador

---

### 🟢 REQFUNC 15 - Admin CRUD Assuntos (5 pts) ✅

**Status:** Implementado e funcional

**Localização:**
- Controller: `AssuntoController.java`
- Service: `AssuntoService.java`
- Repository: `AssuntoRepository.java`
- Templates: `admin/assunto/list.html`, `admin/assunto/form.html`

**Funcionalidades:**
- ✅ CRUD completo de assuntos
- ✅ Validação: nome obrigatório
- ✅ Tratamento de erro na exclusão (se vinculado a processos)
- ✅ Interface simples e direta

---

### 🟢 REQNAOFUNC 2 - Framework CSS (5 pts) ✅

**Status:** Implementado

**Implementação:**
- ✅ Bootstrap 5.3.0 via CDN
- ✅ Bootstrap Icons 1.10.0
- ✅ Uso extensivo de classes utilitárias
- ✅ Componentes: cards, tables, forms, buttons, alerts, modals
- ✅ Sistema de grid responsivo

**Localização:**
- Layout principal: `templates/layout.html`
- CDN configurado no `<head>`

---

### 🟢 REQNAOFUNC 5 - Validações e mensagens (5 pts) ✅

**Status:** Implementado

**Implementação:**
- ✅ Bean Validation (@Valid, @NotBlank, @NotNull, etc.)
- ✅ BindingResult em todos os métodos POST
- ✅ Mensagens de erro com `th:errors`
- ✅ Classes CSS: `is-invalid`, `invalid-feedback`
- ✅ Validação customizada: `@Matricula`

**Validações presentes:**
- Aluno: nome, matrícula (custom), login obrigatórios
- Professor: nome, matrícula, login, senha obrigatórios
- Processo: textoRequerimento, assunto obrigatórios
- Colegiado: datas, curso, portaria, membros obrigatórios
- Assunto: nome obrigatório

---

### 🟢 REQNAOFUNC 6 - Post-Redirect-Get (5 pts) ✅

**Status:** Implementado

**Implementação:**
- ✅ Todos os métodos POST retornam `redirect:`
- ✅ Uso de `RedirectAttributes` para flash messages
- ✅ Mensagens de sucesso/erro exibidas após redirecionamento
- ✅ Previne reenvio de formulário ao atualizar página

**Exemplos:**
```java
// Todos os controllers seguem este padrão:
@PostMapping("/salvar")
public String salvar(..., RedirectAttributes redirect) {
    // ... lógica ...
    redirect.addFlashAttribute("msg", "Salvo com sucesso!");
    return "redirect:/rota";
}
```

---

## 🟡 ETAPA II - REQUISITOS EM ANDAMENTO (15/100)

### 🟢 REQNAOFUNC 8 - Spring Security (10 pts) ✅ **IMPLEMENTADO**

**Status:** Implementado e funcional

**Localização:**
- Config: `config/SecurityConfig.java`
- Controller: `controller/AuthController.java`
- Controller: `controller/HomeController.java`
- Util: `util/PasswordUtil.java`
- Templates: `auth/login.html`, `auth/acesso-negado.html`, `home.html`, `layout.html`

**Arquitetura Implementada:**
- ✅ `JdbcUserDetailsManager` (tabelas padrão do Spring Security: `users` e `authorities`)
- ✅ Autenticação via formulário (`/auth/login`)
- ✅ Senhas criptografadas com BCrypt
- ✅ Autorização por roles no layout (menu dinâmico com `sec:authorize`)
- ✅ Logout funcional (`/auth/logout`)
- ✅ Página de acesso negado (`/auth/acesso-negado`)
- ✅ Usuários de teste criados automaticamente na 1ª execução

**Usuários de Teste:**
| Usuário | Senha | Roles |
|---------|-------|-------|
| `aluno` | `123456` | ROLE_ALUNO |
| `professor` | `123456` | ROLE_PROFESSOR |
| `coordenador` | `123456` | ROLE_COORDENADOR, ROLE_PROFESSOR |
| `admin` | `123456` | ROLE_ADMIN |

**Regras de Autorização:**
```
/admin/**      → ROLE_ADMIN
/coord/**      → ROLE_COORDENADOR
/professor/**  → ROLE_PROFESSOR ou ROLE_COORDENADOR
/aluno/**      → ROLE_ALUNO
/              → Autenticado
/auth/login    → Público
```

**Como testar:**
1. Acessar `http://localhost:8080` (redireciona para `/auth/login`)
2. Login com credenciais acima
3. Verificar menu exibido conforme role
4. Testar acesso autorizado/não autorizado

**Documentação:** `docs/arquitetura-autenticacao.md`

---

### 🟢 REQNAOFUNC 10 - Validador customizado matrícula (5 pts) ✅

**Status:** Implementado

**Localização:**
- Anotação: `validator/Matricula.java`
- Validador: `validator/MatriculaValidator.java`
- Uso: `model/Aluno.java`

**Implementação:**
- ✅ Anotação `@Matricula` criada
- ✅ Validador que verifica:
  - Apenas números (0-9)
  - Mínimo 8 dígitos
- ✅ Mensagem de erro customizada
- ✅ Usado no campo `matricula` de Aluno

**Regras de validação:**
```java
// Válido: "12345678", "20231234", "987654321"
// Inválido: "ABC123", "1234", "123-456", "12345a78"
```

---

## ⏳ ETAPA II - REQUISITOS PENDENTES (85/100)

### 🔴 REQFUNC 4 - Professor consulta reuniões com filtro (10 pts) ❌

**Status:** NÃO IMPLEMENTADO

**Complexidade:** 🟢 Baixa

**O que precisa ser feito:**

1. **Criar ReuniaoService:**
```java
// Arquivo: service/ReuniaoService.java
@Service
public class ReuniaoService {
    @Autowired
    private ReuniaoRepository reuniaoRepository;

    public List<Reuniao> listarPorStatus(StatusReuniao status) {
        if (status == null) {
            return reuniaoRepository.findAll();
        }
        return reuniaoRepository.findByStatus(status);
    }

    public List<Reuniao> listarTodas() {
        return reuniaoRepository.findAll();
    }

    public Reuniao buscarPorId(Long id) {
        return reuniaoRepository.findById(id).orElse(null);
    }
}
```

2. **Atualizar ReuniaoRepository:**
```java
// Adicionar método:
List<Reuniao> findByStatus(StatusReuniao status);
```

3. **Criar Controller:**
```java
// Arquivo: controller/ReuniaoController.java
@Controller
@RequestMapping("/professor/reunioes")
public class ReuniaoController {
    @Autowired
    private ReuniaoService reuniaoService;

    @GetMapping
    public String listar(@RequestParam(required = false) StatusReuniao status, Model model) {
        model.addAttribute("reunioes", reuniaoService.listarPorStatus(status));
        return "professor/reuniao/list";
    }
}
```

4. **Criar Template:**
```
templates/professor/reuniao/list.html
- Tabela com reuniões
- Filtro dropdown: PROGRAMADA / ENCERRADA
- Colunas: Data, Status, Nº Processos, Ações
```

**Prioridade:** Alta

---

### 🔴 REQFUNC 5 - Professor vota com justificativa (5 pts) ❌

**Status:** NÃO IMPLEMENTADO

**Complexidade:** 🟡 Média

**O que precisa ser feito:**

1. **Adicionar campo em Voto.java:**
```java
@Column(columnDefinition = "TEXT")
private String justificativa;
```

2. **Criar VotoService:**
```java
// Arquivo: service/VotoService.java
@Service
public class VotoService {
    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private ProcessoRepository processoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    public void registrarVoto(Long processoId, Long professorId,
                              TipoVoto tipoVoto, String justificativa) {
        Voto voto = new Voto();
        voto.setProcesso(processoRepository.findById(processoId).orElseThrow());
        voto.setProfessor(professorRepository.findById(professorId).orElseThrow());
        voto.setVoto(tipoVoto);
        voto.setJustificativa(justificativa);
        voto.setAusente(false);
        votoRepository.save(voto);
    }

    public List<Voto> listarPorProcesso(Long processoId) {
        return votoRepository.findByProcessoId(processoId);
    }
}
```

3. **Atualizar VotoRepository:**
```java
List<Voto> findByProcessoId(Long processoId);
List<Voto> findByProcesso(Processo processo);
```

4. **Adicionar endpoint no Controller**

5. **Atualizar Template com modal de votação**

**Prioridade:** Média

---

### 🔴 REQFUNC 6 - Professor consulta reuniões agendadas (10 pts) ❌

**Status:** NÃO IMPLEMENTADO

**Complexidade:** 🟡 Média

**Prioridade:** Média

---

### 🔴 REQFUNC 9 - Coordenador cria sessão e define pauta (15 pts) ❌

**Status:** NÃO IMPLEMENTADO

**Complexidade:** 🔴 Alta

**Prioridade:** Alta

---

### 🔴 REQFUNC 10 - Coordenador inicia sessão (5 pts) ❌

**Status:** NÃO IMPLEMENTADO

**Complexidade:** 🟡 Média

**Prioridade:** Alta (necessário para REQFUNC 11)

**Dependências:** REQFUNC 9

---

### 🔴 REQFUNC 11 - Coordenador apregoa e julga processos (15 pts) ❌

**Status:** NÃO IMPLEMENTADO

**Complexidade:** 🔴 Muito Alta

**Prioridade:** Crítica

**Dependências:** REQFUNC 5, REQFUNC 9, REQFUNC 10

**Observações:**
- Esta é a funcionalidade core da Etapa II
- Requer testes extensivos
- Interface complexa (considerar usar JavaScript para UX)

---

### 🔴 REQFUNC 12 - Coordenador finaliza sessão (5 pts) ❌

**Status:** NÃO IMPLEMENTADO

**Complexidade:** 🟢 Baixa

**Prioridade:** Alta

**Dependências:** REQFUNC 11

---

### 🔴 REQFUNC 16 - Upload de PDF do requerimento (5 pts) ❌

**Status:** NÃO IMPLEMENTADO

**Complexidade:** 🟡 Média

**Prioridade:** Baixa

---

### 🔴 REQNAOFUNC 7 - Layouts e Fragmentos Thymeleaf (10 pts) ❌

**Status:** PARCIALMENTE IMPLEMENTADO (50%)

**Complexidade:** 🟡 Média

**O que existe:**
- ✅ Layout base (`layout.html`) com menu dinâmico por role
- ✅ Uso de `th:replace` em todos os templates
- ✅ Namespace `sec:authorize` para controle de exibição

**O que falta:**
- ❌ Criar pasta `templates/fragments/`
- ❌ Extrair navbar em fragmento separado
- ❌ Extrair footer em fragmento separado
- ❌ Extrair mensagens flash em fragmento separado
- ❌ Criar fragmento de paginação

**Prioridade:** Média

---

### 🔴 REQNAOFUNC 9 - Paginação com reflexo no banco (5 pts) ❌

**Status:** NÃO IMPLEMENTADO

**Complexidade:** 🟡 Média

**Prioridade:** Média

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

### Etapa II - Em Andamento
| Requisito | Descrição | Pontos | Status |
|-----------|-----------|--------|--------|
| REQFUNC 4 | Professor consulta reuniões | 10 | ❌ |
| REQFUNC 5 | Professor vota com justificativa | 5 | ❌ |
| REQFUNC 6 | Professor reuniões agendadas | 10 | ❌ |
| REQFUNC 9 | Coordenador cria sessão | 15 | ❌ |
| REQFUNC 10 | Coordenador inicia sessão | 5 | ❌ |
| REQFUNC 11 | Coordenador apregoa processos | 15 | ❌ |
| REQFUNC 12 | Coordenador finaliza sessão | 5 | ❌ |
| REQFUNC 16 | Upload PDF requerimento | 5 | ❌ |
| REQNAOFUNC 7 | Layouts e Fragmentos | 10 | 🟡 50% |
| REQNAOFUNC 8 | Spring Security | 10 | ✅ |
| REQNAOFUNC 9 | Paginação | 5 | ❌ |
| REQNAOFUNC 10 | Validador matrícula | 5 | ✅ |
| **TOTAL** | | **100** | **15/100** |

### Pontuação Total do Projeto
```
Etapa I:  100/100 (100%)
Etapa II:  15/100 (15%)
─────────────────────────
TOTAL:    115/200 (57.5%)
```

---

## 🎯 PRÓXIMAS AÇÕES RECOMENDADAS

### Sequência Ideal de Implementação

**Fase 1: Infraestrutura de Reuniões**
1. ✅ REQNAOFUNC 8 - Spring Security (CONCLUÍDO)
2. ❌ REQFUNC 4 - Professor consulta reuniões
3. ❌ REQFUNC 9 - Coordenador cria sessão
4. ❌ REQNAOFUNC 7 - Finalizar Fragmentos

**Fase 2: Votação e Julgamento**
5. ❌ REQFUNC 5 - Professor vota
6. ❌ REQFUNC 10 - Coordenador inicia sessão
7. ❌ REQFUNC 11 - Coordenador apregoa ⚠️ MAIS COMPLEXO
8. ❌ REQFUNC 12 - Coordenador finaliza

**Fase 3: Melhorias e Extras**
9. ❌ REQFUNC 6 - Reuniões agendadas
10. ❌ REQNAOFUNC 9 - Paginação
11. ❌ REQFUNC 16 - Upload PDF - OPCIONAL

---

## 📁 ESTRUTURA DE ARQUIVOS ATUALIZADA

### Arquivos de Segurança (Etapa II)
```
src/main/java/br/edu/ifpb/pweb2/delibera_consilium/
├── config/
│   └── SecurityConfig.java        # JdbcUserDetailsManager + BCrypt
├── controller/
│   ├── AuthController.java        # /auth/login, /auth/acesso-negado
│   ├── HomeController.java        # /, /home
│   └── ...
└── util/
    └── PasswordUtil.java          # Utilitário BCrypt (jBCrypt)

src/main/resources/templates/
├── auth/
│   ├── login.html                 # Página de login
│   └── acesso-negado.html         # Página de acesso negado
├── home.html                      # Dashboard por role
└── layout.html                    # Menu dinâmico (sec:authorize)

docs/
└── arquitetura-autenticacao.md    # Documentação de segurança
```

### Dependências Adicionadas (pom.xml)
```xml
<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- Thymeleaf Security -->
<dependency>
    <groupId>org.thymeleaf.extras</groupId>
    <artifactId>thymeleaf-extras-springsecurity6</artifactId>
</dependency>

<!-- jBCrypt -->
<dependency>
    <groupId>org.mindrot</groupId>
    <artifactId>jbcrypt</artifactId>
    <version>0.4</version>
</dependency>
```

---

## 📝 OBSERVAÇÕES IMPORTANTES

### Decisões de Arquitetura

1. **Autenticação:** JdbcUserDetailsManager (tabelas padrão: `users`, `authorities`)
2. **Criptografia:** BCrypt via Spring Security
3. **Rotas de Auth:** Prefixo `/auth/` (login, logout, acesso-negado)
4. **Menu Dinâmico:** `sec:authorize` no layout.html
5. **Usuários de Teste:** Criados automaticamente no `SecurityConfig`

### Arquivos Removidos (refatoração)
- ~~`model/User.java`~~ → Usando tabelas padrão do Spring
- ~~`model/Authority.java`~~ → Usando tabelas padrão do Spring
- ~~`repository/UserRepository.java`~~ → Usando JdbcUserDetailsManager
- ~~`security/UserDetailsServiceImpl.java`~~ → Usando JdbcUserDetailsManager
- ~~`security/SecurityUtils.java`~~ → Não necessário na nova arquitetura
- ~~`controller/LoginController.java`~~ → Substituído por AuthController

---

## 🤝 CONTRIBUIDORES

- **Felipe de Brito** - Desenvolvedor
- **Joana Elise** - Desenvolvedora

**Disciplina:** Programação Web II (PWEB2)
**Professor:** Frederico Guedes Pereira
**Instituição:** IFPB - Instituto Federal de Educação, Ciência e Tecnologia da Paraíba

---

## 📞 CONTATO

Para dúvidas sobre o projeto:
- Repositório: https://github.com/joanaeliseal/delibera_consilium
- Issues: Use o sistema de issues do GitHub

---

**Última atualização:** 26/01/2026 às 21:00
**Versão do documento:** 1.2.0
    