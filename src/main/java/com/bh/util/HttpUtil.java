package com.bh.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
  private static final Logger LOG = LoggerFactory.getLogger(HttpUtil.class);
  private static final int CONNECT_TIMEOUT = 5000;
  private static final int READ_TIMEOUT = 5000;

  public static String send(String url, byte[] value) {
    URL u = null;
    HttpURLConnection con = null;
    InputStream inputStream = null;
    OutputStream outStream = null;
    String result = "";
    try {
      u = new URL(url);
      con = (HttpURLConnection) u.openConnection();
      con.setRequestMethod("POST");
      con.setDoOutput(true);
      con.setConnectTimeout(CONNECT_TIMEOUT);
      con.setReadTimeout(READ_TIMEOUT);
      con.setDoInput(true);
      con.setUseCaches(false);
      con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
      outStream = con.getOutputStream();
      outStream.write(value);
      outStream.flush();
      int responseCode = con.getResponseCode();
      inputStream = con.getInputStream();
      if (inputStream != null) {
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = in.readLine()) != null) {
          result += line;
        }
      }
      LOG.info("Response coce: {} , result msg: {}, url: {}", responseCode,
          result, url);
    } catch (Exception e) {
      LOG.error("Failed to report jobinfo to http server", e);
    } finally {
      if (outStream != null) {
        try {
          outStream.close();
        } catch (IOException e) {
          // do nothing
        }
      }
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          // do nothing
        }
      }
      if (con != null) {
        con.disconnect();
      }
    }
    return result;
  }
}
