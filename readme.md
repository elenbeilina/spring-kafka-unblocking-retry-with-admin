### App for adding shark to aquarium.
This app is producing  Rick and Morty quotes to the Kafka topic by rest endpoint.
If exception happened in consumer listener method - then record retries 4 times and if retries does not help -
record sends to DLQ by DltHandler.

> #### Important notes:
> - Error does not retry, only Exceptions
> - if attempts = 1 - no retries
> - if attempts = 2 - adds -retry-backoff value to the topic
> - if attempts = 3, 4, ... - just adds -retry to the topic
> - Spring Boot Admin client is not working without spring web dependency
> - In this project Spring Boot 3 is used and Java 17

---
#### Environment:
- docker env can be found in `docker-compose.yaml` file
- control-center was added to env for cluster monitoring, \
  so cluster state can be checked by `http://localhost:9021/clusters` url
- Spring Boot Admin server can be found: `http://localhost:8090`.

---
#### Test scenario:

 1. Build modules in the parent project:
    ```
    mvn clean package
    ```
 2. Build docker images:
    ```
    docker build -t consumer . -f consumer/Dockerfile
    docker build -t producer . -f producer/Dockerfile
    docker build -t spring-boot-admin . -f spring-boot-admin/Dockerfile
    ```
 3. Ran docker environment:
    ```
    docker compose up -d
    ```
 4. Add quote to the topic:
    ```
    sh producer/produce-message.sh
    ```
---
    
Result can be checked either by log in console or by viewing messages in `main` topic in control center.