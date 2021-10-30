package com.bh.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
  private static final Logger log = LoggerFactory.getLogger(DateUtil.class);

  static SimpleDateFormat logformat_z = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
  static SimpleDateFormat logFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  static final String utc_format = "yyyy-MM-dd'T'HH:mm:ss.SSS";

  public static String formatLogTime(Date date) {
    String time = logformat_z.format(new Date());
    time = replaceTimeStrForT(time);
    return time;
  }

  public static String formatLastHourLogTime(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.set(Calendar.HOUR_OF_DAY, (c.get(Calendar.HOUR_OF_DAY) - 1));
    String time = logformat_z.format(c.getTime());
    time = replaceTimeStrForT(time);
    return time;
  }

  public static long formatUTCToTimestamp(String time) {
    Calendar c = Calendar.getInstance();
    long timestamp = c.getTime().getTime();
    try {
      if (!StringUtils.isEmpty(time)) {
        time = time.replaceAll("\\s+", "Z");
        time = time.substring(0, time.lastIndexOf(".") + 4);
        log.debug(time);
      }
      DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(utc_format);
      LocalDateTime localDateTime = LocalDateTime.parse(time, dateTimeFormatter);
      Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
      c.setTime(date);
      c.set(Calendar.HOUR, c.get(Calendar.HOUR) + 8);
      date = c.getTime();
      timestamp = date.getTime();
    } catch (Exception e) {
      log.error(time, e);
    }
    return timestamp;
  }

  private static String replaceTimeStrForT(String time) {
    return time = time.replaceAll("\\s+", "T");
  }

  public static void main(String args[]) {
    System.out.println(formatUTCToTimestamp("2020-06-09T00:58:49.588927443Z"));
  }
}
