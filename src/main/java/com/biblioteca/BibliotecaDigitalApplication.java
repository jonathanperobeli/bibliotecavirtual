package com.biblioteca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação Sistema de Biblioteca Digital.
 * 
 * Esta aplicação demonstra:
 * - Arquitetura MVC com Spring Boot
 * - Padrões de Projeto GoF (Singleton, Factory, Strategy, Observer)
 * - Boas práticas de Clean Code e SOLID
 * - Autenticação e Autorização com Spring Security
 * - Documentação de API com Swagger/OpenAPI
 * 
 * @author Equipe de Desenvolvimento
 * @version 1.0.0
 */
@SpringBootApplication
public class BibliotecaDigitalApplication {

    public static void main(String[] args) {
        SpringApplication.run(BibliotecaDigitalApplication.class, args);
    }
}
