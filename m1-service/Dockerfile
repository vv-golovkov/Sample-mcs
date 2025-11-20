# -----build
FROM maven:3.9.9-eclipse-temurin-22-alpine AS build
WORKDIR /app

COPY pom.xml .
COPY cs-common ./cs-common/
COPY m1-service ./m1-service/
# COPY m1-service/pom.xml .
COPY ./x_config/logback/logback-spring.xml ./m1-service/src/main/resources/
RUN mvn clean install -DskipTests

# RUN mvn dependency:go-offline -B
# COPY m1-service/src ./src
# RUN mvn clean package -DskipTests

# -----run
FROM eclipse-temurin:22-jdk-alpine
WORKDIR /app

# RUN apk add --no-cache curl
RUN apk add --no-cache curl unzip \
 && curl -L https://releases.hashicorp.com/consul/1.16.1/consul_1.16.1_linux_amd64.zip -o consul.zip \
 && unzip consul.zip \
 && mv consul /usr/local/bin/ \
 && chmod +x /usr/local/bin/consul \
 && rm consul.zip

COPY --from=build /app/m1-service/target/*.jar app.jar
# COPY --from=build /app/target/*.jar app.jar

# add Consul KV from local properties
# COPY m1-service/src/main/resources/application*.yaml /app/
# COPY m1-service/entrypoint.sh /entrypoint.sh
# RUN chmod +x /entrypoint.sh

EXPOSE 8080
# ENTRYPOINT ["/entrypoint.sh"]
ENTRYPOINT ["java", "-jar", "/app/app.jar"]