package com.example.BatchProcessingConfigJob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableBatchProcessing
@EnableScheduling // Enable Spring Scheduler
public class HelloWorldBatchConfig {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	private final Logger log = LoggerFactory.getLogger(HelloWorldBatchConfig.class);

	public HelloWorldBatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	@Bean
	public Job helloWorldJob() {
		log.info("Creating helloWorldJob");
		return jobBuilderFactory.get("helloWorldJob").start(helloWorldStep()).build();
	}

	@Bean
	public Step helloWorldStep() {
		log.info("Creating helloWorldStep");
		return stepBuilderFactory.get("helloWorldStep").tasklet(helloWorldTasklet()).build();
	}

	@Bean
	public Tasklet helloWorldTasklet() {
		return (contribution, chunkContext) -> {
			log.info("Hello, World!");
			return null;
		};
	}

	// Scheduled task example
	@Scheduled(fixedRate = 5000) // Execute every 5 seconds
	public void scheduledTask() {
		log.info("Scheduled task executed!");
		// Add your scheduled task logic here
	}

	// Scheduled task using cron expression (execute every minute)
	@Scheduled(cron = "0 * * * * ?")   //every minutes job run again
	public void scheduledTaskUsingCron() {
		log.info("Scheduled task executed! using Cron");
		// Add your scheduled task logic here
	}
}
