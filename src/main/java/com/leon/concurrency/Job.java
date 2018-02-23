package com.leon.concurrency;

import lombok.Data;

@Data
public class Job implements Runnable {

    private String jobName;

    private JobPriority jobPriority;

    Job(String jobName) {
        this(jobName, JobPriority.MEDIUM);
    }

    Job(String jobName, JobPriority jobPriority) {
        this.jobName = jobName;
        this.jobPriority = jobPriority;
    }

    @Override
    public void run() {
        try {
            System.out.println("Job: " + jobName + " Priority: " + jobPriority);
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            System.err.println(ex.getMessage());
        }
    }

}
