package com.biblioteca.pattern.singleton;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * PADRÃO DE PROJETO: SINGLETON
 * 
 * Gerenciador de configurações do sistema.
 * Garante uma única instância de configurações em toda a aplicação.
 * 
 * Nota: Spring gerencia beans como Singleton por padrão,
 * mas esta classe demonstra o padrão de forma explícita.
 * 
 * Benefícios:
 * - Única instância compartilhada
 * - Acesso global controlado
 * - Estado consistente
 */
@Component
public class ConfigurationManager {

    // Instância única (Thread-safe com ConcurrentHashMap)
    private static ConfigurationManager instance;
    
    private final Map<String, Object> configurations;
    private final AtomicLong accessCount;

    private ConfigurationManager() {
        this.configurations = new ConcurrentHashMap<>();
        this.accessCount = new AtomicLong(0);
        
        // Configurações padrão
        configurations.put("MAX_EMPRESTIMOS_POR_USUARIO", 3);
        configurations.put("DIAS_EMPRESTIMO_PADRAO", 14);
        configurations.put("DIAS_EMPRESTIMO_ESTENDIDO", 30);
        configurations.put("VALOR_MULTA_POR_DIA", 2.0);
        configurations.put("DIAS_AVISO_VENCIMENTO", 3);
        configurations.put("PERMITIR_RENOVACAO", true);
        configurations.put("MAX_RENOVACOES", 1);
    }

    /**
     * Retorna a instância única do ConfigurationManager.
     * Double-checked locking para thread-safety.
     */
    public static synchronized ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }

    /**
     * Obtém uma configuração pelo nome.
     */
    @SuppressWarnings("unchecked")
    public <T> T getConfig(String key) {
        accessCount.incrementAndGet();
        return (T) configurations.get(key);
    }

    /**
     * Obtém uma configuração com valor padrão.
     */
    @SuppressWarnings("unchecked")
    public <T> T getConfig(String key, T defaultValue) {
        accessCount.incrementAndGet();
        Object value = configurations.get(key);
        return value != null ? (T) value : defaultValue;
    }

    /**
     * Define uma configuração.
     */
    public void setConfig(String key, Object value) {
        configurations.put(key, value);
    }

    /**
     * Verifica se uma configuração existe.
     */
    public boolean hasConfig(String key) {
        return configurations.containsKey(key);
    }

    /**
     * Retorna todas as configurações.
     */
    public Map<String, Object> getAllConfigs() {
        return Map.copyOf(configurations);
    }

    /**
     * Retorna contagem de acessos (para métricas).
     */
    public long getAccessCount() {
        return accessCount.get();
    }

    /**
     * Recarrega configurações padrão.
     */
    public void resetToDefaults() {
        configurations.clear();
        configurations.put("MAX_EMPRESTIMOS_POR_USUARIO", 3);
        configurations.put("DIAS_EMPRESTIMO_PADRAO", 14);
        configurations.put("DIAS_EMPRESTIMO_ESTENDIDO", 30);
        configurations.put("VALOR_MULTA_POR_DIA", 2.0);
        configurations.put("DIAS_AVISO_VENCIMENTO", 3);
        configurations.put("PERMITIR_RENOVACAO", true);
        configurations.put("MAX_RENOVACOES", 1);
    }
}
