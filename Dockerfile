FROM docker.io/openjdk:11.0-slim as builder
VOLUME /tmp
WORKDIR /application
ARG JAR_FILE=target/ems-backend-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM docker.io/openjdk:11.0-slim
VOLUME /tmp
WORKDIR /application

COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]