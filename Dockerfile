FROM openjdk:11-jre-slim

ARG PROJECT
ARG SERVICE_PORT

EXPOSE ${SERVICE_PORT}

RUN mkdir /${PROJECT}
WORKDIR /${PROJECT}

COPY *.jar ./app.jar

CMD ["java","-jar","app.jar"]
