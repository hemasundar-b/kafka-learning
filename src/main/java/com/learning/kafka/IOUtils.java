package com.learning.kafka;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class IOUtils {

	public static void writeToFile(Properties applicationProps, String output) throws Exception {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
		Date now = new Date();
		String currentDate = format.format(now);
		
		String completeFilePath = applicationProps.getProperty("output.file.path") + "/"
				+ currentDate + "_"
				+ applicationProps.getProperty("output.file.prefix") + "_"
				+ applicationProps.getProperty("output.file.name")
				+ applicationProps.getProperty("output.file.suffix");

		BufferedWriter bwr = new BufferedWriter(new FileWriter(completeFilePath));
		bwr.write(output);
		bwr.newLine();
		bwr.close();

	}

}
