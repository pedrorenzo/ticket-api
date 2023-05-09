# Start with the Java 17 image
FROM openjdk:17-oracle

# Copy jar file
COPY ticket-api-0.0.1-SNAPSHOT.jar /ticket-api.jar

# Run the app
CMD ["/usr/bin/java", "-jar", "-Dspring.profiles.active=docker", "/ticket-api.jar"]
