package com.bh.exception;

public class BHException extends Exception {

  private static final long serialVersionUID = 1L;

  private int code;

  public BHException() {
    super();
  }

  public BHException(String message) {
    super(message);
  }

  public BHException(int code, String message) {
    super(message);
    this.code = code;
  }

  public BHException(Throwable cause) {
    super(cause);
  }

  public BHException(String message, Throwable cause) {
    super(message, cause);
  }

}
