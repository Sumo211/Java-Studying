package com.leon.concurrency.priority;

import lombok.Data;

@Data
public class Job implements Runnable {

    private String jobName;

    private JobPriority jobPriority;

    public Job(String jobName) {
        this(jobName, JobPriority.MEDIUM);
    }

    public Job(String jobName, JobPriority jobPriority) {
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
