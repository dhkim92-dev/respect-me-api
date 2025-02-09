FROM openjdk:17.0.1-jdk-slim
COPY build/libs/kr.respectme.report-api-latest.jar /app.jar
ENV SPRING_PROFILES_ACTIVE=test
CMD ["java", "-jar", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "/app.jar"]
