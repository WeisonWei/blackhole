package com.bh.exception;


public enum ExceptionCode {

  CONVERT_ERROR(10000, "Convert error"),
  CONNECTION_ERROR(10001, "Connection error"),

  //default
  DEFAULT(9999, "Internal System Error");

  private int code;
  private String msg;

  ExceptionCode(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public int getCode() {
    return code;
  }

  public String getMsg() {
    return msg;
  }
}
