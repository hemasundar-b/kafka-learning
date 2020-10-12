package com.learning.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class SimpleProduer {

	public static void main(String[] args) {
		
		String topic = "learning";
		Properties props = new Properties();
		props.put("bootstrap.servers", "192.168.29.151:9092");
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		
		Producer<String, String> producer = null;
		
		try {
			producer = new KafkaProducer<String, String>(props);
			for (int i = 0; i <= 100; i++) {
				ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, Integer.valueOf(i).toString());
				producer.send(record);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			producer.close();
		}
		
		
	}

}
