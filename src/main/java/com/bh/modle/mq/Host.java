package com.bh.modle.mq;

import java.util.List;

public class Host {

  private List<String> ip;

  private String hostname;

  public List<String> getIp() {
    return ip;
  }

  public void setIp(List<String> ip) {
    this.ip = ip;
  }

  public String getHostname() {
    return hostname;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }
}
