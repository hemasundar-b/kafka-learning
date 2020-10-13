package com.learning.kafka;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class ConsumerThread implements Runnable {

	private String topics;
	private KafkaConsumer<String, String> consumer;
	private Properties applicationProps;
	private int batchSize;
	private int pollDuration;
	private ArrayList<String> outputContainer;
	private StringBuilder sb;

	public ConsumerThread(Properties kafkaProps, Properties applicationProps) {
		this.topics = applicationProps.getProperty("topics");
		this.consumer = new KafkaConsumer<String, String>(kafkaProps);
		this.consumer.subscribe(Collections.singleton(this.topics), new RebalanceListener());
		this.applicationProps = applicationProps;	
		this.batchSize = Integer.parseInt(applicationProps.getProperty("batch.size"));
		this.pollDuration = Integer.parseInt(applicationProps.getProperty("poll.duration"));
		this.outputContainer = new ArrayList<String>();
		this.sb = new StringBuilder();
	}

	private void doCommit() {
		try {
			consumer.commitSync();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			while (true) {
				ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(pollDuration));
				for (ConsumerRecord<String, String> record : records) {
					outputContainer.add(record.value().toString());
					if (outputContainer.size() >= batchSize) {
						sb.append(String.join("\n", outputContainer));
						try {
							IOUtils.writeToFile(applicationProps, sb.toString());
						} catch (Exception e) {
							e.printStackTrace();
						}
						doCommit();
						sb = null;
						outputContainer.clear();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			consumer.close();
		}
	}
}
