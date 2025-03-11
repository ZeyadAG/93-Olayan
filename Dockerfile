FROM openjdk:25-ea-4-jdk-oraclelinux9
WORKDIR /app
COPY ./ /app
RUN ./mvnw clean package -DskipTests
RUN mkdir -p /app/data
COPY src/main/java/com/example/data/*.json /app/data/
ENV DATA_FOLDER=/app/data
EXPOSE 8080
CMD ["java", "-jar", "/app/target/mini1.jar"]
