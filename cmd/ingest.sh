#! /bin/sh

nohup java -jar ../target/kafka-ingest.jar ../../kafka-ingest/src/main/resources/consumer.properties ../../kafka-ingest/src/main/resources/application.properties 1 &

#nohup java -jar ../target/kafka-ingest.jar ../../kafka-ingest/src/main/resources/consumer.properties ../../kafka-ingest/src/main/resources/application.properties 1 &
