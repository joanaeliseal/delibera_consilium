# Arquitetura de Autenticação - Delibera Consilium

Este documento explica a estrutura de autenticação e autorização do sistema.

---

## Visão Geral

O sistema utiliza **Spring Security** com **JdbcUserDetailsManager** para gerenciar autenticação e autorização. Esta abordagem usa as tabelas padrão do Spring Security (`users` e `authorities`).

---

## Componentes da Arquitetura

### 1. Config: `SecurityConfig.java`

Classe principal de configuração do Spring Security.

**Responsabilidades:**
- Configurar regras de autorização por URL
- Configurar páginas de login/logout
- Criar usuários de teste automaticamente
- Configurar o encoder de senhas (BCrypt)

**Localização:** `src/main/java/.../config/SecurityConfig.java`

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) { ... }

    @Bean
    public PasswordEncoder passwordEncoder() { ... }

    @Bean
    public UserDetailsService userDetailsService() { ... }

    @Bean
    public AuthenticationProvider authenticationProvider() { ... }
}
```

---

### 2. Controller: `AuthController.java`

Gerencia as páginas de autenticação.

**Rotas:**
- `GET /auth/login` → Página de login
- `GET /auth/acesso-negado` → Página de acesso negado

**Localização:** `src/main/java/.../controller/AuthController.java`

```java
@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/login")
    public ModelAndView getLoginForm(ModelAndView model) { ... }

    @GetMapping("/acesso-negado")
    public ModelAndView getAcessoNegado(ModelAndView model) { ... }
}
```

---

### 3. Controller: `HomeController.java`

Gerencia a página inicial após autenticação.

**Rotas:**
- `GET /` → Página inicial
- `GET /home` → Página inicial

**Localização:** `src/main/java/.../controller/HomeController.java`

```java
@Controller
public class HomeController {

    @RequestMapping({"/", "/home"})
    public String showHomePage() { ... }

