package com.bh.model.mq;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Fields {

  @JsonProperty("pod_name")
  private String podName;
  @JsonProperty("flink_cluster_id")
  private String cluster;

  public String getPodName() {
    return podName;
  }

  public void setPodName(String podName) {
    this.podName = podName;
  }

  public String getCluster() {
    return cluster;
  }

  public void setCluster(String cluster) {
    this.cluster = cluster;
  }
}
