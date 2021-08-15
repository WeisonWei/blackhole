package com.bh;

import com.bh.kafka.KafkaAuditConsumer;
import com.bh.util.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DhApplication {

    public static void main(String[] args) {
        SpringApplication.run(DhApplication.class, args);
        new Thread((KafkaAuditConsumer) SpringContextUtil.getBean("kafkaAuditConsumer")).start();
    }
}