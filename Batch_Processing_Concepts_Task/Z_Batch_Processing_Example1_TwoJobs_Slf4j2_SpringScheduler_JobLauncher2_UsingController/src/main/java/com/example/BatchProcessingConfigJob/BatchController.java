package com.example.BatchProcessingConfigJob;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/batch")
public class BatchController {

    private final JobLauncher jobLauncher;
    private final Job job1;
    private final Job job2;

    @Autowired
    public BatchController(JobLauncher jobLauncher, Job job1, Job job2) {
        this.jobLauncher = jobLauncher;
        this.job1 = job1;
        this.job2 = job2;
    }

    @GetMapping("/runJob1")
    public Map<String, String> runJob1() {
        return runJob(job1, "Job1");
    }

    @GetMapping("/runJob2")
    public Map<String, String> runJob2() {
        return runJob(job2, "Job2");
    }

    private Map<String, String> runJob(Job job, String jobName) {
        Map<String, String> response = new HashMap<>();
        try {
            jobLauncher.run(job, new JobParameters());
            response.put("status", "Job " + jobName + " started successfully");
        } catch (Exception e) {
            response.put("status", "Error starting " + jobName + " job");
            response.put("error", e.getMessage());
        }
        return response;
    }
}
