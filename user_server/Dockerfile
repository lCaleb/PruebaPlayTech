
FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/*.jar app.jar

COPY src/main/resources/application.yml application.yml

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar" ]