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
│   │   │   ├── controller/      # Controladores REST/MVC
│   │   │   ├── model/           # Entidades e modelos de dados
│   │   │   ├── repository/      # Camada de acesso a dados
│   │   │   ├── service/         # Lógica de negócio
│   │   │   └── DeliberaConsiliumApplication.java
│   │   └── resources/
│   │       ├── application.properties  # Configurações da aplicação
│   │       ├── static/          # Arquivos CSS, JS, imagens
│   │       └── templates/       # Templates HTML (Thymeleaf)
│   └── test/
│       └── java/               # Testes unitários e de integração
├── pom.xml                      # Arquivo de configuração Maven
└── README.md
```

## 🏗️ Arquitetura

O projeto segue a arquitetura em camadas:

- **Controller**: Responsável por receber requisições HTTP e coordenar as respostas
- **Service**: Contém a lógica de negócio da aplicação
- **Repository**: Gerencia a persistência de dados no banco de dados
- **Model**: Define as entidades e estruturas de dados

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
