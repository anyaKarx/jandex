FROM adoptopenjdk/openjdk11:ubi

VOLUME /tmp

EXPOSE 8080

ARG JAR_FILE=build/libs/jandex.jar
COPY ${JAR_FILE} JandexApplication.jar

ENTRYPOINT ["java", "-jar", "/JandexApplication.jar"]

