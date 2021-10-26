package com.bh.config;

import com.bh.message.kafka.KfkConsumer;
import com.bh.util.ClassUtil;
import com.bh.util.SpringUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

//@ConditionalOnClass(ClassUtil.class)
@Configuration
public class Bootstrap {

  public static final Map<String, Thread> bootstrapThreads = new HashMap<>();

  @Resource
  SpringUtil springUtil;

  @Resource
  private KfkConsumer kfkConsumer;

  @PostConstruct
  public void setUp() {
    Object consumer = SpringUtil.getBean("KfkConsumer");
    if (consumer instanceof KfkConsumer) {
      Thread kfkConsumer = new Thread((KfkConsumer) consumer);
      bootstrapThreads.put("kfkConsumer", kfkConsumer);
      kfkConsumer.start();
    }
  }
}
