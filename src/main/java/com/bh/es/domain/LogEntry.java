package com.bh.es.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class LogEntry {
  @Valid
  @NotNull(message = "log field cannot be empty!")
  @Size(min = 1, message = "Log list cannot be empty!")
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
