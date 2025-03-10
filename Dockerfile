FROM maven:3.8.1-jdk-11-slim as builder

# Copy local code to the container image.
WORKDIR /app
#COPY pom.xml .
#COPY src ./src
COPY dimple-companion-chain-backend-0.0.1-SNAPSHOT.jar .
# Build a release artifact.
#RUN mvn package -DskipTests

# Run the web service on container startup.
CMD ["java","-jar","/app/dimple-companion-chain-backend-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]