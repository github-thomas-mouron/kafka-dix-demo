#! /bin/bash

echo '1,{"id":1, "name":"produit1"}
2,{"id":2, "name":"produit2"}
3,{"id":3, "name":"produit3"}' | $KAFKA_HOME/bin/kafka-console-producer.sh --broker-list localhost:9092 --topic referentiel --property parse.key=true --property key.separator=,

