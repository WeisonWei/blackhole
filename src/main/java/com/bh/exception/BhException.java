package com.bh.exception;

public class BhException extends Exception {

  private static final long serialVersionUID = 1L;

  private int code;

  public BhException() {
    super();
  }

  public BhException(String message) {
    super(message);
  }

  public BhException(int code, String message) {
    super(message);
    this.code = code;
  }

  public BhException(Throwable cause) {
    super(cause);
  }

  public BhException(String message, Throwable cause) {
    super(message, cause);
  }

}
