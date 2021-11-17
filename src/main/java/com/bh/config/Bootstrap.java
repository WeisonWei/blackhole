package com.bh.config;

import com.bh.message.converter.MessageConvert;
import com.bh.message.kafka.KfkConsumer;
import com.bh.model.Consumer;
import com.bh.service.ElasticSearchService;
import com.bh.util.JsonUtil;
import com.bh.util.SpringUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.bh.message.AbstractConsumer.SOURCE.KAFKA;
import static org.springframework.util.ObjectUtils.isEmpty;

@Configuration
public class Bootstrap {

  private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);
  protected static final Map<String, Thread> bootstrapThreads = new HashMap<>();

  @Resource
  ConsumerConfig consumerConfig;
  @Resource
  ElasticSearchService es;
  @Resource
  MessageConvert convert;

  @PostConstruct
  public void setUp() {
    String consumers = consumerConfig.getKafkaConsumers();
    if (isEmpty(consumers)) {
      return;
    }

    JsonArray confArray = JsonParser.parseString(consumers).getAsJsonArray();
    for (JsonElement confJson : confArray) {
      Object kfkConsumer = SpringUtil.getBean("KfkConsumer");
      if (kfkConsumer instanceof KfkConsumer) {
        Consumer consumer = JsonUtil.str2Bean(confJson.toString(), Consumer.class);
        boolean isError = checkConsumer(consumer);
        if (!isError) {
          logger.warn("consumer list :{}", consumer);
          continue;
        }
        Properties properties = initProperties(consumer);
        KfkConsumer kfk = new KfkConsumer(KAFKA, properties, consumer.getTopic(), es, convert);
        Thread kfkConsumerThread = new Thread(kfk);
        bootstrapThreads.put(consumer.getType(), kfkConsumerThread);
        kfkConsumerThread.start();
      }
    }
  }

  public Properties initProperties(Consumer consumer) {
    Properties props = new Properties();
    props.put("bootstrap.servers", consumer.getBrokerList());
    props.put("group.id", consumer.getGroupId());
    props.put("enable.auto.commit", "true");
    props.put("key.deserializer", StringDeserializer.class.getName());
    props.put("value.deserializer", StringDeserializer.class.getName());
    props.put("sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required " +
        "username=\"" + consumer.getUserName() + "\" password=\"" + consumer.getPassWord() + "\";");
    props.put("sasl.mechanism", "SCRAM-SHA-256");
    props.put("security.protocol", "SASL_PLAINTEXT");
    return props;
  }

  public Boolean checkConsumer(Consumer kfkSon) {
    return !isEmpty(kfkSon.getUserName()) && !isEmpty(kfkSon.getBrokerList()) &&
        !isEmpty(kfkSon.getGroupId()) && !isEmpty(kfkSon.getType()) &&
        !isEmpty(kfkSon.getTopic());
  }
}
