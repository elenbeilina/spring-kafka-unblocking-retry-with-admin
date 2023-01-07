package com.aqualen.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.EnableKafkaRetryTopic;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.FixedDelayStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.Random;

@EnableKafkaRetryTopic
@SpringBootApplication
public class ConsumerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ConsumerApplication.class, args);
  }

  @Bean
  Random random() {
    return new Random();
  }

  @Bean
  public TaskScheduler taskScheduler() {
    return new ThreadPoolTaskScheduler();
  }

  @Slf4j
  @Component
  @RequiredArgsConstructor
  public static class Consumer {

    private final Random random;

    @KafkaListener(id = "students_consumer", topics = "main")
    @RetryableTopic(fixedDelayTopicStrategy = FixedDelayStrategy.SINGLE_TOPIC,
        backoff = @Backoff(4000), attempts = "4")
    public void listen(String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String receivedTopic) {
      randomlyThrowException();
      log.info("Message {} received in topic {} ", message, receivedTopic);
    }

    @DltHandler
    public void dltHandler(String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String receivedTopic) {
      log.info("Message: {} received in dlt handler at topic: {} ", message, receivedTopic);
    }

    protected void randomlyThrowException() {
      if (random.nextBoolean()) {
        throw new IllegalArgumentException("Send to DLQ topic!");
      }
    }
  }
}
