## docker build -t emsbackend ./
## docker run -dp 3000:3000 -it -rm --entrypoint /bin/sh --name emsbackendcontainer emsbackend
## docker exec -it emsbackendcontainer /bin/bash
##Stage 1: Build jar
FROM openjdk:11 AS build
WORKDIR /build
ENTRYPOINT /bin/bash
COPY ./ build/
RUN cd build && ./mvnw clean package

##Stage 2: Run jar file
FROM openjdk:11
COPY --from=build /build/targer/ems-backend-0.0.1-SNAPSHOT.jar /app/ems-backend-0.0.1-SNAPSHOT.jar
CMD ["java","-jar","/app/ems-backend-0.0.1-SNAPSHOT.jar"]

