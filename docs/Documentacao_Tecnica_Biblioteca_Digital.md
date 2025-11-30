# 📚 Documentação Técnica
# Sistema de Biblioteca Digital

---

**Disciplina:** Linguagem de Programação 2
**Instituição:** IFSP - SPO 
**Data:** Novembro de 2025  

**Integrantes do Grupo:**
- Jonathan Perobeli
- Patrick Yokoyama
---

## Sumário

1. [Introdução](#1-introdução)
2. [Descrição do Sistema](#2-descrição-do-sistema)
3. [Arquitetura do Sistema](#3-arquitetura-do-sistema)
4. [Tecnologias Utilizadas](#4-tecnologias-utilizadas)
5. [Padrões de Projeto GoF](#5-padrões-de-projeto-gof)
6. [Diagrama de Classes](#6-diagrama-de-classes)
7. [Diagrama do Banco de Dados](#7-diagrama-do-banco-de-dados)
8. [Boas Práticas (Clean Code e SOLID)](#8-boas-práticas-clean-code-e-solid)
9. [Segurança](#9-segurança)
10. [API REST](#10-api-rest)
11. [Interface do Usuário](#11-interface-do-usuário)
12. [Como Executar](#12-como-executar)
13. [Conclusão](#13-conclusão)

---

## 1. Introdução

Este documento apresenta a documentação técnica completa do **Sistema de Biblioteca Digital**, desenvolvido como projeto final da disciplina de Desenvolvimento de Aplicações Web.

O sistema foi desenvolvido utilizando **Spring Boot 3.2.0** com **Java 17**, seguindo as melhores práticas de desenvolvimento de software, incluindo arquitetura MVC, padrões de projeto GoF, princípios SOLID e Clean Code.

### 1.1 Objetivos do Projeto

- Desenvolver uma aplicação web completa com Spring Boot
- Implementar operações CRUD com persistência via JPA/Hibernate
- Aplicar padrões de projeto GoF adequados à solução
- Implementar autenticação e autorização com Spring Security
- Documentar a API com OpenAPI/Swagger
- Criar interface web responsiva e funcional

---

## 2. Descrição do Sistema

### 2.1 Visão Geral

O Sistema de Biblioteca Digital é uma aplicação web completa para gerenciamento de acervo bibliográfico, permitindo:

- Cadastro e gerenciamento de livros, autores e categorias
- Controle de empréstimos e devoluções
- Gestão de usuários com diferentes níveis de acesso
- Dashboard com estatísticas em tempo real
- Geração de relatórios

### 2.2 Funcionalidades Principais

| Módulo | Funcionalidades |
|--------|-----------------|
| **Livros** | CRUD completo, busca por título/autor/categoria, controle de disponibilidade |
| **Autores** | CRUD completo, associação com livros (N:N) |
| **Categorias** | CRUD completo, organização hierárquica do acervo |
| **Usuários** | Cadastro, autenticação, perfis (ADMIN/USER) |
| **Empréstimos** | Registro, devolução, renovação, cálculo de multas |
| **Dashboard** | Estatísticas, contadores, visão geral do sistema |
| **Relatórios** | Exportação em PDF |

### 2.3 Regras de Negócio

1. **Empréstimos:** Prazo padrão de 14 dias, máximo de 3 livros por usuário
2. **Multas:** Calculadas por dia de atraso (R$ 1,00/dia padrão ou progressiva)
3. **Disponibilidade:** Livro só pode ser emprestado se houver exemplares disponíveis
4. **Permissões:** Apenas ADMIN pode cadastrar/editar livros, autores e categorias

---

## 3. Arquitetura do Sistema

### 3.1 Arquitetura MVC

O sistema segue o padrão **Model-View-Controller (MVC)**:

```
┌─────────────────────────────────────────────────────────────────┐
│                         CLIENTE                                  │
│                  (Browser / API Client)                          │
└─────────────────────────┬───────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────────────┐
│                      CONTROLLER                                  │
│  ┌──────────────────┐  ┌──────────────────────────────────┐    │
│  │  Web Controllers │  │     API Controllers (REST)       │    │
│  │  (Thymeleaf)     │  │     /api/*                       │    │
│  └──────────────────┘  └──────────────────────────────────┘    │
└─────────────────────────┬───────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────────────┐
│                       SERVICE                                    │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐               │
│  │LivroService │ │AutorService │ │EmprestimoSvc│  ...          │
│  └─────────────┘ └─────────────┘ └─────────────┘               │
└─────────────────────────┬───────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────────────┐
│                     REPOSITORY                                   │
│  ┌───────────────┐ ┌───────────────┐ ┌───────────────┐         │
│  │LivroRepository│ │AutorRepository│ │EmprestimoRepo │  ...    │
│  └───────────────┘ └───────────────┘ └───────────────┘         │
└─────────────────────────┬───────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────────────┐
│                      DATABASE                                    │
│                   H2 (dev) / PostgreSQL (prod)                  │
└─────────────────────────────────────────────────────────────────┘
```

### 3.2 Estrutura de Pacotes

```
com.biblioteca
├── config/                 # Configurações (Security, OpenAPI)
├── controller/
│   ├── api/               # Controllers REST
│   └── web/               # Controllers Web (Thymeleaf)
├── exception/             # Tratamento de exceções
├── model/
│   ├── dto/               # Data Transfer Objects
│   └── entity/            # Entidades JPA
├── pattern/               # Padrões de Projeto GoF
│   ├── factory/           # Factory Method
│   ├── observer/          # Observer
│   ├── singleton/         # Singleton
│   └── strategy/          # Strategy
├── repository/            # Repositórios JPA
└── service/
    └── impl/              # Implementações dos serviços
```

---

## 4. Tecnologias Utilizadas

### 4.1 Backend

| Tecnologia | Versão | Finalidade |
|------------|--------|------------|
| Java | 17 | Linguagem de programação |
| Spring Boot | 3.2.0 | Framework principal |
| Spring Data JPA | 3.2.0 | Persistência de dados |
| Spring Security | 6.2.0 | Autenticação e autorização |
| Spring Validation | 3.2.0 | Validação de dados |
| Hibernate | 6.3.1 | ORM |
| H2 Database | 2.2.224 | Banco de dados (desenvolvimento) |
| PostgreSQL | 42.6.0 | Banco de dados (produção) |
| Lombok | 1.18.30 | Redução de boilerplate |

### 4.2 Frontend

| Tecnologia | Versão | Finalidade |
|------------|--------|------------|
| Thymeleaf | 3.2.0 | Template engine |
| Bootstrap | 5.3.2 | Framework CSS |
| Bootstrap Icons | 1.11.2 | Biblioteca de ícones |

### 4.3 Documentação e Relatórios

| Tecnologia | Versão | Finalidade |
|------------|--------|------------|
| springdoc-openapi | 2.2.0 | Swagger UI / OpenAPI |
| iTextPDF | 5.5.13.3 | Geração de relatórios PDF |

---

## 5. Padrões de Projeto GoF

O sistema implementa **4 padrões de projeto** do catálogo Gang of Four (GoF):

### 5.1 Factory Method

**Localização:** `com.biblioteca.pattern.factory`

**Propósito:** Criar diferentes tipos de empréstimos (padrão e estendido) sem expor a lógica de criação.

**Implementação:**

```java
// Interface da fábrica
public interface EmprestimoFactory {
    Emprestimo criarEmprestimo(Usuario usuario, Livro livro);
    int getPrazoDias();
}

// Fábrica de empréstimo padrão (14 dias)
public class EmprestimoPadraoFactory implements EmprestimoFactory {
    private static final int PRAZO_PADRAO = 14;
    
    @Override
    public Emprestimo criarEmprestimo(Usuario usuario, Livro livro) {
        return Emprestimo.builder()
                .usuario(usuario)
                .livro(livro)
                .dataEmprestimo(LocalDate.now())
                .dataPrevistaDevolucao(LocalDate.now().plusDays(PRAZO_PADRAO))
                .status(StatusEmprestimo.ATIVO)
                .build();
    }
}

// Fábrica de empréstimo estendido (30 dias)
public class EmprestimoEstendidoFactory implements EmprestimoFactory {
    private static final int PRAZO_ESTENDIDO = 30;
    // ...
}
```

**Diagrama:**

```
        ┌─────────────────────┐
        │  EmprestimoFactory  │ (Interface)
        │  + criarEmprestimo()│
        └──────────┬──────────┘
                   │
       ┌───────────┴───────────┐
       ▼                       ▼
┌──────────────────┐   ┌────────────────────┐
│EmprestimoPadrao  │   │EmprestimoEstendido │
│    Factory       │   │     Factory        │
│  (14 dias)       │   │    (30 dias)       │
└──────────────────┘   └────────────────────┘
```

---

### 5.2 Strategy

**Localização:** `com.biblioteca.pattern.strategy`

**Propósito:** Permitir diferentes algoritmos de cálculo de multa intercambiáveis em tempo de execução.

**Implementação:**

```java
// Interface da estratégia
public interface MultaStrategy {
    BigDecimal calcularMulta(long diasAtraso);
    String getDescricao();
}

// Estratégia de multa fixa (R$ 1,00/dia)
public class MultaFixaStrategy implements MultaStrategy {
    private static final BigDecimal VALOR_DIARIO = new BigDecimal("1.00");
    
    @Override
    public BigDecimal calcularMulta(long diasAtraso) {
        return VALOR_DIARIO.multiply(BigDecimal.valueOf(diasAtraso));
    }
}

// Estratégia de multa progressiva
public class MultaProgressivaStrategy implements MultaStrategy {
    @Override
    public BigDecimal calcularMulta(long diasAtraso) {
        // 1-7 dias: R$ 0,50/dia
        // 8-14 dias: R$ 1,00/dia
        // 15+ dias: R$ 2,00/dia
    }
}

// Contexto que usa a estratégia
public class MultaCalculator {
    private MultaStrategy strategy;
    
    public void setStrategy(MultaStrategy strategy) {
        this.strategy = strategy;
    }
    
    public BigDecimal calcular(long diasAtraso) {
        return strategy.calcularMulta(diasAtraso);
    }
}
```

**Diagrama:**

```
┌─────────────────┐         ┌─────────────────┐
│ MultaCalculator │────────▶│  MultaStrategy  │ (Interface)
│   (Contexto)    │         │+ calcularMulta()│
└─────────────────┘         └────────┬────────┘
                                     │
                    ┌────────────────┼────────────────┐
                    ▼                ▼                ▼
           ┌──────────────┐  ┌──────────────┐  ┌──────────────┐
           │ MultaFixa    │  │MultaProgress.│  │ (Extensível) │
           │  Strategy    │  │   Strategy   │  │              │
           └──────────────┘  └──────────────┘  └──────────────┘
```

---

### 5.3 Observer

**Localização:** `com.biblioteca.pattern.observer`

**Propósito:** Notificar automaticamente múltiplos objetos quando um empréstimo é criado ou devolvido.

**Implementação:**

```java
// Interface do observador
public interface EmprestimoObserver {
    void onEmprestimoCriado(Emprestimo emprestimo);
    void onEmprestimoDevolvido(Emprestimo emprestimo);
}

// Observador de email
public class EmailNotificationObserver implements EmprestimoObserver {
    @Override
    public void onEmprestimoCriado(Emprestimo emprestimo) {
        // Envia email de confirmação
    }
}

// Observador de log
public class LoggingEmprestimoObserver implements EmprestimoObserver {
    @Override
    public void onEmprestimoCriado(Emprestimo emprestimo) {
        // Registra no log
    }
}

// Publisher (Subject)
public class EmprestimoEventPublisher {
    private List<EmprestimoObserver> observers = new ArrayList<>();
    
    public void addObserver(EmprestimoObserver observer) {
        observers.add(observer);
    }
    
    public void notifyEmprestimoCriado(Emprestimo emprestimo) {
        observers.forEach(o -> o.onEmprestimoCriado(emprestimo));
    }
}
```

**Diagrama:**

```
┌─────────────────────────┐
│ EmprestimoEventPublisher│ (Subject)
│ + addObserver()         │
│ + notifyEmprestimoCriado│
└───────────┬─────────────┘
            │ notifica
            ▼
┌─────────────────────────┐
│   EmprestimoObserver    │ (Interface)
│ + onEmprestimoCriado()  │
│ + onEmprestimoDevolvido │
└───────────┬─────────────┘
            │
    ┌───────┴───────┐
    ▼               ▼
┌────────────┐  ┌────────────┐
│EmailNotif. │  │ Logging    │
│ Observer   │  │ Observer   │
└────────────┘  └────────────┘
```

---

### 5.4 Singleton

**Localização:** `com.biblioteca.pattern.singleton`

**Propósito:** Garantir uma única instância do gerenciador de configurações em toda a aplicação.

**Implementação:**

```java
public class ConfigurationManager {
    private static volatile ConfigurationManager instance;
    private final Map<String, String> configurations;
    
    private ConfigurationManager() {
        configurations = new HashMap<>();
        loadDefaultConfigurations();
    }
    
    public static ConfigurationManager getInstance() {
        if (instance == null) {
            synchronized (ConfigurationManager.class) {
                if (instance == null) {
                    instance = new ConfigurationManager();
                }
            }
        }
        return instance;
    }
    
    public String getConfig(String key) {
        return configurations.get(key);
    }
}
```

**Diagrama:**

```
┌─────────────────────────────┐
│    ConfigurationManager     │
├─────────────────────────────┤
│ - instance: Configuration.. │ (static)
│ - configurations: Map       │
├─────────────────────────────┤
│ - ConfigurationManager()    │ (private)
│ + getInstance(): Config...  │ (static)
│ + getConfig(key): String    │
└─────────────────────────────┘
            │
            │ única instância
            ▼
    ┌───────────────┐
    │   instance    │
    └───────────────┘
```

---

## 6. Diagrama de Classes

### 6.1 Entidades Principais

```
┌─────────────────┐       ┌─────────────────┐
│     Usuario     │       │    Categoria    │
├─────────────────┤       ├─────────────────┤
│ - id: Long      │       │ - id: Long      │
│ - nome: String  │       │ - nome: String  │
│ - email: String │       │ - descricao: Str│
│ - senha: String │       └────────┬────────┘
│ - role: Role    │                │ 1
│ - ativo: boolean│                │
└────────┬────────┘                │
         │ 1                       │
         │                         │ *
         │ *              ┌────────┴────────┐
┌────────┴────────┐       │      Livro      │
│   Emprestimo    │       ├─────────────────┤
├─────────────────┤       │ - id: Long      │
│ - id: Long      │       │ - titulo: String│
│ - dataEmprestimo│  *    │ - isbn: String  │
│ - dataDevolucao │◄──────│ - qtdTotal: int │
│ - status: Status│   1   │ - qtdDisponivel │
│ - multa: Decimal│       │ - status: Status│
└─────────────────┘       └────────┬────────┘
                                   │ *
                                   │
                                   │ *
                          ┌────────┴────────┐
                          │      Autor      │
                          ├─────────────────┤
                          │ - id: Long      │
                          │ - nome: String  │
                          │ - nacionalidade │
                          │ - biografia     │
                          └─────────────────┘
```

### 6.2 Relacionamentos

| Entidade A | Relacionamento | Entidade B |
|------------|----------------|------------|
| Livro | N:N | Autor |
| Livro | N:1 | Categoria |
| Emprestimo | N:1 | Livro |
| Emprestimo | N:1 | Usuario |

---

## 7. Diagrama do Banco de Dados

### 7.1 Modelo Relacional

```
┌─────────────────────────────────────────────────────────────────┐
│                          USUARIOS                                │
├─────────────────────────────────────────────────────────────────┤
│ PK  id              BIGINT AUTO_INCREMENT                       │
│     nome            VARCHAR(100) NOT NULL                       │
│     email           VARCHAR(100) NOT NULL UNIQUE                │
│     senha           VARCHAR(255) NOT NULL                       │
│     telefone        VARCHAR(20)                                 │
│     endereco        VARCHAR(200)                                │
│     role            ENUM('ADMIN','USER') NOT NULL               │
│     ativo           BOOLEAN NOT NULL                            │
│     data_cadastro   TIMESTAMP                                   │
│     ultimo_acesso   TIMESTAMP                                   │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                         CATEGORIAS                               │
├─────────────────────────────────────────────────────────────────┤
│ PK  id              BIGINT AUTO_INCREMENT                       │
│     nome            VARCHAR(100) NOT NULL UNIQUE                │
│     descricao       VARCHAR(500)                                │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                           AUTORES                                │
├─────────────────────────────────────────────────────────────────┤
│ PK  id              BIGINT AUTO_INCREMENT                       │
│     nome            VARCHAR(150) NOT NULL                       │
│     nacionalidade   VARCHAR(100)                                │
│     data_nascimento DATE                                        │
│     biografia       TEXT                                        │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                           LIVROS                                 │
├─────────────────────────────────────────────────────────────────┤
│ PK  id                   BIGINT AUTO_INCREMENT                  │
│     titulo               VARCHAR(200) NOT NULL                  │
│     isbn                 VARCHAR(20) UNIQUE                     │
│     ano_publicacao       INTEGER                                │
│     editora              VARCHAR(100)                           │
│     edicao               VARCHAR(50)                            │
│     numero_paginas       INTEGER                                │
│     sinopse              TEXT                                   │
│     quantidade_total     INTEGER NOT NULL                       │
│     quantidade_disponivel INTEGER NOT NULL                      │
│     url_capa             VARCHAR(255)                           │
│     status               ENUM('DISPONIVEL','INDISPONIVEL',...)  │
│     data_cadastro        DATE                                   │
│ FK  categoria_id         BIGINT → CATEGORIAS(id)                │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                        LIVRO_AUTOR                               │
├─────────────────────────────────────────────────────────────────┤
│ PK,FK  livro_id     BIGINT → LIVROS(id)                         │
│ PK,FK  autor_id     BIGINT → AUTORES(id)                        │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                        EMPRESTIMOS                               │
├─────────────────────────────────────────────────────────────────┤
│ PK  id                      BIGINT AUTO_INCREMENT               │
│     data_emprestimo         DATE NOT NULL                       │
│     data_prevista_devolucao DATE NOT NULL                       │
│     data_devolucao          DATE                                │
│     status                  ENUM('ATIVO','DEVOLVIDO',...)       │
│     multa                   DECIMAL(10,2)                       │
│     observacoes             VARCHAR(500)                        │
│ FK  livro_id                BIGINT → LIVROS(id)                 │
│ FK  usuario_id              BIGINT → USUARIOS(id)               │
└─────────────────────────────────────────────────────────────────┘
```

---

## 8. Boas Práticas (Clean Code e SOLID)

### 8.1 Princípios SOLID Aplicados

#### S - Single Responsibility Principle (SRP)
Cada classe tem uma única responsabilidade:
- `LivroService` - apenas operações de livros
- `EmprestimoService` - apenas operações de empréstimos
- `MultaStrategy` - apenas cálculo de multas

#### O - Open/Closed Principle (OCP)
O sistema está aberto para extensão, fechado para modificação:
- Novas estratégias de multa podem ser adicionadas sem alterar código existente
- Novos tipos de empréstimo via Factory Method

#### L - Liskov Substitution Principle (LSP)
Subtipos podem substituir seus tipos base:
- Qualquer `MultaStrategy` pode ser usado no `MultaCalculator`
- Qualquer `EmprestimoFactory` produz empréstimos válidos

#### I - Interface Segregation Principle (ISP)
Interfaces pequenas e específicas:
- `LivroService`, `AutorService`, `CategoriaService` separados
- `EmprestimoObserver` com métodos específicos

#### D - Dependency Inversion Principle (DIP)
Dependência de abstrações, não de implementações:
- Services dependem de interfaces de Repository
- Injeção de dependência via construtor

### 8.2 Práticas de Clean Code

```java
// ✅ Nomes significativos
public List<LivroDTO> buscarPorCategoria(Long categoriaId)

// ✅ Métodos pequenos e focados
public boolean isDisponivel() {
    return this.quantidadeDisponivel > 0;
}

// ✅ Validações claras
@NotBlank(message = "Título é obrigatório")
@Size(min = 1, max = 200, message = "Título deve ter entre 1 e 200 caracteres")
private String titulo;

// ✅ Tratamento de exceções específico
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

// ✅ DTOs para transferência de dados
public class LivroDTO {
    // Separação entre entidade JPA e dados da API
}
```

---

## 9. Segurança

### 9.1 Autenticação

O sistema utiliza **Spring Security** com autenticação baseada em formulário:

- Login via email/senha
- Senhas criptografadas com BCrypt
- Sessão gerenciada pelo Spring Security
- Proteção CSRF habilitada

### 9.2 Autorização

Dois níveis de acesso implementados:

| Role | Permissões |
|------|------------|
| **ADMIN** | Acesso total: CRUD de livros, autores, categorias, usuários |
| **USER** | Acesso limitado: visualização, empréstimos próprios, perfil |

### 9.3 Configuração de Segurança

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/home", "/login", "/register").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard")
            );
        return http.build();
    }
}
```

---

## 10. API REST

### 10.1 Endpoints Disponíveis

A API está documentada via **Swagger/OpenAPI** em: `/swagger-ui.html`

#### Livros (`/api/livros`)
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/livros` | Lista paginada |
| GET | `/api/livros/{id}` | Busca por ID |
| POST | `/api/livros` | Criar novo |
| PUT | `/api/livros/{id}` | Atualizar |
| DELETE | `/api/livros/{id}` | Excluir |

#### Autores (`/api/autores`)
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/autores` | Lista todos |
| GET | `/api/autores/{id}` | Busca por ID |
| POST | `/api/autores` | Criar novo |
| PUT | `/api/autores/{id}` | Atualizar |
| DELETE | `/api/autores/{id}` | Excluir |

#### Categorias (`/api/categorias`)
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/categorias` | Lista todas |
| GET | `/api/categorias/{id}` | Busca por ID |
| POST | `/api/categorias` | Criar nova |
| PUT | `/api/categorias/{id}` | Atualizar |
| DELETE | `/api/categorias/{id}` | Excluir |

#### Empréstimos (`/api/emprestimos`)
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/emprestimos` | Lista todos |
| POST | `/api/emprestimos` | Criar empréstimo |
| PUT | `/api/emprestimos/{id}/devolver` | Registrar devolução |

### 10.2 Exemplo de Requisição

```bash
# Listar livros
curl -X GET http://localhost:8080/api/livros

# Criar livro (autenticado)
curl -X POST http://localhost:8080/api/livros \
  -H "Content-Type: application/json" \
  -u admin@biblioteca.com:admin123 \
  -d '{
    "titulo": "Clean Code",
    "isbn": "978-0132350884",
    "anoPublicacao": 2008,
    "quantidadeTotal": 5,
    "categoriaId": 1,
    "autoresIds": [1]
  }'
```

---

## 11. Interface do Usuário

### 11.1 Telas Principais

| Tela | Descrição | Acesso |
|------|-----------|--------|
| Home | Página inicial com estatísticas | Público |
| Login | Autenticação de usuários | Público |
| Cadastro | Registro de novos usuários | Público |
| Dashboard | Painel com visão geral | Autenticado |
| Livros | Lista e gerenciamento de livros | Autenticado |
| Autores | Lista e gerenciamento de autores | Autenticado |
| Categorias | Lista e gerenciamento de categorias | Autenticado |
| Empréstimos | Gerenciamento de empréstimos | Autenticado |
| Perfil | Dados do usuário logado | Autenticado |
| Admin | Gerenciamento de usuários | ADMIN |

### 11.2 Tecnologias Frontend

- **Thymeleaf:** Template engine integrada ao Spring
- **Bootstrap 5.3:** Framework CSS responsivo
- **Bootstrap Icons:** Biblioteca de ícones

---

## 12. Como Executar

### 12.1 Pré-requisitos

- Java 17+
- Maven 3.8+
- Git

### 12.2 Execução Local

```bash
# Clonar repositório
git clone https://github.com/jonathanperobeli/biblioteca-virtual.git
cd biblioteca-virtual

# Executar
./mvnw spring-boot:run

# Acessar
# http://localhost:8080
```

### 12.3 Usuários de Teste

| Tipo | Email | Senha |
|------|-------|-------|
| Admin | admin@biblioteca.com | admin123 |
| Usuário | joao@email.com | 123456 |

### 12.4 URLs Importantes

| URL | Descrição |
|-----|-----------|
| http://localhost:8080 | Aplicação |
| http://localhost:8080/swagger-ui.html | Documentação API |
| http://localhost:8080/h2-console | Console do banco H2 |

---

## 13. Conclusão

O Sistema de Biblioteca Digital foi desenvolvido com sucesso, atendendo a todos os requisitos propostos:

### Requisitos Atendidos

- ✅ Aplicação web completa com Spring Boot
- ✅ CRUD com persistência via JPA/Hibernate
- ✅ 4 padrões de projeto GoF (Factory, Strategy, Observer, Singleton)
- ✅ Arquitetura MVC bem definida
- ✅ Autenticação e autorização com Spring Security
- ✅ API REST documentada com Swagger/OpenAPI
- ✅ Interface web funcional com Thymeleaf/Bootstrap
- ✅ Dashboard com estatísticas
- ✅ Código limpo seguindo princípios SOLID

### Aprendizados

O desenvolvimento deste projeto proporcionou experiência prática em:

1. Desenvolvimento de aplicações enterprise com Spring Boot
2. Implementação de padrões de projeto em cenários reais
3. Configuração de segurança em aplicações web
4. Documentação de APIs REST
5. Trabalho colaborativo com versionamento Git

---

## Repositório

🔗 **GitHub:** https://github.com/jonathanperobeli/bibliotecavirtual

---

*Documento gerado em Novembro de 2025*
