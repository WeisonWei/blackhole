package com.bh.util;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class JsonUtil {

  private static final ObjectMapper objectMapper;

  static {
    objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public static String toJson(Object object) throws IOException {
    return objectMapper.writeValueAsString(object);
  }

  public static String safeToJson(Object object) {
    try {
      return toJson(object);
    } catch (IOException ignore) {
      throw new RuntimeException(ignore);
    }
  }

  public static <T> T read(InputStream in, Class<T> type) throws IOException {
    return objectMapper.readValue(in, type);
  }

  public static <T> T str2Bean(String json, Class<T> type) {
    try {
      return objectMapper.readValue(json.getBytes(Charset.defaultCharset()), type);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> List<T> str2List(String json, Class<T> clazz) {
    try {
      return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static Map str2Map(String json) {
    try {
      return objectMapper.readValue(json, Map.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static Object map2Bean(Map map, Class clazz) throws Exception {
    try {
      return objectMapper.readValue(toJson(map), clazz);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static ObjectNode createObjectNode() {
    return objectMapper.createObjectNode();
  }

  public static ObjectMapper getObjectMapper() {
    return objectMapper;
  }

}
