package com.learning.kafka;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class ConsumerDriverMain {

	private static Properties getProperties(String configFilePath) throws Exception {

		File configFile = new File(configFilePath);
		FileInputStream inputStream = new FileInputStream(configFile);
		Properties props = new Properties();
		props.load(inputStream);
		return props;

	}

	public static void main(String[] args) throws Exception {
		String kafkaConfigFile = args[0];
		String applicationConfigFile = args[1];
		int numInstances = Integer.parseInt(args[2]);
		
		Properties kafkaProps = getProperties(kafkaConfigFile);
		Properties applicationProps = getProperties(applicationConfigFile);
		
		ConsumerThreadGroup group = new ConsumerThreadGroup(kafkaProps, applicationProps,numInstances);
		group.execute();
		
		

	}

}
