package com.bh.kafka;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KafkaAuditConsumer implements InitializingBean, DisposableBean, Runnable{
  private static final Logger logger = LoggerFactory.getLogger(KafkaAuditConsumer.class);
  private static KafkaConsumer<String, String> consumer = null;

  @Value("${spring.kafka.consumer.username}")
  private String username;
  @Value("${spring.kafka.consumer.password}")
  private String password;
  @Value("${spring.kafka.consumer.brokerList}")
  private String brokerList;
  @Value("${spring.kafka.consumer.groupId}")
  private String groupId;
  @Value("${spring.kafka.consumer.topic}")
  private String topic;

  public void doConsumer(){

    logger.info("Initialization logKafkaConsumer......");
    Properties props = initPropertiesConfig();

    consumer = new KafkaConsumer<String, String>(props);
    consumer.subscribe(Arrays.asList(topic));

    while (!Thread.currentThread().isInterrupted()) {
      try {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(50000));
        for (ConsumerRecord<String, String> record : records) {
          logger.info("value = {}, offset = {}", record.value(), record.offset());
        }

      } catch (Exception e) {
        e.printStackTrace();
        logger.error(e.getMessage());
      }
    }

  }

  private Properties initPropertiesConfig() {
    Properties props = new Properties();
    props.put("bootstrap.servers", brokerList);
    props.put("group.id", groupId);
    props.put("enable.auto.commit", "true");
    props.put("key.deserializer", StringDeserializer.class.getName());
    props.put("value.deserializer", StringDeserializer.class.getName());

    //下面三个参数是关于认证的核心参数
    props.put("sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"" + username + "\" password=\"" + password + "\";");
    props.put("sasl.mechanism", "SCRAM-SHA-256");
    props.put("security.protocol", "SASL_PLAINTEXT");
    return props;
  }

  @Override
  public void run() {
    logger.info("KafkaAuditConsumer Call run......"+this);
    this.doConsumer();
  }

  @Override
  public void destroy() throws Exception {
    if (consumer != null) {
      consumer.close();
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {

  }
}
