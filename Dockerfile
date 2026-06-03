# ---- build stage ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
# baixa as dependências primeiro (camada cacheada enquanto o pom não muda)
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
# compila e empacota (testes rodam no CI, não no build de deploy)
COPY src ./src
RUN mvn -B -q clean package -DskipTests

# ---- runtime stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# 512 MB no free tier do Render: deixa a JVM usar a RAM do container
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0"
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
