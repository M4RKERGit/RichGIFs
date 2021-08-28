# syntax=docker/dockerfile:1
FROM openjdk:16-alpine3.13
MAINTAINER Oleg Ivolga "ivolga.oleg@yandex.ru"
COPY build/libs/richGIFs-V1.0.jar /app/richGIFs-V1.0.jar
COPY config.properties /config.properties
ENTRYPOINT ["java"]
CMD ["-jar", "/app/richGIFs-V1.0.jar"]
EXPOSE 8080
RUN apk --update --no-cache add curl
HEALTHCHECK --interval=1800s --timeout=10s CMD curl -f http://localhost:8080/api/EUR || exit 1
HEALTHCHECK --interval=1800s --timeout=10s CMD curl -f http://localhost:8080/all || exit 1
HEALTHCHECK --interval=1800s --timeout=10s CMD curl -f http://localhost:8080/gif/EUR || exit 1