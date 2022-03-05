# KafkaStarted

```
data-collector - send message
Change application.yml:
server:
  port: 8087
```  

```
data-calculator - read message
Change application.yml:
server:
  port: 8088
```    

# run to start Kafka service
/data-collector/extra/start_kafka.sh

# run to start Redis service
/data-calculator/extra/start_db.sh


Endpoint: 0.0.0.0:8087/datacolletor/v1/read
