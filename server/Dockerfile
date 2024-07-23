FROM amazoncorretto:17-alpine-jdk
COPY target/*.jar shareit-server-app.jar
ENTRYPOINT ["java","-jar","/shareit-server-app.jar"]