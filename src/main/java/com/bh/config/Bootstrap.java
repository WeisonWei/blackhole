package com.bh.config;

import com.bh.message.kafka.KfkConsumer;
import com.bh.modle.Consumer;
import com.bh.util.JsonUtil;
import com.bh.util.SpringUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.util.ObjectUtils.isEmpty;

@Configuration
public class Bootstrap {

  private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);
  public static final Map<String, Thread> bootstrapThreads = new HashMap<>();

  @Resource
  ConsumerConfig consumerConfig;

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
        Boolean isError = checkConsumer(consumer);
        if (!isError) {
          logger.warn("consumer list :{}", consumer);
          continue;
        }
        ((KfkConsumer) kfkConsumer).updateConsumer(consumer);
        Thread kfkConsumerThread = new Thread((KfkConsumer) kfkConsumer);
        bootstrapThreads.put(consumer.getType(), kfkConsumerThread);
        kfkConsumerThread.start();
      }
    }
  }

  public Boolean checkConsumer(Consumer kfkSon) {
    return !isEmpty(kfkSon.getUserName()) && !isEmpty(kfkSon.getBrokerList()) &&
        !isEmpty(kfkSon.getGroupId()) && !isEmpty(kfkSon.getType()) &&
        !isEmpty(kfkSon.getTopic());
  }
}
