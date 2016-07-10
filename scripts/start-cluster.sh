#! /bin/bash

ps aux | grep -E "kafka.Kafka|org.apache.zookeeper.server.quorum.QuorumPeerMain" | grep -v grep | awk '{print $2}' | xargs kill -9
rm -rf /tmp/kafka-logs /tmp/zookeeper /tmp/kafka-streams

$KAFKA_HOME/bin/zookeeper-server-start.sh $KAFKA_HOME/config/zookeeper.properties > /dev/null 2>&1 &
$KAFKA_HOME/bin/kafka-server-start.sh $KAFKA_HOME/config/server.properties > /dev/null 2>&1 &
sleep 5
$KAFKA_HOME/bin/kafka-topics.sh --create --zookeeper localhost --topic achats --replication-factor 1 --partitions 8
$KAFKA_HOME/bin/kafka-topics.sh --create --zookeeper localhost --topic achats-by-product-id --replication-factor 1 --partitions 8
$KAFKA_HOME/bin/kafka-topics.sh --create --zookeeper localhost --topic achats-enrichis --replication-factor 1 --partitions 8
$KAFKA_HOME/bin/kafka-topics.sh --create --zookeeper localhost --topic referentiel --replication-factor 1 --partitions 8 --config cleanup.policy=compact
