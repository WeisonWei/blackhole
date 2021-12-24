package com.bh.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * ConsumerConfig简介
 * 所有要采集的kfk
 * @author lihanran
 * @date 2021-12-21 10:54
 */
@Component
public class ConsumerConfig {

  @Value("${log.consumers.kafka:}")
  private String kafka;

  public String getKafkaConsumers() {
    return kafka;
  }

}
