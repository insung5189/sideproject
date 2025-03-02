FROM openjdk:17-jdk-slim

ARG JAR_FILE=build/libs/sideproject-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
RUN apt-get update && apt-get install -y curl  # `curl` 설치 추가
EXPOSE 7070
ENTRYPOINT ["java","-jar","/app.jar"]
