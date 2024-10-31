FROM maven:latest

WORKDIR /app

COPY pom.xml /app/

COPY . /app/

RUN mvn package

cmd ["java", "-jar", "target/bookstorebackend.jar"]