# ViveMedellin-f4

Proyecto base en Spring Boot para CommunityApp.

## Dependencias principales
- Spring Web
- Spring Security
- H2 Database
- Lombok

## Estructura de paquetes
- com.communityapp.auth.controller
- com.communityapp.auth.service
- com.communityapp.auth.config
- com.communityapp.group.controller
- com.communityapp.group.service
- com.communityapp.group.model
- com.communityapp.common


## Endpoints
- POST /api/groups/create

## Cómo ejecutar
1. Instala JDK 17 o superior
2. Ejecuta: `mvn spring-boot:run`


## Enter
Usuario: user
Contraseña: password

## Iniciar sesión 
$body = '{"username":"user","password":"password"}'
$session = New-Object Microsoft.PowerShell.Commands.WebRequestSession
Invoke-WebRequest -Uri "http://localhost:8080/api/auth/login" -Method POST -Body $body -ContentType "application/json" -WebSession $session