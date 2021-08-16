package com.bh;

import com.bh.kafka.Consumer;
import com.bh.util.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DhApplication {

    public static void main(String[] args) {
        SpringApplication.run(DhApplication.class, args);
        new Thread((Consumer) SpringContextUtil.getBean("consumer")).start();
    }
}