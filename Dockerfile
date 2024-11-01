# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Compile the application
RUN javac -d out src/main/java/ch/heigvd/dai/*.java src/main/java/ch/heigvd/dai/commands/*.java

# Set the classpath to include the compiled classes
ENV CLASSPATH /app/out

# Start the server
CMD ["java", "-jar", "target/java-tcp-programming-1.0-SNAPSHOT.jar", "server"]

# Start the game (client processed)
CMD ["java", "-jar", "target/java-tcp-programming-1.0-SNAPSHOT.jar", "client", "--host", "localhost"]