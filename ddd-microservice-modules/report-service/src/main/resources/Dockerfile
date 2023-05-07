FROM eclipse-temurin:11.0.19_7-jre-jammy
WORKDIR /usr/app
EXPOSE 8080
ENV JAVA_OPTS="" \
    APPLICATION="report-service"

LABEL codo-app="$APPLICATION"

VOLUME /root/.m2

COPY target/app-dist.jar /usr/app/

COPY entrypoint.sh ./
RUN chmod +x ./entrypoint.sh

CMD [ "./entrypoint.sh" ]
