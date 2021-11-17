package com.bh.message.converter;

import com.bh.es.document.Log;
import com.bh.exception.ConvertException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;

public interface MessageConvert {

  /**
   * handle all service's message conversion
   *
   * @param message
   * @return
   * @throws ConvertException
   */
  public Pair<String, List<Log>> convert(ConsumerRecord<String, String> message)
      throws ConvertException, JsonProcessingException;
}
