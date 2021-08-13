package com.bh.kafka;


import com.alibaba.fastjson.JSON;
import com.bh.domain.Log;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component("kafkaConsumer")
public class KafkaConsumer  {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	 @KafkaListener(topics = {"${log.kafkaconsumer.topic}"})
     public void doConsumer (ConsumerRecord<?, ?> record, Acknowledgment acknowledgment, Consumer<?, ?> consumer){
		try {
			String logStr = (String) record.value();
			Log log = JSON.parseObject(logStr, Log.class);
			logger.info("Consume kafka message :{}", log);
			acknowledgment.acknowledge();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Consume kafka message error", e);
		}
     }
}
