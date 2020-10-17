package com.learning.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class SimpleProducer {

	public static void main(String[] args) {
		
		String hostip = args[0];
		
		String topic = "rebalance-topic";
		Properties props = new Properties();
		props.put("bootstrap.servers", hostip);
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		
		Producer<String, String> producer = null;
		
		try {
			producer = new KafkaProducer<String, String>(props);
			for (int i = 0; i < 1000000; i++) {
				if (i%10000 == 0) {
					// Adding a delay of 2 seconds for every 100 records
					Thread.sleep(2000);
				}
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
