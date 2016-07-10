#! /bin/bash

ps aux | grep -E "kafka.Kafka|org.apache.zookeeper.server.quorum.QuorumPeerMain" | grep -v grep | awk '{print $2}' | xargs kill -9
rm -rf /tmp/kafka-logs /tmp/zookeeper /tmp/kafka-streams
