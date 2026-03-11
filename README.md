# ForumHub API - Challenge Alura

Uma API REST robusta desenvolvida em Java e Spring Boot para o gerenciamento de tópicos de um fórum, simulando o backend de uma plataforma de discussões. Este projeto faz parte do desafio final da formação Java da Alura em parceria com o programa Oracle Next Education (ONE).

## 🚀 Funcionalidades

- **CRUD de Tópicos**: Criar, listar, detalhar, atualizar e excluir tópicos.
- **Autenticação e Autorização**: Segurança implementada com **Spring Security** e **JWT (JSON Web Token)**.
- **Persistência de Dados**: Banco de dados MySQL com controle de versões via **Flyway**.
- **Validações**: Regras de negócio para evitar tópicos duplicados e garantir campos obrigatórios.
- **Tratamento de Erros**: Respostas JSON amigáveis para erros 404, 400 e 403.

## 🛠️ Tecnologias Utilizadas

* **Java 17**
* **Spring Boot 3**
* **Spring Data JPA**
* **Spring Security**
* **Auth0 JWT**
* **MySQL**
* **Flyway** (Migrations)
* **Lombok**
* **Maven**

## 🏁 Como Iniciar

### Pré-requisitos
* Java 17 ou superior
* MySQL 8.0+
* IDE de sua preferência (IntelliJ, VS Code, etc)
