FROM eclipse-temurin:17.0.7_7-jdk-jammy AS bridging-service

WORKDIR /apps/

COPY ./target/bridging-service-0.0.1-SNAPSHOT.jar .

CMD ["java","-jar",“bridging-service-0.0.1-SNAPSHOT.jar"]