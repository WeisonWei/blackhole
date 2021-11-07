package com.bh.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Objects;

@Configuration
public class ElasticSearchConfig {
  private static final Logger logger = LoggerFactory.getLogger(ElasticSearchConfig.class);

  public static int MAX_CONN_TOTAL = 30;
  public static int MAX_CONN_PER_ROUTE = 10;
  public static int CONNECT_TIMEOUT_MILLIS = 1000;
  public static int SOCKET_TIMEOUT_MILLIS = 30000;
  public static int CONNECTION_REQUEST_TIMEOUT_MILLIS = 500;

  private static final int ADDRESS_LENGTH = 2;
  private static final String HTTP_SCHEME = "http";

  final CredentialsProvider cp = new BasicCredentialsProvider();

  //ip:port
  @Value("${elasticsearch.host}")
  private String[] address;

  //@Value("${elasticsearch.username}")
  //private String username;

  //@Value("${elasticsearch.password}")
  //private String password;

  @Bean
  public RestClientBuilder restClientBuilder() {
    HttpHost[] hosts = Arrays.stream(address)
        .map(this::makeHttpHost)
        .filter(Objects::nonNull)
        .toArray(HttpHost[]::new);
    logger.info("hosts:{}", Arrays.toString(hosts));

    //permission
    //credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
    return RestClient.builder(hosts)
        .setHttpClientConfigCallback(clientBuilder -> clientBuilder.setDefaultCredentialsProvider(cp));
  }

  @Bean(name = "highLevelClient")
  public RestHighLevelClient highLevelClient(@Autowired RestClientBuilder restClientBuilder) {
    setConnectTimeOutConfig(restClientBuilder);
    setConnectConfig(restClientBuilder);
    return new RestHighLevelClient(restClientBuilder);
  }

  private HttpHost makeHttpHost(String s) {
    assert StringUtils.isNotEmpty(s);
    String[] address = s.split(":");
    if (address.length == ADDRESS_LENGTH) {
      String ip = address[0];
      int port = Integer.parseInt(address[1]);
      return new HttpHost(ip, port, HTTP_SCHEME);
    }
    return null;
  }

  public void setConnectTimeOutConfig(RestClientBuilder builder) {
    builder.setRequestConfigCallback(requestConfigBuilder -> {
      requestConfigBuilder.setConnectTimeout(CONNECT_TIMEOUT_MILLIS);
      requestConfigBuilder.setSocketTimeout(SOCKET_TIMEOUT_MILLIS);
      requestConfigBuilder.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT_MILLIS);
      return requestConfigBuilder;
    });
  }

  public void setConnectConfig(RestClientBuilder builder) {
    builder.setHttpClientConfigCallback(httpClientBuilder -> {
      httpClientBuilder.setMaxConnTotal(MAX_CONN_TOTAL);
      httpClientBuilder.setMaxConnPerRoute(MAX_CONN_PER_ROUTE);
      return httpClientBuilder;
    });
  }
}
