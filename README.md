# ViveMedellin-f4


## Dependencias principales
- Spring Web
- Spring Security
- H2 Database
- Lombok


##  Cómo ejecutar

### Requisitos
- JDK 17 o superior
- Maven 3.6+


**Para ejecutar**
mvn spring-boot:run


**Aplicación disponible en:** http://localhost:8080

##  Credenciales de acceso
- **Usuario:** `user`
- **Contraseña:** `password`

## Probar login HU01

$body = '{"username":"user","password":"password"}'
$response = Invoke-WebRequest -Uri "http://localhost:8080/api/auth/login" -Method POST -Body $body -ContentType "application/json"
$response.Content





