package com.bh.config;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.bh.domain.Topics;
import com.bh.message.kafka.KfkConsumer;
import com.bh.util.JsonUtil;
import com.bh.util.SpringUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

//@ConditionalOnClass(ClassUtil.class)
@Configuration
public class Bootstrap {
  private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);
  public static final Map<String, Thread> bootstrapThreads = new HashMap<>();

  @Resource
  SpringUtil springUtil;

  @Autowired
  KfkConf kfkConf;

  @PostConstruct
  public void setUp() {
    JsonArray jsonArray = JsonParser.parseString(kfkConf.getTopics()).getAsJsonArray();
    if (!ObjectUtils.isEmpty(jsonArray)) {
      for (JsonElement json : jsonArray) {
        Object consumer = SpringUtil.getBean("KfkConsumer");
        if (consumer instanceof KfkConsumer) {
          Topics kfkSon = JsonUtil.str2Bean(json.toString(), Topics.class);
          Boolean b = checkListKfk(kfkSon);
          if (!b) {
            logger.warn("kfk list :" + kfkSon.toString());
            continue;
          }
          ((KfkConsumer) consumer).changeConsumer(kfkSon.getUserName(), kfkSon.getPassWord(), kfkSon.getBrokerList(),
              kfkSon.getGroupId(), kfkSon.getTopic());
          Thread kfkConsumer = new Thread((KfkConsumer) consumer);
          bootstrapThreads.put(kfkSon.getType(), kfkConsumer);
          kfkConsumer.start();
        }
      }
    }
  }

  public Boolean checkListKfk(Topics kfkSon) {
    if (ObjectUtils.isEmpty(kfkSon.getUserName()) || ObjectUtils.isEmpty(kfkSon.getBrokerList()) ||
        ObjectUtils.isEmpty(kfkSon.getGroupId()) || ObjectUtils.isEmpty(kfkSon.getType()) || ObjectUtils.isEmpty(kfkSon.getTopic())) {
      return false;
    }
    return true;
  }
}
