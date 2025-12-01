# Delibera Consilium

## 📋 Contexto do Projeto

**Delibera Consilium** é uma aplicação web desenvolvida como projeto acadêmico para a disciplina de **Programação Web II (PWEB2)** do Instituto Federal de Educação, Ciência e Tecnologia da Paraíba (IFPB).

O nome "Delibera Consilium" é uma expressão em latim que significa "Deliberação do Conselho", refletindo o propósito da aplicação de gerenciar discussões e deliberações em ambientes colaborativos.

## 🚀 Tecnologias Utilizadas

- **Java 21**: Linguagem de programação principal
- **Spring Boot 3.5.7**: Framework para desenvolvimento de aplicações web
- **Spring Web**: Módulo para construção de APIs e aplicações web
- **PostgreSQL**: Sistema de gerenciamento de banco de dados
- **Lombok**: Biblioteca para redução de boilerplate code
- **Maven**: Ferramenta de gerenciamento de dependências e construção do projeto

## 📁 Estrutura do Projeto

```
delibera_consilium/
├── src/
│   ├── main/
│   │   ├── java/br/edu/ifpb/pweb2/delibera_consilium/
│   │   │   ├── DeliberaConsiliumApplication.java
│   │   │   ├── controller/ # Controladores REST/MVC
│   │   │   │   ├── AlunoController.java
│   │   │   │   ├── AssuntoController.java
│   │   │   │   ├── ColegiadoController.java
│   │   │   │   ├── ProcessoAlunoController.java
│   │   │   │   ├── ProcessoCoordenadorController.java
│   │   │   │   ├── ProcessoProfessorController.java
│   │   │   │   └── ProfessorController.java
│   │   │   ├── model/ # Entidades e modelos de dados
│   │   │   │   ├── Aluno.java
│   │   │   │   ├── Assunto.java
│   │   │   │   ├── Colegiado.java
│   │   │   │   ├── Processo.java
│   │   │   │   ├── Professor.java
│   │   │   │   ├── Reuniao.java
│   │   │   │   ├── StatusReuniao.java
│   │   │   │   ├── TipoDecisao.java
│   │   │   │   ├── TipoVoto.java
│   │   │   │   └── Voto.java
│   │   │   ├── repository/  # Camada de acesso a dados
│   │   │   │   ├── AlunoRepository.java
│   │   │   │   ├── AssuntoRepository.java
│   │   │   │   ├── ColegiadoRepository.java
│   │   │   │   ├── ProcessoRepository.java
│   │   │   │   ├── ProfessorRepository.java
│   │   │   │   ├── ReuniaoRepository.java
│   │   │   │   └── VotoRepository.java
│   │   │   ├── service/  # Lógica de negócio
│   │   │   │   ├── AlunoService.java
│   │   │   │   ├── AssuntoService.java
│   │   │   │   ├── ColegiadoService.java
│   │   │   │   ├── ProcessoService.java
│   │   │   │   └── ProfessorService.java
│   │   │   └── validator/ # Lógica de validação da matrícula
│   │   │       ├── Matricula.java
│   │   │       └── MatriculaValidator.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/ # Arquivos CSS, JS, imagens 
│   │       └── templates/ # Templates HTML (Thymeleaf)
│   └── test/
│       └── java/br/edu/ifpb/pweb2/delibera_consilium/
│           └── DeliberaConsiliumApplicationTests.java
├── pom.xml             # Arquivo de configuração Maven
└── README.md
```

## 🏗️ Arquitetura

O projeto segue a arquitetura em camadas:

- **Controller**: Responsável por receber requisições HTTP e coordenar as respostas
- **Service**: Contém a lógica de negócio da aplicação
- **Repository**: Gerencia a persistência de dados no banco de dados
- **Model**: Define as entidades e estruturas de dados

## 🧭 Controladores (controller)

Breve lista dos controllers do projeto e sua responsabilidade principal (sem detalhar rotas):

- `AlunoController.java` — gerencia operações administrativas relacionadas a alunos (CRUD, formulários).
- `ProfessorController.java` — gerencia operações administrativas relacionadas a professores (CRUD, formulários).
- `AssuntoController.java` — gerencia assuntos/pautas usados em processos.
- `ColegiadoController.java` — gerencia colegiados e associação de professores como membros.
- `ProcessoAlunoController.java` — funcionalidades para alunos criarem/visualizarem seus processos.
- `ProcessoProfessorController.java` — visão e ações relacionadas a processos atribuídos a um professor (relator).
- `ProcessoCoordenadorController.java` — visão do coordenador para listar e distribuir processos entre professores.

## 🧭 Entidades principais (model)

- `Aluno.java`: representa um aluno com dados pessoais e matrícula.
- `Professor.java`: representa um professor que participa das reuniões e processos.
- `Colegiado.java`: representa o colegiado (conselho) que delibera sobre processos.
- `Assunto.java`: tópico ou pauta que pode compor uma reunião ou processo.
- `Processo.java`: registro de um processo submetido ao colegiado para deliberação.
- `Reuniao.java`: representa uma reunião do colegiado, com data, pauta e participantes.
- `StatusReuniao.java`: enum com os estados possíveis de uma reunião (por exemplo: AGENDADA, REALIZADA, CANCELADA).
- `TipoDecisao.java`: enum que descreve tipos de decisão adotados pelo colegiado.
- `TipoVoto.java`: enum com tipos de voto possíveis (por exemplo: FAVOR, CONTRA, ABSTENCAO).
- `Voto.java`: representa o voto de um participante em um processo ou item de pauta.

## 📦 Repositórios (repository)

As interfaces em `repository/` são responsáveis pela persistência dos dados e, normalmente, estendem `JpaRepository` ou outra interface do Spring Data. Arquivos atuais:

- `AlunoRepository.java`
- `ProfessorRepository.java`
- `ColegiadoRepository.java`
- `AssuntoRepository.java`
- `ProcessoRepository.java`
- `ReuniaoRepository.java`
- `VotoRepository.java`

## 🛠️ Serviços (service)

As classes em `service/` encapsulam a lógica de negócio e orquestram chamadas aos repositórios. Elas são usadas pelos controllers para manter a aplicação organizada e testável. Serviços atuais:

- `AlunoService.java`
- `ProfessorService.java`
- `ColegiadoService.java`
- `AssuntoService.java`
- `ProcessoService.java`

## 🔧 Configuração e Execução

### Pré-requisitos

- Java 21 ou superior instalado
- PostgreSQL configurado e em execução
- Maven 3.6 ou superior

### Instalação

1. Clone o repositório:
```bash
git clone https://github.com/joanaeliseal/delibera_consilium.git
cd delibera_consilium
```

2. Configure o banco de dados no arquivo `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/delibera_consilium
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### Crie seu banco de dados
1. Abra seu gerenciador de banco de dados ou terminal.
2. Crie um banco de dados vazio com o nome do projeto:
```properties
CREATE DATABASE delibera_consilium;
```

3. Compile e execute o projeto:
```bash
mvn clean install
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`

## 📝 Notas Importantes

- Este é um projeto acadêmico em desenvolvimento
- A versão atual é `0.0.1-SNAPSHOT`
- Contribuições e melhorias são bem-vindas

## 👤 Autor

Desenvolvido por: **Felipe de Brito** e **Joana Elise**

Disciplina: Programação Web II (PWEB2)
Instituição: IFPB (Instituto Federal de Educação, Ciência e Tecnologia da Paraíba)

## 📄 Licença

Este projeto é disponibilizado sob licença aberta. Consulte o arquivo LICENSE para mais detalhes.
