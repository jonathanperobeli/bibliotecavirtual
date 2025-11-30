package com.biblioteca.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção para recursos não encontrados.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resource, Long id) {
        super(String.format("%s não encontrado(a) com ID: %d", resource, id));
    }

    public ResourceNotFoundException(String resource, String field, String value) {
        super(String.format("%s não encontrado(a) com %s: %s", resource, field, value));
    }
}
