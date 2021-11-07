package com.bh.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConsumerConfig {

  @Value("${log.consumers.kafka:}")
  private String kafka;

  public String getKafkaConsumers() {
    return kafka;
  }

}
