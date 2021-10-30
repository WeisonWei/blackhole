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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

//@ConditionalOnClass(ClassUtil.class)
@Configuration
public class Bootstrap {

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
          ((KfkConsumer) consumer).changeConsumer(kfkSon.get("username").getAsString(), kfkSon.get("password").getAsString(), kfkSon.get("brokerList").getAsString(), kfkSon.get("groupId").getAsString(), kfkSon.get("topic").getAsString());
          Thread kfkConsumer = new Thread((KfkConsumer) consumer);
          bootstrapThreads.put(kfkSon.get("type").getAsString(), kfkConsumer);
          kfkConsumer.start();
        }
      }
    }
  }
}
