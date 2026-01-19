# 1. BUILD STAGE
FROM eclipse-temurin:24-jdk AS build

WORKDIR /app

RUN apt-get update && apt-get install -y dos2unix

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN dos2unix mvnw
RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests


# 2. RUNTIME STAGE
FROM eclipse-temurin:24-jdk
ARG PROFILE=prod

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENV DB_URL=jdbc:postgresql://postgres:5432/SmartSaudiStockAdvisor
ENV DB_USERNAME=postgres
ENV DB_PASSWORD=placeholder_password
ENV JWT_SECRETE=placeholder_secret
ENV JWT_EXPIRATION_TIME=900000
ENV ACTIVE_PROFILE=${PROFILE}

CMD ["java", "-jar", "-Dspring.profiles.active=${ACTIVE_PROFILE}", "app.jar"]