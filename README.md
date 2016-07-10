#Démo KStreams

Deux modes d'installation de Zookeeper, Kafka et de l'application sont possibles : avec ou sans Docker.

Pré-requis : Git et Maven.
    
###Commun aux deux modes

Cloner le projet git :

    git clone https://github.com/tmouron/kafka-dix-demo.git


##Démo sans Docker

Pré-requis : java 8.

###Démo

Télécharger et décomprésser Kafka 10 :

    wget http://www-eu.apache.org/dist/kafka/0.10.0.0/kafka_2.11-0.10.0.0.tgz
    tar -xvf kafka_2.11-0.10.0.0.tgz
    export KAFKA_HOME=$(pwd)/kafka_2.11-0.10.0.0
    
Démarrer le cluster :

    cd kafka-dix-demo
    scripts/start-cluster.sh
    
    
Builder le projet :

    mvn package
    
Packager et lancer l'application KStreams :
    
    java -jar target/kafka-dix-1.0-jar-with-dependencies.jar localhost:9092 localhost:2181 > /dev/null 2>&1 &
    
    
####Référentiel

Injecter des produits dans le topic `referentiel` :

    scripts/inject-referentiel.sh
    
Cela injecte les messages (clé,valeur) suivants dans le référentiel :

    1,{"id":1, "name":"produit1"}
    2,{"id":2, "name":"produit2"}
    3,{"id":3, "name":"produit3"}

####Achats

Injecter des produits dans le topic `achats` :

    scripts/inject-achats.sh
    
Cela injecte les messages suivants dans le flux des achats :

    {"id": 1, "price": 3.45}
    {"id": 2, "price": 13.40}
    {"id": 30, "price": 1.05}
    {"id": 1, "price": 3.40}
    
Ici la clé n'est pas présente, ce qui revient à produire le message dans une partition aléatoire.
    
####Résultat

Consommer les messages présents dans le topic `achats-enrichis` :

    scripts/consume-output.sh
    
Output :

    {"id":2,"name":"produit2","price":13.40}
    {"id":30,"name":"REF INCONNUE","price":1.05}
    {"id":1,"name":"produit1","price":3.40}
    {"id":1,"name":"produit1","price":3.45}
    
On voit que les achats ont été enrichis du libellé produit grâce au référentiel.

## Démo avec Docker

Pré-requis : Docker.

###Démo

Builder le projet :

    cd kafka-dix-demo
    mvn package
    
Lancer le cluster :

    cd docker
    cp ../target/kafka-dix-1.0-jar-with-dependencies.jar app/
    docker-compose up -d

####Référentiel

Injecter des produits dans le topic `referentiel` :

    docker exec $(docker ps | grep kafka | awk {'print $1'} | head -1) bash -c "/opt/scripts/inject-referentiel.sh"
    
Cela injecte les messages (clé,valeur) suivants dans le référentiel :

    1,{"id":1, "name":"produit1"}
    2,{"id":2, "name":"produit2"}
    3,{"id":3, "name":"produit3"}
    
####Achats

Injecter des produits dans le topic `achats` :

    docker exec $(docker ps | grep kafka | awk {'print $1'} | head -1) bash -c "/opt/scripts/inject-achats.sh"
    
Cela injecte les messages suivants dans le flux des achats :

    {"id": 1, "price": 3.45}
    {"id": 2, "price": 13.40}
    {"id": 30, "price": 1.05}
    {"id": 1, "price": 3.40}
    
Ici la clé n'est pas présente, ce qui revient à produire le message dans une partition aléatoire.
    
####Résultat

Consommer les messages présents dans le topic `achats-enrichis` :

    docker exec $(docker ps | grep kafka | awk {'print $1'} | head -1) bash -c "/opt/scripts/consume-output.sh"
    
Output :

    {"id":2,"name":"produit2","price":13.40}
    {"id":30,"name":"REF INCONNUE","price":1.05}
    {"id":1,"name":"produit1","price":3.40}
    {"id":1,"name":"produit1","price":3.45}
    
On voit que les achats ont été enrichis du libellé produit grâce au référentiel.
