package com.learning.kafka;

import java.util.Collection;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;

public class RebalanceListener implements ConsumerRebalanceListener {

	public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
		System.out.println("Rebalance Triggered");

	}

	public void onPartitionsAssigned(Collection<TopicPartition> partitions) {

	}

}
