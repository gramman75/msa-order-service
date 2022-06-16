FROM openjdk:11.0-jdk-slim-buster

VOLUME /temp

COPY target/order-service-1.0.jar order-service.jar

ENTRYPOINT ["java","-jar","order-service.jar"]