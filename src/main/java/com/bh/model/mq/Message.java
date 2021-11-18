package com.bh.model.mq;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Message {

  @JsonProperty("@timestamp")
  private String timestamp;
  private Host host;

  private Log log;

  private String message;

  private Map<String, String> fields;

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public Host getHost() {
    return host;
  }

  public void setHost(Host host) {
    this.host = host;
  }

  public Log getLog() {
    return log;
  }

  public void setLog(Log log) {
    this.log = log;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Map<String, String> getFields() {
    return fields;
  }

  public void setFields(Map<String, String> fields) {
    this.fields = fields;
  }
}
