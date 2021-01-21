# IoT Portal

## LEGACY REPOSITORY!
This repository is no longer maintained. See the [project summary page](https://github.com/tlvlp/iot-project-summary) for details and updates.

## Service
Part of the [tlvlp IoT project](https://github.com/tlvlp/iot-project-summary)'s server side microservices.

This Dockerized SpringBoot-based service is responsible providing providing an interactive portal for the API Gateway:
- Unit monitoring, reporting and interaction
- User management

## Security
- Secured with TLS towards the end users
- Uses HTTP over an encrypted docker network to access the API gateway
- Does not access the DB directly
- Uses the API gateway's authentication

## Building and publishing JAR + Docker image
This project is using the Maven dockerfile plugin.
All configuration can be found in the [Maven pom.xml](pom.xml) file 
with separate profiles for production and development.

Example scripts to build the project:
- [build-dev.sh](build-dev.sh) build with dev parameters and requires a separate lancher
- [build-prod.sh](build-prod.sh) builds and publishes it to the docker repository


## Dockerhub
Repository: [tlvlp/iot-portal](https://cloud.docker.com/repository/docker/tlvlp/iot-portal)

## Deployment
- This service is currently designed as **stateless** and can have an arbitrary number of instances running per Docker Swarm Stack.
- For settings and deployment details see the project's [deployment repository](https://github.com/tlvlp/iot-server-deployment)
