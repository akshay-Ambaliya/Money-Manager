
# =================================================================================
#         PART 1 : Maven + JDK + source code
# =================================================================================


# Stage 1 : Build the Application using official Maven + JDK image
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

# Stage 2 : Set the working directory
WORKDIR /app

# Stage 3 : Copy Maven configuration first
COPY pom.xml .

# Stage 4 : Download Dependencies
RUN mvn dependency:go-offline

# Stage 5 : Copy the Source code
COPY src ./src

# Stage 6 : Build the Spring Boot jar
RUN mvn clean package -DskipTests



# =========================================================================================
#         PART 2 : Java Runtime + JAR
# =========================================================================================
# Stage 7 : Run the application
FROM eclipse-temurin:21-jre-alpine


# Stage 8 : Set the Working Directory
WORKDIR /app

# Stage 9 : Copy the JAR from the build stage ("builder")
COPY --from=builder /app/target/*.jar app.jar

# Stage 10 : Tell Docker that the application uses port 8081
EXPOSE 8081

# Stage 11 : Start the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]


