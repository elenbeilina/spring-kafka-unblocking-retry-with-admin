FROM openjdk:17-jdk-alpine
COPY /target/consumer-*.jar consumer-*.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/consumer-*.jar"]