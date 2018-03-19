FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD tc-eureka-server-1.0-SNAPSHOT.jar app-eureka-server.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app-eureka-server.jar"]