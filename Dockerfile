FROM openjdk:17-jdk-alpine
MAINTAINER petru.petru
COPY ../build/libs/GitMeta-1.0-SNAPSHOT.jar server.jar
ENTRYPOINT ["java","-jar","/server.jar"]