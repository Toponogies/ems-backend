FROM docker.io/maven:3.8.5-openjdk-11-slim as builder
VOLUME /tmp
WORKDIR /application

COPY pom.xml .
COPY src ./src

RUN --mount=type=cache,target=/root/.m2 mvn clean package -Dmaven.test.skip

FROM docker.io/openjdk:11.0-slim as extracter
VOLUME /tmp
WORKDIR /application

COPY --from=builder /application/target/ems-backend-0.0.1-SNAPSHOT.jar application.jar

RUN java -Djarmode=layertools -jar application.jar extract

FROM docker.io/openjdk:11.0-slim
VOLUME /tmp
WORKDIR /application

COPY --from=extracter application/dependencies/ ./
COPY --from=extracter application/spring-boot-loader/ ./
COPY --from=extracter application/snapshot-dependencies/ ./
COPY --from=extracter application/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]