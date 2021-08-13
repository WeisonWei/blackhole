package com.bh.exception;

public class DhException extends Exception {

  private static final long serialVersionUID = 1L;

  public DhException() {
    super();
  }

  public DhException(String message) {
    super(message);
  }

  public DhException(Throwable cause) {
    super(cause);
  }

  public DhException(String message, Throwable cause) {
    super(message, cause);
  }

}
