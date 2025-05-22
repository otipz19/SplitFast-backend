FROM maven:3.9.9-amazoncorretto-21
COPY target/split-fast.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]