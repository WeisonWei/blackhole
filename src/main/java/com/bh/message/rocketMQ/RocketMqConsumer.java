package com.bh.message.rocketMQ;

import com.bh.message.AbstractConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RocketMqConsumer extends AbstractConsumer implements Runnable {
  private static final Logger logger = LoggerFactory.getLogger(RocketMqConsumer.class);

  public RocketMqConsumer() {
    super(SOURCE.ROCKET_MQ);
  }

  @Override
  public void doConsumer() {
  }

  @Override
  public void run() {
  }
}
