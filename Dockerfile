FROM eclipse-temurin:23-jre-alpine
COPY target/*.jar app.jar
CMD ["java", "-jar", "app.jar"]
