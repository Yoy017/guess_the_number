# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:21-jre

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container at /app
COPY target/java-tcp-programming-1.0-SNAPSHOT.jar /app/java-tcp-programming-1.0-SNAPSHOT.jar

# Expose the port 6433
EXPOSE 6433

ENTRYPOINT ["java", "-jar", "/app/java-tcp-programming-1.0-SNAPSHOT.jar"]
CMD ["server"]