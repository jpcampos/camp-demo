FROM java:8-jdk-alpine

COPY target/camp-demo-0.0.1-SNAPSHOT.jar /usr/app/

WORKDIR /usr/app

RUN sh -c 'touch camp-demo-0.0.1-SNAPSHOT.jar'

ENTRYPOINT ["java","-jar","camp-demo-0.0.1-SNAPSHOT.jar"]