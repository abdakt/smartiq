FROM postgres

COPY order.jar /order.jar

RUN apt-get update -y
RUN apt-get install -y default-jre-headless
# run the app
CMD ["/usr/bin/java", "-jar", "/order.jar"]