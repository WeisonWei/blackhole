package com.bh.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
public class PropertyUtilTest {
  private static final Logger logger = LoggerFactory.getLogger(PropertyUtilTest.class);

  @Test
  public void readProperties() throws Exception {
    String property = PropertyUtil.getProperty("es-index");
    logger.info("es-index-fields--->", property);
    Assertions.assertEquals(property, "cluster,service,version,ip,appId,erp,logLevel,podName,filepath,filename,timestamp;");
  }

  @Test
  public void updateProperties() throws Exception {
    PropertyUtil.addProperty("es", "123");
    PropertyUtil.updateProperty("es", "456");
    String property = PropertyUtil.getProperty("es");
    logger.info("es-index-fields--->", property);
    Assertions.assertEquals(property, "456");
  }

}
