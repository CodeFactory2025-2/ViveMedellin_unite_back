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
$response = Invoke-WebRequest -Uri "http://localhost:8080/api/auth/login" -Method POST -Body $body -ContentType "application/json" -SessionVariable session
$response.Content

## Probar creación de grupo HU02

$bodyGrupo = @{
    nombreGrupo = "Club de Cine"
    descripcion = "Un grupo para cinéfilos."
    tema = "cultura"
    reglas = @("Respetar opiniones", "No spoilers")
    privacidad = "publico"
} | ConvertTo-Json -Depth 3

$bytes = [System.Text.Encoding]::UTF8.GetBytes($bodyGrupo)

try {
    $responseGrupo = Invoke-WebRequest -Uri "http://localhost:8080/api/groups" -Method POST -Body $bytes -ContentType "application/json" -WebSession $session
    $responseGrupo.Content
} catch {
    $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
    $reader.ReadToEnd()
}





