# ===== BUILD STAGE =====
FROM maven:3.9.9-eclipse-temurin-22-alpine AS build
WORKDIR /app

# COPY pom.xml .
COPY cs-apiGateway/pom.xml .

RUN mvn dependency:go-offline -B

# COPY src ./src
COPY cs-apiGateway/src ./src

RUN mvn clean package -DskipTests

# ===== RUN STAGE =====
FROM eclipse-temurin:22-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]