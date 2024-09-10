FROM openjdk:17-jdk

COPY target/Wunderlist-0.0.1-SNAPSHOT.jar /app.jar
LABEL authors="metehan.kara"

ENTRYPOINT ["java", "-jar", "/app.jar"]