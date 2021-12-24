package com.bh.message.kafka;

import com.bh.es.document.Log;
import com.bh.message.AbstractConsumer;
import com.bh.message.converter.MessageConvert;
import com.bh.service.ElasticSearchService;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class KfkConsumer extends AbstractConsumer implements InitializingBean, DisposableBean, Runnable {
  private static final Logger logger = LoggerFactory.getLogger(KfkConsumer.class);
  private KafkaConsumer<String, String> kafkaConsumer = null;

  private Properties props;
  private String topic;
  private ElasticSearchService es;
  private MessageConvert messageConvert;

  public KfkConsumer() {
    super(SOURCE.KAFKA);
  }

  public KfkConsumer(SOURCE source, Properties props,
                     String topic, ElasticSearchService es,
                     MessageConvert messageConvert) {
    super(source);
    this.kafkaConsumer = new KafkaConsumer<>(props);
    this.props = props;
    this.topic = topic;
    this.es = es;
    this.messageConvert = messageConvert;
  }

  public void doConsume() {
    logger.info("Initialization kfkConsumer......");
    kafkaConsumer.subscribe(Collections.singletonList(topic));

    while (!Thread.currentThread().isInterrupted()) {
      try {
        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(50000));
        for (ConsumerRecord<String, String> record : records) {
          String msg = record.value();
          if (isNotBlank(msg)) {
            if (msg.charAt(0) != '[') {
              logger.warn("Json syntax error,support for Array. json{}", msg);
              break;
            }
          }
          //convert
          Pair<String, List<Log>> logPair = messageConvert.convert(record);
          if (logPair == null) {
            logger.debug("message convert failed");
            return;
          }
          logger.debug("index:{} insert documents:{}", logPair.getLeft(), logPair.getRight());
          //todo move indexCache to add up ,add locked
          boolean success = es.addDocs(logPair.getLeft(), logPair.getRight());
          logger.debug("index:{} insert {} documents, result: {}", logPair.getLeft(),
              logPair.getRight().size(), success);
        }
      } catch (Exception e) {
        e.printStackTrace();
        logger.error(e.getMessage());
      }
    }
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
