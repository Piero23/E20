

### Base Java Version
FROM eclipse-temurin:17-jre-alpine

### Working Directory
WORKDIR /app

### Jar Creation (Target Copy)
COPY target/*.jar app.jar

### Exposing Port (for Documentation)
EXPOSE 8088

## Execution Entrypoints at RunTime
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
