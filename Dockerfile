FROM openjdk:11.0-jdk-slim-buster

VOLUME /temp

COPY target/order-service-0.0.1-SNAPSHOT.jar order-service.jar

ENTRYPOINT ["java","-jar","order-service.jar"]