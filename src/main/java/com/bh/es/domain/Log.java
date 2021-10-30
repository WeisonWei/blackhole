package com.bh.es.domain;

import java.util.Map;

public class Log {
  private String cluster;
  private String service;
  private String version;
  private String ip;
  private String appId;
  private String erp;
  private String logLevel;
  private String podName;
  private String filepath;
  private String filename;
  private String timestamp;
  private Map<String, String> tags;
  private String content;
  private String additional;

  public String getCluster() {
    return cluster;
  }

  public void setCluster(String cluster) {
    this.cluster = cluster;
  }

  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public String getErp() {
    return erp;
  }

  public void setErp(String erp) {
    this.erp = erp;
  }

  public String getLogLevel() {
    return logLevel;
  }

  public void setLogLevel(String logLevel) {
    this.logLevel = logLevel;
  }

  public String getPodName() {
    return podName;
  }

  public void setPodName(String podName) {
    this.podName = podName;
  }

  public String getFilepath() {
    return filepath;
  }

  public void setFilepath(String filepath) {
    this.filepath = filepath;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public Map<String, String> getTags() {
    return tags;
  }

  public void setTags(Map<String, String> tags) {
    this.tags = tags;
  }

  public String getAdditional() {
    return additional;
  }

  public void setAdditional(String additional) {
    this.additional = additional;
  }

  @Override
  public String toString() {
    return "Log{" +
        "cluster='" + cluster + '\'' +
        ", sevice='" + service + '\'' +
        ", version='" + version + '\'' +
        ", ip='" + ip + '\'' +
        ", appID='" + appId + '\'' +
        ", erp='" + erp + '\'' +
        ", logLevel='" + logLevel + '\'' +
        ", podname='" + podName + '\'' +
        ", filepath='" + filepath + '\'' +
        ", filename='" + filename + '\'' +
        ", timestamp='" + timestamp + '\'' +
        ", content='" + content + '\'' +
        ", additional='" + additional + '\'' +
        '}';
  }
}
