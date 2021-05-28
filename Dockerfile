FROM maven:3.6.0-jdk-11-slim AS build

COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml org.apache.maven.plugins:maven-assembly-plugin:assembly

FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/destributedService-1.0-SNAPSHOT.jar /usr/local/lib/destributedService.jar

EXPOSE 9090
