package com.learning.kafka;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

public class ConsumerThread implements Runnable {

	private String topics;
	private KafkaConsumer<String, String> consumer;
	private Properties applicationProps;
	private int batchSize;
	private int pollDuration;
	private long batchDuration;
	private ArrayList<String> outputContainer;
	private long startTime;

	private class RebalanceListener implements ConsumerRebalanceListener {
		public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
			System.out.println("Rebalancing. Partitions will be revoked.");

			if (!outputContainer.isEmpty()) {
				System.out.println("Rebalance started");
				doProcessing();
				doCommit();
				startTime = System.nanoTime();
			}

		}
		
		public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
		
		}

	}

	public ConsumerThread(Properties kafkaProps, Properties applicationProps) {
		this.topics = applicationProps.getProperty("topics");
		try {
			this.consumer = new KafkaConsumer<String, String>(kafkaProps);
			System.out.println("created Consumer");
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.consumer.subscribe(Collections.singleton(this.topics), new RebalanceListener());
		this.applicationProps = applicationProps;
		this.batchSize = Integer.parseInt(applicationProps.getProperty("batch.size"));
		this.batchDuration = Long.parseLong(applicationProps.getProperty("batch.duration"));
		this.pollDuration = Integer.parseInt(applicationProps.getProperty("poll.duration"));
		this.outputContainer = new ArrayList<String>();
	}

	private void doCommit() {
		try {
			this.consumer.commitSync();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void doProcessing() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.join("\n", outputContainer));
		try {
			IOUtils.writeToFile(applicationProps, sb.toString());
			doCommit();
			outputContainer.clear();
			startTime = System.nanoTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		startTime = System.nanoTime();
		try {
			while (true) {
				// Thread.sleep(100);
				if (((System.nanoTime() - startTime) / 1000000000 >= batchDuration) && (!outputContainer.isEmpty())) {
					doProcessing();
				}
				ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(pollDuration));
				if (!records.isEmpty()) {
					System.out.println("Records fetched in the poll");
				}
				for (ConsumerRecord<String, String> record : records) {
					outputContainer.add(record.value().toString());
					if (outputContainer.size() >= batchSize) {
						System.out.println("Batch Threshold reached. Writing to Disk");
						doProcessing();
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
