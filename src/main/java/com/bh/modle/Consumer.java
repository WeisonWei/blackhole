package com.bh.modle;

public class Consumer {
  private String type;
  private String userName;
  private String passWord;
  private String brokerList;
  private String groupId;
  private String topic;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassWord() {
    return passWord;
  }

  public void setPassWord(String passWord) {
    this.passWord = passWord;
  }

  public String getBrokerList() {
    return brokerList;
  }

  public void setBrokerList(String brokerList) {
    this.brokerList = brokerList;
  }

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }
}
