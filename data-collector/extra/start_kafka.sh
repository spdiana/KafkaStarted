#!/usr/bin/env bash

set -eu
docker rm -f kfk1 || true
docker rm -f kfk2 || true
docker network rm data_colletor_net || true


docker network create -d bridge --subnet 172.26.0.0/16 data_colletor_net || true


docker run -d -p 2181:2181 --name kfk1 --network data_colletor_net wurstmeister/zookeeper:latest
docker run -d -p 9092:9092 --name kfk2 --network data_colletor_net -e KAFKA_ADVERTISED_HOST_NAME=localhost -e KAFKA_ZOOKEEPER_CONNECT=kfk1:2181 wurstmeister/kafka:2.12-2.2.1

echo "Creating topic..."
sleep 5;
wait;
docker exec -ti kfk2 /opt/kafka/bin/kafka-topics.sh --create --if-not-exists --zookeeper kfk1:2181 --replication-factor 1 --partitions 1 --topic demo-data-topic

sleep 5;
echo "List topics"
docker exec -ti kfk2 /opt/kafka/bin/kafka-topics.sh --list --zookeeper kfk1:2181
