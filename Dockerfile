FROM eclipse-temurin:23-jre-alpine
WORKDIR /app
#COPY *.jar app.jar
COPY . ./
RUN ./mvnw -DoutputFile=target/mvn-dependency-list.log -B -DskipTests clean dependency:list install
#CMD ["java", "-jar", "app.jar"]
CMD ["sh", "-c", "java -jar target/*.jar"]
