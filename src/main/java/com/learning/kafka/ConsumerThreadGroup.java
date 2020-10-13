package com.learning.kafka;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConsumerThreadGroup {

	private Properties applicationProps;
	private Properties kafkaProps;
	private List<ConsumerThread> consumers;
	private int numInstances;

	public ConsumerThreadGroup(Properties applicationProps, Properties kafkaProps, int numInstances) {
		this.applicationProps = applicationProps;
		this.kafkaProps = kafkaProps;
		this.consumers = new ArrayList<ConsumerThread>();
		this.numInstances = numInstances;

		for (int i = 0; i < this.numInstances; i++) {
			ConsumerThread consThread = new ConsumerThread(this.kafkaProps, this.applicationProps);
			consumers.add(consThread);
		}
	}

	public void execute() {
		for (ConsumerThread consThread : consumers) {
			Thread t = new Thread(consThread);
			System.out.println("Created consumer thread " + t.getName());
			t.start();
		}
	}
}
