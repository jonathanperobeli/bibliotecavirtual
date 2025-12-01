# Sistema de Biblioteca Digital

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen)
![Java](https://img.shields.io/badge/Java-17-orange)
![License](https://img.shields.io/badge/License-MIT-blue)

## 📚 Sobre o Projeto

Sistema completo de gerenciamento de biblioteca digital desenvolvido com Spring Boot, implementando as melhores práticas de desenvolvimento de software, padrões de projeto GoF e arquitetura MVC.

### Funcionalidades Principais

- ✅ **CRUD Completo** para Livros, Autores, Categorias, Usuários e Empréstimos
- 🔐 **Autenticação e Autorização** com Spring Security (roles ADMIN/USER)
- 📊 **Dashboard** com estatísticas em tempo real
- 🔍 **API RESTful** documentada com Swagger/OpenAPI
- 🎨 **Interface Web** responsiva com Thymeleaf e Bootstrap 5

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Autenticação e autorização
- **Spring Validation** - Validação de dados
- **Hibernate** - ORM
- **H2 Database** - Banco de dados em memória (dev)
- **PostgreSQL** - Banco de dados (prod)

### Frontend
- **Thymeleaf** - Template engine
- **Bootstrap 5.3** - Framework CSS
- **Bootstrap Icons** - Ícones

### Documentação e Relatórios
- **springdoc-openapi** - Swagger UI
- **iTextPDF** - Geração de relatórios PDF

### Ferramentas
- **Maven** - Gerenciamento de dependências
- **Lombok** - Redução de boilerplate

## 🎯 Padrões de Projeto (GoF)

O projeto implementa 4 padrões de projeto Gang of Four:

1. **Factory Method** - Criação de diferentes tipos de empréstimos
2. **Strategy** - Cálculo flexível de multas
3. **Observer** - Notificação de eventos de empréstimo
4. **Singleton** - Gerenciamento de configurações

## 📁 Estrutura do Projeto

```
src/main/java/com/biblioteca/
├── BibliotecaDigitalApplication.java
├── config/
│   ├── SecurityConfig.java
│   ├── OpenApiConfig.java
│   └── DataInitConfig.java
├── controller/
│   ├── api/                    # Controllers REST
│   │   ├── LivroApiController.java
│   │   ├── AutorApiController.java
│   │   └── ...
│   └── web/                    # Controllers Web (Thymeleaf)
│       ├── HomeController.java
│       ├── LivroController.java
│       └── ...
├── exception/
│   ├── ResourceNotFoundException.java
│   ├── BusinessException.java
│   └── GlobalExceptionHandler.java
├── model/
│   ├── dto/
│   │   ├── LivroDTO.java
│   │   └── ...
│   └── entity/
│       ├── Livro.java
│       ├── Usuario.java
│       └── ...
├── pattern/
│   ├── factory/
│   ├── strategy/
│   ├── observer/
│   └── singleton/
├── repository/
│   ├── LivroRepository.java
│   └── ...
└── service/
    ├── LivroService.java
    └── impl/
        └── LivroServiceImpl.java
```

## 🚀 Como Executar

### Pré-requisitos
- Java 17+
- Maven 3.6+

### Passos

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/bibliotecadigital.git
cd bibliotecadigital
```

2. Execute com Maven:
```bash
mvn spring-boot:run
```

3. Acesse a aplicação:
- **Aplicação Web**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console

### Credenciais de Teste

| Usuário | Email | Senha | Papel |
|---------|-------|-------|-------|
| Admin | admin@biblioteca.com | admin123 | ADMIN |
| João | joao@email.com | 123456 | USER |
| Maria | maria@email.com | 123456 | USER |

## 📋 API Endpoints

### Livros
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/livros` | Lista todos os livros |
| GET | `/api/livros/{id}` | Busca livro por ID |
| POST | `/api/livros` | Cria novo livro |
| PUT | `/api/livros/{id}` | Atualiza livro |
| DELETE | `/api/livros/{id}` | Remove livro |

### Empréstimos
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/emprestimos` | Lista empréstimos |
| POST | `/api/emprestimos` | Cria empréstimo |
| POST | `/api/emprestimos/{id}/devolver` | Registra devolução |
| POST | `/api/emprestimos/{id}/renovar` | Renova empréstimo |

*Veja todos os endpoints na documentação Swagger*

## 📊 Relatórios Disponíveis

- Relatório de Livros (PDF)
- Relatório de Empréstimos (PDF)
- Relatório de Empréstimos Atrasados (PDF)
- Relatório de Usuários (PDF)
- Relatório do Dashboard (PDF)

## 🔧 Configuração

### Perfil de Desenvolvimento (padrão)
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:bibliotecadb
  h2:
    console:
      enabled: true
```

### Perfil de Produção
```yaml
spring:
  profiles:
    active: prod
  datasource:
    url: jdbc:postgresql://localhost:5432/biblioteca
    username: postgres
    password: sua_senha
```

## 🧪 Testes

Execute os testes com:
```bash
mvn test
```

## 👨‍💻 Autores
- Jonathan Campos Machado
- Patrick Yokoyama Kloth

---


