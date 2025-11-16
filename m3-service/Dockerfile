#-----build
FROM maven:3.9.9-eclipse-temurin-22-alpine AS build
WORKDIR /app

# COPY pom.xml .
COPY m3-service/pom.xml .

RUN mvn dependency:go-offline -B

# COPY src ./src
COPY m3-service/src ./src

RUN mvn clean package -DskipTests

#-----run
FROM eclipse-temurin:22-jdk-alpine
WORKDIR /app

RUN apk add --no-cache curl

COPY --from=build /app/target/*.jar app.jar
# COPY --from=build /app/m3-service/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]