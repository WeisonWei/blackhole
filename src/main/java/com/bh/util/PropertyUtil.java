package com.bh.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static com.bh.constant.Constant.ES_LOG_INDEX_FIELDS;
import static com.bh.constant.Constant.SERVICE_LOG_FIELDS;

public class PropertyUtil {
  private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);

  private static File file;
  private static Properties properties;
  private static final String propertiesUrl = SERVICE_LOG_FIELDS;

  public static String ES_FIELDS;

  static {
    ClassPathResource classPathResource = new ClassPathResource(propertiesUrl);
    try {
      file = classPathResource.getFile();
      ES_FIELDS = getProperty(ES_LOG_INDEX_FIELDS);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void load() throws IOException {
    if (file.exists()) {
      properties = new Properties();
      try (FileInputStream fis = new FileInputStream(file)) {
        properties.load(fis);
      }
    }
  }

  public static Boolean optionProperty(String key, String value) {
    try (FileOutputStream fos = new FileOutputStream(file)) {
      if (properties == null) {
        load();
      }
      properties.setProperty(key, value);
      properties.store(fos, "Copyright (TM) universe.io");
    } catch (IOException e) {
      return false;
    }
    return true;
  }

  public static Boolean updateProperty(String key, String value) {
    if (getProperty(key) == null) {
      return false;
    }
    return optionProperty(key, value);
  }

  public static Boolean addProperty(String key, String value) {
    if (getProperty(key) != null) {
      return false;
    }
    return optionProperty(key, value);
  }

  public static String getProperty(String key) {
    try (FileInputStream fis = new FileInputStream(file)) {
      if (properties == null) {
        load();
      }
      properties.load(fis);
    } catch (IOException e) {
      return null;
    }
    return properties.getProperty(key);
  }
}
