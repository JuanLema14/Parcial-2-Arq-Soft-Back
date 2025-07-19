# Multi-stage build para optimizar el tamaño de la imagen
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .
COPY src ./src

# Construir la aplicación
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

RUN addgroup -g 1001 -S spring && \
    adduser -u 1001 -S spring -G spring

WORKDIR /app

COPY --from=build /app/target/Parcial-2-Arq-Soft-Back-*.jar app.jar

RUN chown -R spring:spring /app

# Cambiar al usuario no root
USER spring:spring

# Exponer el puerto de la aplicación
EXPOSE 8080

# Variables de entorno para la base de datos
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/hospital_db
ENV SPRING_DATASOURCE_USERNAME=hospital_user
ENV SPRING_DATASOURCE_PASSWORD=secret

# Comando de inicio con configuraciones optimizadas para contenedor
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "/app/app.jar"]