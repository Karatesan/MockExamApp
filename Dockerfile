FROM openjdk:17 as build

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn

COPY pom.xml .

COPY src src

RUN sed -i 's/\r$//' mvnw

RUN /bin/sh mvnw dependency:go-offline -B

RUN sed -i 's/\r$//' mvnw
RUN /bin/sh mvnw package
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:17

ARG DEPENDENCY=/app/target/dependency

COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

CMD ["java", "-cp", "app:app/lib/*","com.fdmgroup.MockExam.MockExamApplication"]