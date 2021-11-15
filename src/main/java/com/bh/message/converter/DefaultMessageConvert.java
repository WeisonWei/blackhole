package com.bh.message.converter;

import com.bh.es.document.Log;
import com.bh.exception.ConvertException;
import com.bh.util.JsonUtil;
import com.bh.util.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.bh.exception.ExceptionCode.CONVERT_ERROR;

@Component
public class DefaultMessageConvert implements MessageConvert {

  private static final Logger logger = LoggerFactory.getLogger(DefaultMessageConvert.class);

  @Override
  public Log convert(String message) throws ConvertException {
    Map<String, String> msgMap = JsonUtil.str2Map(message);
    if (msgMap.containsKey("cluster") && msgMap.containsKey("service")) {
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

      assert log != null;
      log.setAdditional(diff.toString());
      return log;
    }
    return null;
  }
}
