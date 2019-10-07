# IoT Portal

## Service
Part of the [tlvlp IoT project](https://github.com/tlvlp/iot-project-summary)'s server side microservices.

This Dockerized SpringBoot-based service is responsible providing providing an interactive portal for the API Gateway:
- Unit monitoring, reporting and interaction
- User management

## Building and publishing JAR + Docker image
This project is using the [Palantir Docker Gradle plugin](https://github.com/palantir/gradle-docker).
All configuration can be found in the [Gradle build file](build.gradle) file 
and is recommended to be run with the docker/dockerTagsPush task.

## Dockerhub
Repository: [tlvlp/iot-portal](https://cloud.docker.com/repository/docker/tlvlp/iot-portal)

## Deployment
- This service is currently designed as **stateless** and can have an arbitrary number of instances running per Docker Swarm Stack.
- For settings and deployment details see the project's [deployment repository](https://github.com/tlvlp/iot-server-deployment)
