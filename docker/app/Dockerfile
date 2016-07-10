FROM java:8-jre
COPY kafka-dix-1.0-jar-with-dependencies.jar /opt/app/app.jar
CMD ["java", "-jar", "/opt/app/app.jar", "kafka:9092", "zk:2181"]
