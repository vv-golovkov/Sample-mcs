# ===== BUILD STAGE =====
FROM maven:3.9.9-eclipse-temurin-22-alpine AS build
WORKDIR /app

# because of change in context in docker-compose.yaml to see the root of Sample-mcs
# COPY pom.xml .
COPY cs-configServer/pom.xml .

RUN mvn dependency:go-offline -B

# because of change in context in docker-compose.yaml to see the root of Sample-mcs
# COPY src ./src
COPY cs-configServer/src ./src

RUN mvn clean package -DskipTests

# ===== RUN STAGE =====
FROM eclipse-temurin:22-jdk-alpine
WORKDIR /app

# Встановлюємо curl для healthcheck (not recommended for PROD; use manual HTTP request)
RUN apk add --no-cache curl

COPY --from=build /app/target/*.jar app.jar

# if using local config files
COPY config-repo /app/config-repo

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]