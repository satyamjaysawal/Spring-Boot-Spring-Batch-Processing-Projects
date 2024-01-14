package com.batchprocessing.BatchProcessingExampleDemo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MyJobRunner implements CommandLineRunner {
	
	@Autowired
	private Job jobA;
	
	@Autowired
	private JobLauncher  jobLauncher;

	public void run(String... args) throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
				.addLong("time", System.currentTimeMillis())
				.toJobParameters();
		jobLauncher.run(jobA, jobParameters);
		System.out.println("EXECUTION IS DONE");
	}

	// Schedule the job to run every minute
	@Scheduled(cron = "1 * * * * *")
	public void scheduleJob() {
		try {
			JobParameters jobParameters = new JobParametersBuilder()
					.addLong("time", System.currentTimeMillis())
					.toJobParameters();
			jobLauncher.run(jobA, jobParameters);
			System.out.println("SCHEDULED EXECUTION IS DONE");
		} catch (Exception e) {
			// Handle exceptions if needed
			e.printStackTrace();
		}
	}

}
