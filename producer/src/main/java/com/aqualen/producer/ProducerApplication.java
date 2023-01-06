package com.aqualen.producer;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableKafka
@SpringBootApplication
public class ProducerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProducerApplication.class, args);
  }

  @Slf4j
  @Component
  @RequiredArgsConstructor
  public static class Producer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void produce() {
      var rickAndMortyQuote = new Faker().rickAndMorty().quote();

      kafkaTemplate.send("main", rickAndMortyQuote);
      log.info("Sending record with value {} to topic {}.", rickAndMortyQuote, "main");
    }
  }

  @RestController
  @RequestMapping("produce")
  @RequiredArgsConstructor
  public static class Controller {

    private final Producer producer;

    @PutMapping
    public void produce() {
      producer.produce();
    }
  }
}
