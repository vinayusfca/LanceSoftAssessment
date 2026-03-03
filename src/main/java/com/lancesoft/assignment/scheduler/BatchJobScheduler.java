package com.lancesoft.assignment.scheduler;

import com.lancesoft.assignment.batch.AccountInsertJobConfig;
import com.lancesoft.assignment.batch.AccountNotificationJobConfig;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BatchJobScheduler {

    private final JobLauncher jobLauncher;
    private final Job insertAccountsJob;
    private final Job notifyAccountsJob;

    public BatchJobScheduler(JobLauncher jobLauncher,
                             @Qualifier(AccountInsertJobConfig.INSERT_JOB_NAME) Job insertAccountsJob,
                             @Qualifier(AccountNotificationJobConfig.NOTIFY_JOB_NAME) Job notifyAccountsJob) {
        this.jobLauncher = jobLauncher;
        this.insertAccountsJob = insertAccountsJob;
        this.notifyAccountsJob = notifyAccountsJob;
    }

    /**
     * Runs at 2:00 AM IST every day to insert 100 records.
     */
    @Scheduled(cron = "0 00 02 * * *")
    public void runInsertAccountsJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();

        System.out.println("Starting insertAccountsJob at 2:00 AM");
        jobLauncher.run(insertAccountsJob, params);
    }

    /**
     * Runs at 15:00 PM every day to retrieve accounts and send notifications.
     */
    @Scheduled(cron = "0 00 15 * * *")
    public void runNotifyAccountsJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();

        System.out.println("Starting notifyAccountsJob at 3:00 PM");
        jobLauncher.run(notifyAccountsJob, params);
    }
}

