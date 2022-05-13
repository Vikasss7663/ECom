FROM openjdk:11
VOLUME /app
ADD build/libs/product-0.0.1-SNAPSHOT.jar app.jar
COPY entrypoint.sh /
ENTRYPOINT ["/entrypoint.sh"]