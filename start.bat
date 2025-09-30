@echo off
title CommunityApp - Spring Boot
color 0A
echo.
echo ==========================================
echo    CommunityApp - Spring Boot v1.0
echo ==========================================
echo.
echo Iniciando aplicacion...
echo URL: http://localhost:8080
echo Login: user / password
echo.
echo Para detener: Ctrl+C
echo.
echo Aguarde mientras inicia...
echo.

REM Iniciar Maven en modo silencioso
mvn spring-boot:run