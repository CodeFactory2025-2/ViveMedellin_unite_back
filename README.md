# Backend - Creación y Gestión de Grupos/Comunidades 
ViveMedellin - Feature4 

Este proyecto es un backend desarrollado en Spring Boot, conectado a PostgreSQL (Supabase), que implementa:

- Registro y login con JWT
- Creación y gestión de grupos
- Seguridad con Spring Security

## Tecnologías 


| Componente | Tecnología  | 
|--------|--------|
| Backend |Spring-Boot| 
|Seguridad|Spring Security + JWT|
|Base de datos|PostgreSQL (Supabase)|
|ORM|Hibernate JPA|
|Pruebas|Postman / Swagger|



## API Endpoints


### -  Autenticación

| Método | Endpoint | Descripción|
|--------|--------|--------|
|POST| api/auth/login| Inicia sesión con usuario y contraseña y devuelve un token|
|GET| api/auth/check | Verifica si el usuario tiene aun la sesión válida|

#### Request: Inicio de sesión 


```java
 /auth/register
```

```json
{
  "username": "maria",
  "password": "123456"
}
```
#### Respuesta:

```json
{
  "message": "Autenticación exitosa",
  "Token": "eyJhbGciOi..."
}
```



### - Grupos

| Método | Endpoint | Descripción|
|--------|--------|--------|
|POST| api/groups | Crea un nuevo grupo.|
|POST| /api/groups/{id}/join| Permite que un usuario se una a un grupo existente|
|DELETE| /api/groups/{id} | Elimina un grupo solo si el usuario tiene los permisos|

#### Request: Crear Grupo

Token requerido

```java
 /api/groups
```

```json
{
"id": "1"
"nombreGrupo": "creadores de matematicas",
"descripcion": "grupo para aprender",
"categoria": "educacion",
"categoriaOtro": "",
"privacidad": "publico",
"aceptaReglas": true,
"tema": "matematicas"
}
```

#### Respuesta 
```json
{
"message": "Grupo creado exitosamente"
}
```
### -  Publicaciones 

| Método | Endpoint | Descripción|
|--------|--------|--------|
|GET|/api/groups/{groupId}/posts| Obtiene todas las publicaciones de un grupo en especifico|
|POST | /api/groups/{groupId}/posts | Crea una nueva publicación dentro del grupo|
|GET | /api/groups/{groupId}/posts/search| Busca publicaciones dentro del grupo por palabras claves | 

#### Request: Crear una nueva publicación

Token requerido

```java
 /api/groups/{groupId}/posts
```
#### Respuesta 
```json
{
    "post": {
        "id": 1,
        "group": {
            "id": 5,
            "nombreGrupo": "Club de Fotografía Medellín",
            "descripcion": "Un grupo para los amantes de la fotografía urbana.",
            "categoria": "Arte",
            "categoriaOtro": "",
            "privacidad": "Público",
            "aceptaReglas": true,
            "adminId": 1,
            "tema": "Fotografía Urbana"
        },
        "author": {
            "id": 1,
            "username": "admin",
            "password": "$2a$12$Xn1K5yBvfTjD.A.jSicev.ehCtSK/UNjdSYaA0yMp8LmsZsY/JpAC",
            "enabled": true,
            "authorities": null,
            "credentialsNonExpired": true,
            "accountNonExpired": true,
            "accountNonLocked": true
        },
        "content": "Esta es una nueva publicación en el grupo de tecnología.",
        "createdAt": "2025-10-25T05:08:31.6404133"
    },
    "mensaje": "Publicación creada correctamente"
}
```


## Tablas creadas automáticamente vía JPA - Base de datos
| Tabla | Función | 
|--------|--------|
|users|Usuarios del sistema|
|groups|Grupos creados|
|group_members|Relación usuario–grupo (miembros)|
|group_posts|Publicaciones dentro de los grupos|
|group_rules|Reglas de cada grupo|

### Ejemplo de tabla
```sql
CREATE TABLE groups (
  id BIGSERIAL PRIMARY KEY,
  nombre_grupo VARCHAR(255) NOT NULL,
  descripcion TEXT,
  categoria VARCHAR(255),
  categoria_otro VARCHAR(255),
  privacidad VARCHAR(50),
  acepta_reglas BOOLEAN,
  admin_id BIGINT REFERENCES users(id),
  tema VARCHAR(255)
);
```

```sql
CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  username VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL
);
```

## Errores comunes 
|Error| Causa|
|--------|--------|
|403 Forbidden|Falta token Bearer|
|401 Unauthorized|Token inválido o expirado|
|DB connect error|Revisar variables y cadena JDBC|

##  Cómo ejecutar

### Requisitos
- JDK 17 o superior
- Maven 3.6+


### Para ejecutar
```bash
mvn spring-boot:run
```

## Swagger
```bash
http://localhost:8080/swagger-ui/index.html
```
### Aplicación disponible en:
```bash
http://localhost:8080
```


