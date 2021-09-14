FROM openjdk:11-jre-slim
COPY /target/library.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
