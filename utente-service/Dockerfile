

### Base Java Version
FROM eclipse-temurin:17-jre-alpine

### Working Directory
WORKDIR /app

### Jar Creation (Target Copy)
COPY target/*.jar app.jar

## Execution Entrypoints at RunTime
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
