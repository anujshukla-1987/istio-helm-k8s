FROM maven:3.5-jdk-8 AS build
COPY src /usr/src/app/src  
COPY pom.xml /usr/src/app  
RUN mvn -f /usr/src/app/pom.xml clean install

FROM gcr.io/distroless/java  
COPY --from=build /usr/src/app/target/spring-boot-crud-exec.jar /usr/app/api.jar  
EXPOSE 8080  
ENTRYPOINT ["java","-jar","/usr/app/api.jar"]