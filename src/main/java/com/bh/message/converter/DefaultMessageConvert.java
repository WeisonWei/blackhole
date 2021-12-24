package com.bh.message.converter;

import com.bh.es.document.Log;
import com.bh.exception.ConvertException;
import com.bh.model.mq.Message;
import com.bh.util.JsonUtil;
import com.bh.util.PropertyUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.bh.exception.ExceptionCode.CONVERT_ERROR;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public class DefaultMessageConvert implements MessageConvert {

  private static final Logger logger = LoggerFactory.getLogger(DefaultMessageConvert.class);
  private boolean isFieldsConfigEnable = false;

  public static final Set<String> INDEXES = ConcurrentHashMap.newKeySet();


  /**
   * todo
   * @param message
   * @return
   */
  @Override
  public Pair<String, List<Log>> convert(ConsumerRecord<String, String> message)
      throws ConvertException, JsonProcessingException {
    String value = message.value();
    if (isFieldsConfigEnable || isBlank(value)) {
      return mapByConfig(value);
    } else {
      return mapByBean(value);
    }
  }

  private Pair<String, List<Log>> mapByBean(String value)
      throws JsonProcessingException {
    List<Log> logs = new ArrayList<>();
    List<Message> messages = JsonUtil.str2ListByClass(value, Message.class);
    String index = null;
    for (Message msg : messages) {
      String logContent = msg.getMessage();
      Map<String, String> fields = msg.getFields();
      String serviceName = fields.get("process_name");
      //todo spilt time is error
      if (logContent.length() >= 11) {
        String logDate = logContent.substring(0, 11);
        //TODO is index different each time?
        if (index == null) {
          index = serviceName + "_" + logDate;
        }
        if (!INDEXES.contains(index)) {
          INDEXES.add(index);
        }
      }

      if (isBlank(msg.getMessage()) || msg.getHost() == null
          || msg.getFields() == null || msg.getLog() == null) {
        //todo add log
        continue;
      }
      Log log = Log.mapTo(msg);
      logs.add(log);
    }
    return Pair.of(index, logs);
  }

  private Pair<String, List<Log>> mapByConfig(String value)
      throws ConvertException {
    List<Log> logs = new ArrayList<>();
    Map<String, String> msgMap = JsonUtil.str2Map(value);
    if (msgMap.containsKey("cluster") && value.contains("service")) {
      String cluster = msgMap.get("cluster");
      String service = msgMap.get("service");
      String fields = PropertyUtil.getProperty(cluster + "-" + service);
      String esFields = PropertyUtil.ES_FIELDS;
      String[] esFieldArr = esFields.split(",");
      String[] serviceFieldArr = fields.split(",");

      Map<String, String> diff = new HashMap<>();
      for (String sField : serviceFieldArr) {
        boolean isAllMismatch = true;
        for (String esField : esFieldArr) {
          if (esField.equals(sField)) {
            isAllMismatch = false;
          }
        }
        if (isAllMismatch) {
          diff.put(sField, msgMap.get(sField));
          msgMap.remove(sField);
        }
      }

      Log log;
      try {
        log = (Log) JsonUtil.map2Bean(msgMap, Log.class);
      } catch (Exception e) {
        logger.error("DefaultMessageConvert exception ", e);
        throw new ConvertException(CONVERT_ERROR);
      }

      log.setAdditional(diff.toString());
      logs.add(log);
      return null;
    }
    return null;
  }
}
