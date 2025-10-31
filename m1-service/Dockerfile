#-----build
FROM maven:3.9.9-eclipse-temurin-22-alpine AS build
WORKDIR /app
COPY pom.xml .
#COPY ../openapi /app/openapi
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

#-----run
FROM eclipse-temurin:22-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
#COPY --from=build /app/openapi /app/openapi

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]