#!/bin/bash

echo "========================================"
echo " 📚 BIBLIOTECA DIGITAL"
echo "========================================"
echo ""

# Verificar se Java está instalado
if ! command -v java &> /dev/null; then
    echo "❌ Java não encontrado!"
    echo ""
    echo "Por favor, instale o Java 17 ou superior:"
    echo "https://adoptium.net/"
    echo ""
    exit 1
fi

# Verificar versão do Java
echo "🔍 Verificando versão do Java..."
java_version=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)

if [ "$java_version" -lt 17 ]; then
    echo ""
    echo "⚠️  AVISO: Este projeto requer Java 17 ou superior"
    echo "Versão atual: $java_version"
    echo ""
fi

echo ""
echo "🚀 Iniciando aplicação..."
echo ""
echo "Aguarde... Isso pode levar alguns segundos na primeira execução."
echo ""

# Executar a aplicação
./mvnw spring-boot:run

exit_code=$?

if [ $exit_code -eq 0 ]; then
    echo ""
    echo "========================================"
    echo "✅ Aplicação finalizada com sucesso!"
    echo "========================================"
else
    echo ""
    echo "========================================"
    echo "❌ Erro ao executar a aplicação"
    echo "========================================"
fi

echo ""
