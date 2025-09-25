FROM openjdk:17-jdk

WORKDIR /app

ENV PATH_JAR=target/*.jar

COPY ${PATH_JAR} aplicacion.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar","aplicacion.jar"]