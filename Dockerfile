FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# ==========================
# Etapa 2: Imagen final (runtime)
# ==========================
FROM eclipse-temurin:17-jdk-alpine

# Establece el directorio de trabajo
WORKDIR /app

# Copia el jar generado desde la etapa anterior
COPY --from=builder /app/target/communityapp-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto 8080
EXPOSE 8080

# Variable de entorno opcional (puede ser sobrescrita en Render)
ENV JAVA_OPTS=""

# Comando para ejecutar el contenedor
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

ARG JAR_FILE=target/*.jar
