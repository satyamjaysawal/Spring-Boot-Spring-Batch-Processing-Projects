package com.batchprocessing.BatchProcessingExampleDemo;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

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

	@Scheduled(cron = "*/30 * * * * *")
	public void launchJobA() throws Exception {
		Date currentDate = new Date();
		System.out.println("Scheduler Job Started at : " + currentDate);

		JobExecution jobExecution = jobLauncher.run(jobA, new JobParametersBuilder()
				.addDate("date", currentDate)
				.toJobParameters());

		// Check the job status
		BatchStatus batchStatus = jobExecution.getStatus();
		if (batchStatus == BatchStatus.COMPLETED) {
			System.out.println("Scheduler Job Completed successfully at : "+ "Job ID: " + jobExecution.getJobId() +" : " + new Date() );
		} else if (batchStatus == BatchStatus.FAILED) {
			System.out.println("Scheduler Job failed at : " + new Date());
			System.out.println("Exit Status: " + jobExecution.getExitStatus().getExitCode()+" : " +"Job ID: " + jobExecution.getJobId());
		}
		System.out.println("Scheduler Job Ended at : " + new Date());
	}



}
