package com.bh.es.document;

import java.util.List;

public class LogEntry {
  private List<Log> log;

  public List<Log> getLog() {
    return log;
  }

  public void setLog(List<Log> log) {
    this.log = log;
  }

  @Override
  public String toString() {
    return "LogEntry{" +
        "log=" + log +
        '}';
  }
}
