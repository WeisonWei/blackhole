package com.bh.message;

public abstract class AbstractConsumer {
  private SOURCE source;

  public AbstractConsumer(SOURCE source) {
    this.source = source;
  }

  public abstract void doConsume();

  public enum SOURCE {
    KAFKA, ROCKET_MQ
  }
}
