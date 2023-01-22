FROM maven:3.8.7 as BUILDER
ARG VERSION=0.0.1-SNAPSHOT
WORKDIR /build/
COPY pom.xml /build/
COPY src/main/java /build/src/

RUN mvn clean package
COPY target/avow-task-0.0.1-SNAPSHOT.jar target/application.jar
FROM openjdk:19-alpine
WORKDIR /app/

COPY --from=BUILDER /build/target/application.jar /app/
ENTRYPOINT [ "java", "-jar", "/app/application.jar" ]