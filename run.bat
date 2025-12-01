@echo off
chcp 65001 >nul
cls

echo ========================================
echo  📚 BIBLIOTECA DIGITAL
echo ========================================
echo.

REM Verificar se Java está instalado
where java >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Java não encontrado!
    echo.
    echo Por favor, instale o Java 17 ou superior:
    echo https://adoptium.net/
    echo.
    pause
    exit /b 1
)

REM Verificar versão do Java
echo 🔍 Verificando versão do Java...
java -version 2>&1 | findstr /C:"17" >nul
if %ERRORLEVEL% NEQ 0 (
    java -version 2>&1 | findstr /C:"21" >nul
    if %ERRORLEVEL% NEQ 0 (
        echo.
        echo ⚠️  AVISO: Este projeto requer Java 17 ou superior
        echo.
    )
)

echo.
echo 🚀 Iniciando aplicação...
echo.
echo Aguarde... Isso pode levar alguns segundos na primeira execução.
echo.

REM Executar a aplicação
call mvnw.cmd spring-boot:run

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo ✅ Aplicação finalizada com sucesso!
    echo ========================================
) else (
    echo.
    echo ========================================
    echo ❌ Erro ao executar a aplicação
    echo ========================================
)

echo.
pause
