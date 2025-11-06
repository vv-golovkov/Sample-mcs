#-----build
FROM maven:3.9.9-eclipse-temurin-22-alpine AS build
WORKDIR /app

COPY pom.xml .
COPY cs-common ./cs-common/
COPY m1-service ./m1-service/
# COPY m1-service/pom.xml .
#  COPY ../openapi /app/openapi
RUN mvn clean install -DskipTests

# RUN mvn dependency:go-offline -B
# COPY m1-service/src ./src
#  COPY logback-spring.xml /app/m1-service/src/main/resources
# RUN mvn clean package -DskipTests

#-----run
FROM eclipse-temurin:22-jdk-alpine
WORKDIR /app

# Встановлюємо curl для healthcheck (not recommended for PROD; use manual HTTP request)
RUN apk add --no-cache curl

COPY --from=build /app/m1-service/target/*.jar app.jar
# COPY --from=build /app/target/*.jar app.jar
#  COPY --from=build /app/openapi /app/openapi

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]