FROM openjdk:17-jdk-alpine
ARG JAR_FILE=build/libs/sideproject-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
COPY .env .env
EXPOSE 7070
ENTRYPOINT ["java","-jar","/app.jar"]