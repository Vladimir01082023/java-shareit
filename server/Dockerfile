FROM amazoncorretto:17-alpine-jdk
#ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:9091
COPY target/*.jar shareit-server-app.jar
ENTRYPOINT ["java","-jar","/shareit-server-app.jar"]