FROM adoptopenjdk/openjdk11:ubi

VOLUME /tmp

EXPOSE 8080

COPY build/libs/jandex.jar /opt/application.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]

