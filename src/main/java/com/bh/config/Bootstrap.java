package com.bh.config;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.bh.message.kafka.KfkConsumer;
import com.bh.util.SpringUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
    JsonArray jsonArray = new JsonParser().parse(kfkConf.getList()).getAsJsonArray();
    if (!ObjectUtils.isEmpty(jsonArray)) {
      for (JsonElement json : jsonArray) {
        Object consumer = SpringUtil.getBean("KfkConsumer");
        if (consumer instanceof KfkConsumer) {
          JsonObject kfkSon = json.getAsJsonObject();
          Boolean b = checkListKfk(kfkSon);
          if (!b) {
            logger.warn("kfk list :" + kfkSon.getAsString());
            continue;
          }
          ((KfkConsumer) consumer).changeConsumer(kfkSon.get("username").getAsString(), kfkSon.get("password").getAsString(), kfkSon.get("brokerList").getAsString(), kfkSon.get("groupId").getAsString(), kfkSon.get("topic").getAsString());
          Thread kfkConsumer = new Thread((KfkConsumer) consumer);
          bootstrapThreads.put(kfkSon.get("type").getAsString(), kfkConsumer);
          kfkConsumer.start();
        }
      }
    }
  }

  public Boolean checkListKfk(JsonObject kfkSon) {
    if (ObjectUtils.isEmpty(kfkSon.get("username")) || ObjectUtils.isEmpty(kfkSon.get("password")) || ObjectUtils.isEmpty(kfkSon.get("brokerList")) || ObjectUtils.isEmpty(kfkSon.get("groupId")) || ObjectUtils.isEmpty(kfkSon.get("topic"))) {
      return false;
    }
    return true;
  }
}
