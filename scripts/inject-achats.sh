#! /bin/bash

echo '{"id": 1, "price": 3.45}
{"id": 2, "price": 13.40}
{"id": 30, "price": 1.05}
{"id": 1, "price": 3.40}' | $KAFKA_HOME/bin/kafka-console-producer.sh --broker-list localhost:9092 --topic achats
