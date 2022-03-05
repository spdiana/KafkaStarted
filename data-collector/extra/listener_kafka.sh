#!/usr/bin/env bash

docker exec -ti kfk2 \
  kafka-console-consumer.sh \
    --bootstrap-server :9092 \
    --topic demo-data-topic