package com.lancesoft.assignment.batch;

import com.lancesoft.assignment.model.Account;
import com.lancesoft.assignment.repository.AccountRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class AccountNotificationJobConfig {

    private final JobRepository jobRepository;
    private final AccountRepository accountRepository;
    private final PlatformTransactionManager transactionManager;

    public static final String NOTIFY_JOB_NAME = "notifyAccountsJob";

    public AccountNotificationJobConfig(JobRepository jobRepository,
                                        AccountRepository accountRepository,
                                        PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.accountRepository = accountRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job notifyAccountsJob(Step notifyAccountsStep) {
        return new JobBuilder(NOTIFY_JOB_NAME, jobRepository)
                .start(notifyAccountsStep)
                .build();
    }

    @Bean
    public Step notifyAccountsStep() {
        return new StepBuilder("notifyAccountsStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    for (Account account : accountRepository.findAll()) {
                        // Send notifications
                        System.out.printf("Sending email to %s (%s) about balance %.2f%n",
                                account.getAccountName(), account.getEmail(), account.getBalance());
                        System.out.printf("Sending SMS to %s about balance %.2f%n",
                                account.getPhoneNumber(), account.getBalance());
                        System.out.printf("Sending WhatsApp message to %s about balance %.2f%n",
                                account.getWhatsappNumber(), account.getBalance());
                    }
                    System.out.println("Finished sending notifications to all accounts.");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}

