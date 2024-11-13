package dev.nampd.coursehub.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailJobScheduler {

    private final JobLauncher jobLauncher;
    private final Job emailReminderJob;

    public EmailJobScheduler(JobLauncher jobLauncher, Job emailReminderJob) {
        this.jobLauncher = jobLauncher;
        this.emailReminderJob = emailReminderJob;
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void runEmailReminderJob() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(emailReminderJob, params);
        } catch (Exception e) {
            log.error("Failed to run email reminder job", e);
        }
    }
}
