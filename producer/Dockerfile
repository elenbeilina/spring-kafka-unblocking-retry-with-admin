FROM openjdk:17-jdk-alpine
COPY /target/producer-*.jar producer-*.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/producer-*.jar"]