FROM openjdk:11
EXPOSE 8080
WORKDIR /app
COPY . .
RUN mkdir /app/reports
CMD ["java", "-jar", "/app/target/value-0.0.1-SNAPSHOT.jar"]
