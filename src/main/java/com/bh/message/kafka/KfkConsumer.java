package com.bh.message.kafka;

import com.bh.modle.Consumer;
import com.bh.message.AbstractConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@Component("KfkConsumer")
@Scope("prototype")
public class KfkConsumer extends AbstractConsumer implements InitializingBean, DisposableBean, Runnable {
  private static final Logger logger = LoggerFactory.getLogger(KfkConsumer.class);
  private KafkaConsumer<String, String> kafkaConsumer = null;

  private String username;
  private String password;
  private String brokerList;
  private String groupId;
  private String topic;

  public void updateConsumer(Consumer consumer) {
    this.username = consumer.getUserName();
    this.password = consumer.getPassWord();
    this.brokerList = consumer.getBrokerList();
    this.groupId = consumer.getGroupId();
    this.topic = consumer.getTopic();
  }

  public KfkConsumer() {
    super(SOURCE.KAFKA);
  }

  public void doConsume() {
    logger.info("Initialization kfkConsumer......");
    Properties props = initPropertiesConfig();

    kafkaConsumer = new KafkaConsumer<>(props);
    kafkaConsumer.subscribe(Collections.singletonList(topic));

    while (!Thread.currentThread().isInterrupted()) {
      try {
        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(50000));
        for (ConsumerRecord<String, String> rec : records) {
          logger.info("value = {}, offset = {}", rec.value(), rec.offset());
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
    props.put("sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required " +
        "username=\"" + username + "\" password=\"" + password + "\";");
    props.put("sasl.mechanism", "SCRAM-SHA-256");
    props.put("security.protocol", "SASL_PLAINTEXT");
    return props;
  }

  @Override
  public void run() {
    logger.info("KafkaAuditConsumer Call run......{}", this);
    this.doConsume();
  }

  @Override
  public void destroy() throws Exception {
    if (kafkaConsumer != null) {
      kafkaConsumer.close();
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
  }
}
