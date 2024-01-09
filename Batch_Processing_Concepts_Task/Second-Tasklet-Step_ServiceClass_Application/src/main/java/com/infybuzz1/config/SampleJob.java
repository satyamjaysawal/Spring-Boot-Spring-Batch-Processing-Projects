package com.infybuzz1.config;

import com.infybuzz1.service.SecondTasklest;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SampleJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private SecondTasklest secondTasklest;

    // Define the Spring Batch Job
    @Bean
    public Job job() {
        return jobBuilderFactory.get("First Job")
                .start(firstStep())
                .next(secondStep())
                .build();
    }

    // Define the first Step in the Job
    private Step firstStep() {
        return stepBuilderFactory.get("First Step")
                .tasklet(firstTask())
                .build();
    }

    // Define the Tasklet for the first step
    private Tasklet firstTask() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                System.out.println("This is the first tasklet step");
                return RepeatStatus.FINISHED;
            }
        };
    }

    // Define the second Step in the Job
    private Step secondStep() {
        return stepBuilderFactory.get("Second Step")
//                .tasklet(secondTask())
                .tasklet(secondTasklest)
                .build();
    }

    // Define the Tasklet for the second step
//    private Tasklet secondTask() {
//        return new Tasklet() {
//            @Override
//            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
//                System.out.println("This is the second tasklet step");
//                return RepeatStatus.FINISHED;
//            }
//        };
//    }
}
