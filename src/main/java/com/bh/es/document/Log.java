package com.bh.es.document;

import com.bh.model.mq.Host;
import com.bh.model.mq.Message;
import com.bh.util.DateUtil;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;

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
  private String content;
  private String machineRoom;
  private String writeTimestamp;
  private String processName;
  private String type;
  private String additional;
  private Map<String, String> tags;

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

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
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

  public String getMachineRoom() {
    return machineRoom;
  }

  public void setMachineRoom(String machineRoom) {
    this.machineRoom = machineRoom;
  }

  public String getWriteTimestamp() {
    return writeTimestamp;
  }

  public void setWriteTimestamp(String writeTimestamp) {
    this.writeTimestamp = writeTimestamp;
  }

  public String getProcessName() {
    return processName;
  }

  public void setProcessName(String processName) {
    this.processName = processName;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getAdditional() {
    return additional;
  }

  public void setAdditional(String additional) {
    this.additional = additional;
  }

  public Map<String, String> getTags() {
    return tags;
  }

  public void setTags(Map<String, String> tags) {
    this.tags = tags;
  }

  public static Log mapTo(Message message) {
    Log log = new Log();
    log.setContent(message.getMessage());
    String timestamp = message.getTimestamp();
    if (!isBlank(timestamp) && timestamp.contains("Z")) {
      timestamp = String.valueOf(DateUtil.formatUTCToTimestamp(timestamp));
    }
    log.setTimestamp(timestamp);
    if (message.getHost() != null) {
      Host host = message.getHost();
      if (host.getIp() != null && !host.getIp().isEmpty()) {
        log.setIp(host.getIp().get(0));
      }
    }
    if (message.getFields() != null && !message.getFields().isEmpty()) {
      log.setTags(message.getFields());
    }

    if (message.getLog() != null && message.getLog().getFile() != null) {
      log.setFilepath(message.getLog().getFile().getPath());
    }
    return log;
  }

  @Override
  public String toString() {
    return "Log{" +
        "cluster='" + cluster + '\'' +
        ", service='" + service + '\'' +
        ", version='" + version + '\'' +
        ", ip='" + ip + '\'' +
        ", appId='" + appId + '\'' +
        ", erp='" + erp + '\'' +
        ", logLevel='" + logLevel + '\'' +
        ", podName='" + podName + '\'' +
        ", filepath='" + filepath + '\'' +
        ", filename='" + filename + '\'' +
        ", timestamp='" + timestamp + '\'' +
        ", content='" + content + '\'' +
        ", machineRoom='" + machineRoom + '\'' +
        ", writeTimestamp='" + writeTimestamp + '\'' +
        ", processName='" + processName + '\'' +
        ", type='" + type + '\'' +
        ", additional='" + additional + '\'' +
        ", tags=" + tags +
        '}';
  }
}
