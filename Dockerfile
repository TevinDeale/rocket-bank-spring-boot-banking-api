FROM alpine:3.19.1
RUN apk update
RUN apk add openjdk21-jre
EXPOSE 8080
ARG JAR_FILE=target/RocketBank-*.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT [ "java", "-jar", "app.jar" ]