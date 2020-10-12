package com.learning.kafka;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class ConsumerThread implements Runnable {

	private KafkaConsumer<String, String> consumer;

	public ConsumerThread(String brokers, String groupId, String topics) {
		Properties props = createConsumerConfig(brokers, groupId);
		this.consumer = new KafkaConsumer<String, String>(props);
		this.consumer.subscribe(Collections.singleton(topics), new RebalanceListener());
	}

	private static Properties createConsumerConfig(String brokers, String groupId) {
		Properties props = new Properties();
		props.put("bootstrap.servers", brokers);
		props.put("group.id", groupId);
		props.put("enable.auto.commit", "false");
		props.put("session.timeout.ms", "30000");
		props.put("auto.offset.reset", "earliest");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		return props;
	};

	private static void writeToFile() {

	}

	private static void doProcessing(ConsumerRecords records) {
	}

	public void run() {
		try {
			while (true) {
				ConsumerRecords records = consumer.poll(Duration.ofMillis(2000));

			}
		} finally {

		}

	}

}
