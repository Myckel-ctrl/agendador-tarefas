# 📅 Agendador de Tarefas — Microsserviço

Microsserviço para gerenciamento de tarefas desenvolvido com **Java 21**, **Spring Boot** e **MongoDB**, integrado ao microsserviço de Usuários através do **OpenFeign**.

---

## 🚀 Tecnologias

- Java 21
- Spring Boot
- Spring Security
- Spring Data MongoDB
- MongoDB
- OpenFeign
- MapStruct
- Spring Validation
- Swagger / OpenAPI
- Lombok
- Actuator

---

## 🏛️ Visão geral da arquitetura

```
┌───────────────────────┐        Feign (HTTP)        ┌────────────────────────┐
│    usuario            │◄───────────────────────────│   agendador-tarefas    │
│   (porta 8080)        │   GET /users/me            │   (porta 8081)         │
│   PostgreSQL          │   Authorization: Bearer ...│    MongoDB             │
└───────────────────────┘                            └────────────────────────┘
```

O `agendador-tarefas` **não possui base de usuários própria**. A cada requisição:

1. O token JWT é recebido pelo `JwtAuthFilter`.
2. O `JwtUtil` valida assinatura e expiração.
3. O serviço consulta o `usuario` via OpenFeign.
4. O usuário autenticado é colocado no `SecurityContext`.
5. A requisição segue para o Controller.

---

## 📁 Estrutura de pacotes

```
src/main/java/com/agendador_tarefas/
├── entity/
│   ├── Tarefa.java
│   └── enums/StatusTarefa.java
├── dto/task/
│   ├── TarefaRequestDTO.java
│   ├── TarefaUpdateDTO.java
│   ├── StatusUpdateDTO.java
│   └── TarefaResponseDTO.java
├── client/                      ← integração via OpenFeign
│   ├── UserClient.java
│   ├── fallback/UserClientFallbackFactory.java
│   ├── dto/UserResponseDTO.java
│   ├── decoder/UserServiceErrorDecoder.java
│   └── config/FeignClientConfiguration.java
├── mapper/
│   └── TarefaMapper.java         ← MapStruct (inclusive PATCH parcial)
├── repository/
│   └── TarefaRepository.java
├── service/
│   ├── TarefaService.java
│   └── TarefaServiceImpl.java
├── security/
│   ├── JwtUtil.java
│   ├── JwtAuthFilter.java
│   ├── AuthenticatedUser.java
│   ├── SecurityConfig.java
│   ├── CustomAuthenticationEntryPoint.java
│   └── CustomAccessDeniedHandler.java
├── config/
│   ├── FeignConfiguration.java
│   ├── MongoAuditingConfig.java
│   └── OpenApiConfig.java
├── exception/
│   ├── ApiError.java
│   ├── ResourceNotFoundException.java
│   ├── AccessDeniedException.java
│   ├── UnauthorizedException.java
│   ├── ServiceUnavailableException.java
│   ├── InvalidDateRangeException.java
│   └── GlobalExceptionHandler.java
└── controller/
    └── TarefaController.java
```

---

## 🔐 Segurança

- Nenhum endpoint é público, exceto `/actuator/health` e a documentação Swagger.
- Autenticação **stateless**, via header `Authorization: Bearer <token>`.
- O e-mail e o id do usuário são **sempre extraídos do token/Feign** — nunca de parâmetros da URL ou do corpo da requisição.
- Este serviço **não implementa `UserDetailsService`**: ele não é dono de credenciais. Um `UserDetailsService` fictício (sem base própria) só adicionaria complexidade artificial — decisão consciente, documentada no código.
- `PasswordEncoder` (BCrypt) é mantido como bean por boa prática defensiva, mesmo não sendo usado no fluxo atual (não há senha local neste serviço).

---

## 🧩 Regras de negócio

- Toda tarefa inicia com status **PENDENTE**.
- Apenas o proprietário pode visualizar, alterar ou excluir uma tarefa.
- Datas de criação e alteração são preenchidas automaticamente.
- Atualizações parciais utilizam MapStruct.

---

## 📦 Endpoints

Todos exigem `Authorization: Bearer <token>`.

| Método | Rota                          | Descrição                                       |
|--------|-------------------------------|--------------------------------------------------|
| POST   | `/tarefas`                    | Cria uma tarefa (status inicial `PENDENTE`)      |
| GET    | `/tarefas/{id}`               | Busca uma tarefa por ID (apenas se for dono)     |
| GET    | `/tarefas`                    | Lista as próprias tarefasb                       |
| GET    | `/tarefas/periodo`            | Busca tarefas por período `(inicio)` e `(fim)`   |
| PATCH  | `/tarefas/{id}`               | Atualização parcial (campos nulos são ignorados) |
| PATCH  | `/tarefas/{id}/status`        | Altera apenas o status                           |
| DELETE | `/tarefas/{id}`               | Remove a tarefa                                  |

---

# ⚠️ Tratamento de erros

| Código | Significado |
|---------|-------------|
| 400 | Requisição inválida |
| 401 | Token inválido ou expirado |
| 403 | Usuário sem permissão |
| 404 | Recurso não encontrado |
| 503 | Microsserviço de usuários indisponível |

---

# ▶️ Como executar o projeto

## Pré-requisitos

- Java 21
- MongoDB em execução
- Microsserviço **usuario** em execução
- A mesma chave `jwt.secret` configurada nos dois microsserviços

## Executando pela IDE

1. Clone o repositório.
2. Abra o projeto na sua IDE (IntelliJ IDEA, Eclipse ou STS).
3. Aguarde o Gradle baixar todas as dependências.
4. Verifique se o MongoDB está em execução.
5. Certifique-se de que o microsserviço **usuario** esteja rodando na porta configurada.
6. Execute a classe:

```
AgendadorTarefasApplication.java
```

Após iniciar, a aplicação estará disponível em:

```
http://localhost:8081
```

---

## 🧪 Testando a API

1. Inicie o microsserviço **usuario**.
3. Inicie o microsserviço **agendador-tarefas**.
4. Acesse:

```
http://localhost:8081/swagger-ui.html
```

4. Clique em **Authorize** no canto superior direito.
5. Informe o token JWT gerado no microsserviço de **usuario** no formato:

```
eyJhbGciOiJIUzI1Ni... 
```

O Swagger já está configurado com autenticação JWT e documentação OpenAPI, permitindo testar toda a API diretamente pela interface web.

---

# 🎯 Objetivo

Este projeto foi desenvolvido para aprofundar conhecimentos em Java e Spring Boot, aplicando conceitos de microsserviços, Spring Security, JWT, OpenFeign, MongoDB, MapStruct e boas práticas de arquitetura em aplicações reais.

---

# 👨‍💻 Autor

**Myckel de Vasconcelos Mota**

Projeto desenvolvido para fins de estudo e composição de portfólio.

