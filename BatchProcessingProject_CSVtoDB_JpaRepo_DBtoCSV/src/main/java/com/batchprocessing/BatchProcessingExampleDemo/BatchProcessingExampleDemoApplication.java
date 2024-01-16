package com.batchprocessing.BatchProcessingExampleDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BatchProcessingExampleDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchProcessingExampleDemoApplication.class, args);
	}

}