    @ModelAttribute("menu")
    public String selectMenu() { ... }
}
```

---

### 4. Util: `PasswordUtil.java`

Utilitário para criptografia de senhas usando jBCrypt.

**Métodos:**
- `hashPassword(String)` → Criptografa uma senha
- `checkPass(String, String)` → Verifica se a senha corresponde
- `main(String[])` → Gera senhas criptografadas para teste

**Localização:** `src/main/java/.../util/PasswordUtil.java`

```java
public abstract class PasswordUtil {

    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public static boolean checkPass(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
```

---

## Tabelas do Spring Security

O `JdbcUserDetailsManager` usa duas tabelas padrão criadas automaticamente:

### Tabela `users`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| username | VARCHAR(50) | Login do usuário (PK) |
| password | VARCHAR(500) | Senha criptografada (BCrypt) |
| enabled | BOOLEAN | Se o usuário está ativo |

### Tabela `authorities`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| username | VARCHAR(50) | Login do usuário (FK) |
| authority | VARCHAR(50) | Role do usuário (ex: ROLE_ALUNO) |

---

## Fluxo de Autenticação

```
┌─────────────────────────────────────────────────────────────────────┐
│                        FLUXO DE LOGIN                               │
└─────────────────────────────────────────────────────────────────────┘

1. Usuário acessa qualquer página protegida
                    │
                    ▼
2. Spring Security redireciona para /auth/login
                    │
                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│  AuthController.getLoginForm()                                      │
│  - Retorna o template "auth/login"                                  │
└─────────────────────────────────────────────────────────────────────┘
                    │
                    ▼
3. Usuário preenche o formulário (username + password)
                    │
                    ▼
4. Formulário envia POST para /auth/login
                    │
                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│  Spring Security (automático)                                       │
│  - Busca usuário na tabela 'users' via JdbcUserDetailsManager       │
│  - Compara senha com BCrypt                                         │
│  - Carrega authorities da tabela 'authorities'                      │
└─────────────────────────────────────────────────────────────────────┘
                    │
        ┌───────────┴───────────┐
        ▼                       ▼
   [SUCESSO]               [FALHA]
        │                       │
        ▼                       ▼
   Redireciona             Redireciona
   para /home              para /auth/login?error
        │
        ▼
┌─────────────────────────────────────────────────────────────────────┐
│  HomeController.showHomePage()                                      │
│  - Retorna o template "home"                                        │
│  - Menu exibido conforme roles do usuário                           │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Roles do Sistema

| Role | Descrição | Acesso |
|------|-----------|--------|
| `ROLE_ALUNO` | Estudante | `/aluno/**` |
| `ROLE_PROFESSOR` | Professor (não coordenador) | `/professor/**` |
| `ROLE_COORDENADOR` | Professor coordenador | `/coord/**`, `/professor/**` |
| `ROLE_ADMIN` | Administrador do sistema | `/admin/**` |

---

## Usuários de Teste

Criados automaticamente na primeira execução:

| Usuário | Senha | Roles |
|---------|-------|-------|
| `aluno` | `123456` | ROLE_ALUNO |
| `professor` | `123456` | ROLE_PROFESSOR |
| `coordenador` | `123456` | ROLE_COORDENADOR, ROLE_PROFESSOR |
| `admin` | `123456` | ROLE_ADMIN |

---

## Configuração de Segurança

```java
.authorizeHttpRequests(auth -> auth
    // Recursos públicos
    .requestMatchers("/css/**", "/images/**", "/imagens/**").permitAll()

    // Recursos por role
    .requestMatchers("/admin/**").hasRole("ADMIN")
    .requestMatchers("/coord/**").hasRole("COORDENADOR")
    .requestMatchers("/professor/**").hasAnyRole("PROFESSOR", "COORDENADOR")
    .requestMatchers("/aluno/**").hasRole("ALUNO")

    // Qualquer outra requisição precisa autenticação
    .anyRequest().authenticated()
)
.formLogin(form -> form
    .loginPage("/auth/login")
    .defaultSuccessUrl("/home", true)
    .permitAll()
)
.logout(logout -> logout
    .logoutUrl("/auth/logout")
)
.exceptionHandling(ex -> ex
    .accessDeniedPage("/auth/acesso-negado")
)
```

---

## Estrutura de Pastas

```
src/main/java/br/edu/ifpb/pweb2/delibera_consilium/
├── config/
│   └── SecurityConfig.java        # Configuração do Spring Security
├── controller/
│   ├── AuthController.java        # Login e autenticação
│   └── HomeController.java        # Página inicial
└── util/
    └── PasswordUtil.java          # Utilitário para criptografia

src/main/resources/templates/
├── auth/
│   ├── login.html                 # Página de login
│   └── acesso-negado.html         # Página de acesso negado
├── home.html                      # Página inicial
└── layout.html                    # Layout principal (com menu por role)
```

---

## Dependências Necessárias (pom.xml)

```xml
<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- Thymeleaf Security (para sec:authorize) -->
<dependency>
    <groupId>org.thymeleaf.extras</groupId>
    <artifactId>thymeleaf-extras-springsecurity6</artifactId>
</dependency>

<!-- jBCrypt (para PasswordUtil) -->
<dependency>
    <groupId>org.mindrot</groupId>
    <artifactId>jbcrypt</artifactId>
    <version>0.4</version>
</dependency>
```

---

## Templates Thymeleaf

### Verificar role no template

```html
<!-- Exibir apenas para ADMIN -->
<div sec:authorize="hasRole('ADMIN')">
    Conteúdo para administradores
</div>

<!-- Exibir para PROFESSOR ou COORDENADOR -->
<div sec:authorize="hasAnyRole('PROFESSOR', 'COORDENADOR')">
    Conteúdo para professores
</div>
```

### Exibir nome do usuário logado

```html
<span sec:authentication="name">Usuário</span>
```

### Formulário de logout

```html
<form th:action="@{/auth/logout}" method="post">
    <button type="submit">Sair</button>
</form>
```

---

## Referências

- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [Thymeleaf + Spring Security](https://www.thymeleaf.org/doc/articles/springsecurity.html)
- [JdbcUserDetailsManager](https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/provisioning/JdbcUserDetailsManager.html)

---

*Documento criado para estudo da disciplina PWEB2 - IFPB*
