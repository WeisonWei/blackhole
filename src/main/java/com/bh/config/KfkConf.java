package com.bh.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@ConfigurationProperties(prefix = "kafka.consumer")
@Component
public class KfkConf {
  private String list;

  public String getList() {
    return list;
  }

  public void setList(String list) {
    this.list = list;
  }
}
