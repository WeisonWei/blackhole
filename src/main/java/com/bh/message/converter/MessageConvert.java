package com.bh.message.converter;

import com.bh.es.document.Log;
import com.bh.exception.ConvertException;

public interface MessageConvert {

  /**
   * handle all service's message conversion
   * @param message
   * @return
   * @throws ConvertException
   */
  public Log convert (String message) throws ConvertException;

}
