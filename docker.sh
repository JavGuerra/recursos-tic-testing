#!/bin/bash
./mvnw clean package -DskipTests
docker build -t spring-thymeleaf:1.0.0 .
docker-compose down -v
docker-compose up -d
docker logs -f spring_app