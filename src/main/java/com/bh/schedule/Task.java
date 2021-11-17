package com.bh.schedule;

import com.bh.service.ElasticSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.bh.message.converter.DefaultMessageConvert.INDEXES;

@Component
public class Task {
  private static final Logger logger = LoggerFactory.getLogger(Task.class);

  @Resource
  ElasticSearchService es;

  @Scheduled(cron = "0 0 0 * * ?")
  public void executeFileDownLoadTask() {
    logger.info("Scheduled task begin.");
    for (String index : INDEXES) {
      if (index.contains("_")) {
        //2021-10-29
        String date = index.substring(0, index.indexOf("_"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate logDate = LocalDate.parse(date, formatter);
        LocalDate threeDaysAgo = LocalDate.now().minusDays(3L);
        if (threeDaysAgo.isAfter(logDate)) {
          try {
            es.deleteIndex(index);
          } catch (IOException e) {
            logger.error("Index:{} delete failed,Exception:{}", index, e);
          }
        }
      }
    }
  }
}
