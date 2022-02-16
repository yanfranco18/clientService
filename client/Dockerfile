FROM openjdk:11.0-oracle
VOLUME /tmp
EXPOSE 8094
ADD ./target/client-0.0.1-SNAPSHOT.jar client-service.jar
ENTRYPOINT ["java","-jar","/client-service.jar"]