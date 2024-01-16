package com.example.BatchProcessingConfigJob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.batch.core.Step;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class HelloWorldBatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final JobLauncher jobLauncher;

    private final Logger log = LoggerFactory.getLogger(HelloWorldBatchConfig.class);

    public HelloWorldBatchConfig(JobBuilderFactory jobBuilderFactory,
                                 StepBuilderFactory stepBuilderFactory,
                                 JobLauncher jobLauncher) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.jobLauncher = jobLauncher;
    }

    @Bean
    public Job job1() {
        log.info("Creating Job1");
        return jobBuilderFactory.get("Job1")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .build();
    }

    @Bean
    public Step step1() {
        log.info("Creating Step1");
        return stepBuilderFactory.get("Step1")
                .tasklet(Tasklet1())
                .build();
    }

    @Bean
    public Tasklet Tasklet1() {
        return (contribution, chunkContext) -> {
            log.info("Hello, World! Current Time: {}", getCurrentTimeFormatted());
            return null;
        };
    }

    @Scheduled(cron = "0 * * * * ?")
    public void scheduledTask1() {
        log.info("Scheduled task 1 executed! Current Time: {}", getCurrentTimeFormatted());
        launchJob(job1());
    }

    @Bean
    public Job job2() {
        log.info("Creating Job2");
        return jobBuilderFactory.get("Job2")
                .incrementer(new RunIdIncrementer())
                .start(step2())
                .build();
    }

    @Bean
    public Step step2() {
        log.info("Creating Step2");
        return stepBuilderFactory.get("Step2")
                .tasklet(Tasklet2())
                .build();
    }

    @Bean
    public Tasklet Tasklet2() {
        return (contribution, chunkContext) -> {
            log.info("Second Tasklet! Current Time: {}", getCurrentTimeFormatted());
            return null;
        };
    }

    @Scheduled(cron = "*/30 * * * * ?")
    public void scheduledTask2() {
        log.info("Scheduled task 2 executed! Current Time: {}", getCurrentTimeFormatted());
        launchJob(job2());
    }

    private String getCurrentTimeFormatted() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss:SSS dd/MM/yyyy");
        return now.format(formatter);
    }

    private void launchJob(Job job) {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution jobExecution = jobLauncher.run(job, jobParameters);

            log.info("Job {} ..................completed with status...........................  {}", job.getName(), jobExecution.getStatus());
        } catch (Exception e) {
            log.error("Error launching job", e);
        }
    }
}
