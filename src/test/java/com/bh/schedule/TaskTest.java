package com.bh.schedule;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.bh.message.converter.DefaultMessageConvert.INDEXES;

class TaskTest {

  @Test
  void executeFileDownLoadTask() {
    for (int i = 0; i < 20; i++) {
      INDEXES.add(LocalDate.now().plusDays(i) + "_" + i);
    }

    for (String index : INDEXES) {
      if (index.contains("_")) {
        String date = index.substring(0, index.indexOf("_"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate logDate = LocalDate.parse(date, formatter);
        LocalDate threeDaysAgo = LocalDate.now().minusDays(3L);
        Assertions.assertTrue(threeDaysAgo.isBefore(logDate));
      }
    }
  }
}
