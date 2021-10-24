package com.bh.exception;

public class ConvertException extends BhException {

  public ConvertException() {
    super();
  }

  public ConvertException(String message) {
    super(message);
  }

  public ConvertException(int code, String message) {
    super(code, message);
  }

  public ConvertException(Throwable cause) {
    super(cause);
  }

  public ConvertException(String message, Throwable cause) {
    super(message, cause);
  }

  public ConvertException(ExceptionCode exception) {
    super(exception.getCode(), exception.getMsg());
  }
}
