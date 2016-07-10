package com.octo.kafka.dix;

import com.octo.kafka.dix.model.ProduitBrut;
import com.octo.kafka.dix.model.ProduitEnrichi;
import com.octo.kafka.dix.model.Referentiel;
import com.octo.kafka.dix.serializer.JsonDeserializer;
import com.octo.kafka.dix.serializer.JsonSerializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by thm on 15/06/2016.
 */
public class Main {

    public static void main(String[] args) throws Exception {

        if (args.length != 2) {
            System.out.println("Usage : Main <kafka-hosts> <zk-hosts>");
            System.exit(1);
        }

        String kafka = args[0];
        String zk = args[1];

        Properties streamsConfiguration = new Properties();
        // Donne un nom à l'application. Toutes les instances de cette application pourront se partager les partitions
        // de mêmes topics grâce à cet identifiant.
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "enrichissement-achats");
        // Broker Kafka
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafka);
        // Noeud Zookeeper
        streamsConfiguration.put(StreamsConfig.ZOOKEEPER_CONNECT_CONFIG, zk);

        KStreamBuilder builder = new KStreamBuilder();

        // Initialisation des ser/déserialiseurs pour lire et écrire dans les topics
        Serde<ProduitBrut> achatBrutSerde = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(ProduitBrut.class));
        Serde<ProduitEnrichi> achatEnrichiSerde = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(ProduitEnrichi.class));
        Serde<Referentiel> referentielSerde = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(Referentiel.class));

        // Création d'un KStream (flux) à partir du topic "achats"
        KStream<String, ProduitBrut> achats = builder.stream(Serdes.String(), achatBrutSerde, "achats");

        // Création d'une KTable (table) à partir du topic "referentiel"
        KTable<String, Referentiel> referentiel = builder.table(Serdes.String(), referentielSerde, "referentiel");


        KStream<String, ProduitEnrichi> enriched = achats
                // Re-partitionnement du flux avec la nouvelle clé qui nous permettra de faire une jointure
                .map((k, v) -> new KeyValue<>(v.getId().toString(), v))
                // Copie du flux vers un nouveau topic avec la nouvelle clé
                .through(Serdes.String(), achatBrutSerde, "achats-by-product-id")
                // Jointure du flux d'achats avec le référentiel
                .leftJoin(referentiel, (achat, ref) -> {
                    if (ref == null) return new ProduitEnrichi(achat.getId(), "REF INCONNUE", achat.getPrice());
                    else return new ProduitEnrichi(achat.getId(), ref.getName(), achat.getPrice());
                });

        // On publie le flux dans un topic "achats-enrichis"
        enriched.to(Serdes.String(), achatEnrichiSerde, "achats-enrichis");

        // Enfin, on démarre l'application
        KafkaStreams streams = new KafkaStreams(builder, streamsConfiguration);
        streams.start();
    }

}


