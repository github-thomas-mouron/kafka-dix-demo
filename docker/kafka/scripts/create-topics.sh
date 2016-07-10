#! /bin/bash

$KAFKA_HOME/bin/kafka-topics.sh --create --zookeeper zk --topic achats --replication-factor 1 --partitions 8
$KAFKA_HOME/bin/kafka-topics.sh --create --zookeeper zk --topic achats-by-product-id --replication-factor 1 --partitions 8
$KAFKA_HOME/bin/kafka-topics.sh --create --zookeeper zk --topic achats-enrichis --replication-factor 1 --partitions 8
$KAFKA_HOME/bin/kafka-topics.sh --create --zookeeper zk --topic referentiel --replication-factor 1 --partitions 8 --config cleanup.policy=compact
