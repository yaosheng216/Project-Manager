FROM registry-vpc.cn-beijing.aliyuncs.com/pcss-cloud/openjdk:8-jdk-alpine
COPY target/*-SNAPSHOT.jar /opt/app.jar
ADD target/lib     /opt/lib
EXPOSE 8181
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/opt/app.jar"]
