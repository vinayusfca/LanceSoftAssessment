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

import java.util.UUID;
import java.util.stream.IntStream;

@Configuration
public class AccountInsertJobConfig {

    private final JobRepository jobRepository;
    private final AccountRepository accountRepository;
    private final PlatformTransactionManager transactionManager;

    public static final String INSERT_JOB_NAME = "insertAccountsJob";

    public AccountInsertJobConfig(JobRepository jobRepository,
                                  AccountRepository accountRepository,
                                  PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.accountRepository = accountRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job insertAccountsJob(Step insertAccountsStep) {
        return new JobBuilder(INSERT_JOB_NAME, jobRepository)
                .start(insertAccountsStep)
                .build();
    }

    @Bean
    public Step insertAccountsStep() {
        return new StepBuilder("insertAccountsStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    // Insert 100 dummy account records
                    IntStream.rangeClosed(1, 100).forEach(i -> {
                        Account account = new Account();
                        account.setAccountId("ACC-" + UUID.randomUUID());
                        account.setAccountName("User " + i);
                        account.setBalance(1000.0 + i);
                        account.setEmail("user" + i + "@example.com");
                        account.setPhoneNumber("99999999" + String.format("%02d", i % 100));
                        account.setWhatsappNumber("88888888" + String.format("%02d", i % 100));
                        accountRepository.save(account);
                    });
                    System.out.println("Inserted 100 account records into the database.");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}

