# 後端多模組 Maven 建置，供 Zeabur 部署（main 分支）
# builder 需含 Maven，用官方 maven + eclipse-temurin-17 映像
FROM maven:3.9-eclipse-temurin-17-alpine AS builder
WORKDIR /app

COPY pom.xml ./
COPY brainwave-core/pom.xml brainwave-core/
COPY brainwave-service/pom.xml brainwave-service/
COPY brainwave-backend/pom.xml brainwave-backend/
RUN mvn -B dependency:go-offline -DskipTests

COPY brainwave-core/src brainwave-core/src
COPY brainwave-service/src brainwave-service/src
COPY brainwave-backend/src brainwave-backend/src
RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/brainwave-backend/target/brainwave-backend-*.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
