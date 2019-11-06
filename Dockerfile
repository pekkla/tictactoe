FROM maven:3.6-jdk-11
ADD . /app

WORKDIR /app
RUN mvn clean install -DskipTests=true

FROM openjdk:11-slim-stretch
WORKDIR /app
ADD entry.sh .
COPY --from=0 /app/target/tic-tac-toe.jar .
RUN /bin/chmod +x ./entry.sh
RUN useradd -r -U tic-tac-toe-user
USER tic-tac-toe-user
EXPOSE 8089

ENTRYPOINT ./entry.sh
