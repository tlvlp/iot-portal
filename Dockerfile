FROM openjdk:11-jre-slim

ARG PROJECT
ARG SERVICE_PORT
ARG JAR_FILE

EXPOSE ${SERVICE_PORT}

RUN mkdir /${PROJECT}
WORKDIR /${PROJECT}

ADD target/${JAR_FILE} ./app.jar

CMD ["java","-jar","app.jar"]
