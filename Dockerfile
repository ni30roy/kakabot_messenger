# ---------- BUILD STAGE ----------
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Copy Maven wrapper & pom
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Download dependencies (cache friendly)
RUN chmod +x mvnw && ./mvnw dependency:go-offline

# Copy source
COPY src src

# Build jar
RUN ./mvnw clean package -DskipTests


# ---------- RUNTIME STAGE ----------
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port (Render / Docker use PORT env)
EXPOSE 8086

# Start app
ENTRYPOINT ["java","-jar","app.jar"]