FROM alpine:3.12.0

RUN apk --no-cache add openjdk11

ARG JAVA_HOME=/usr/lib/jvm/java-11-openjdk

COPY . .

RUN chmod u+x mvnw && ./mvnw package

EXPOSE 8081

CMD ["java", "-Dserver.port=8081", "-Dspring.profiles.active=prod", "-jar", "target/spacecraft-0.0.1-SNAPSHOT.jar"]
#java -Dserver.port=8081 -Dspring.profiles.active=dev -jar target/spacecraft-0.0.1-SNAPSHOT.jar