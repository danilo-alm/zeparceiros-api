FROM eclipse-temurin:21
RUN addgroup --system spring && adduser --system --group spring
USER spring:spring
WORKDIR /app
COPY target/ze-parceiros-0.0.1-SNAPSHOT.jar ./ze-parceiros-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "./ze-parceiros-0.0.1-SNAPSHOT.jar"]

