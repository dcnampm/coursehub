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
public class JobScheduler {

    private final JobLauncher jobLauncher;
    private final Job emailReminderJob;
    private final Job weeklyReportJob;


    public JobScheduler(JobLauncher jobLauncher, Job emailReminderJob, Job weeklyReportJob) {
        this.jobLauncher = jobLauncher;
        this.emailReminderJob = emailReminderJob;
        this.weeklyReportJob = weeklyReportJob;
    }

    @Scheduled(cron = "0 */1 * * * *")
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

    @Scheduled(cron = "0 */2 * * * *")
//    @Scheduled(cron = "0 0 9 * * *")
    public void runWeeklyReportJob() {
        try {
            jobLauncher.run(weeklyReportJob, new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters());
            log.info("Weekly report job has been executed successfully.");
        } catch (Exception e) {
            log.error("Failed to execute weekly report job.", e);
        }
    }
}
