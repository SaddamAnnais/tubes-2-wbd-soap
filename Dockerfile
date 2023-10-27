FROM maven:3.8.3-openjdk-11
WORKDIR /app
COPY . .
EXPOSE 8001
RUN mvn clean install
ENTRYPOINT ["mvn", "tomcat7:run"]
