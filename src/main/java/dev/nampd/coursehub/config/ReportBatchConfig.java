    package dev.nampd.coursehub.config;

    import dev.nampd.coursehub.model.dto.DetailedReportDto;
    import dev.nampd.coursehub.model.entity.WeeklyReport;
    import dev.nampd.coursehub.repository.CourseRepository;
    import dev.nampd.coursehub.repository.ReportRepository;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.batch.core.*;
    import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
    import org.springframework.batch.core.configuration.annotation.JobScope;
    import org.springframework.batch.core.job.builder.JobBuilder;
    import org.springframework.batch.core.repository.JobRepository;
    import org.springframework.batch.core.step.builder.StepBuilder;
    import org.springframework.batch.item.ItemProcessor;
    import org.springframework.batch.item.ItemReader;
    import org.springframework.batch.item.file.FlatFileItemWriter;
    import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
    import org.springframework.batch.item.support.ListItemReader;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.core.io.FileSystemResource;
    import org.springframework.transaction.PlatformTransactionManager;

    import java.io.IOException;
    import java.nio.file.Files;
    import java.nio.file.Paths;
    import java.time.DayOfWeek;
    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.time.format.DateTimeFormatter;
    import java.time.temporal.WeekFields;
    import java.util.List;
    import java.util.Locale;

    @Configuration
    @Slf4j
    @EnableBatchProcessing
    public class ReportBatchConfig {
        String directoryPath = "C:\\Users\\nampd\\IdeaProjects\\coursehub\\report";

        private final CourseRepository courseRepository;
        private final ReportRepository reportRepository;

        public ReportBatchConfig(CourseRepository courseRepository, ReportRepository reportRepository) {
            this.courseRepository = courseRepository;
            this.reportRepository = reportRepository;
        }

        @Bean
        public Job weeklyReportJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
            log.info("vao doan code này ");
            return new JobBuilder("weeklyReportJob", jobRepository)
                    .start(weeklyReportStep(jobRepository, transactionManager))
                    .build();
        }

        @Bean
        public Step weeklyReportStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
            return new StepBuilder("weeklyReportStep", jobRepository)
                    .<DetailedReportDto, DetailedReportDto>chunk(100, transactionManager)
                    .reader(courseReportReader())
                    .processor(courseReportProcessor())
                    .writer(courseReportWriter())
                    .listener(reportStepExecutionListener())
                    .build();
        }

        @Bean
        @JobScope
        public ItemReader<DetailedReportDto> courseReportReader() {
            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusWeeks(1);

            List<DetailedReportDto> reportData = courseRepository.findCourseReport(startDate, endDate);
            log.info("số lượng data: {}" , reportData.size());
            return new ListItemReader<>(reportData);
        }

        @Bean
        @JobScope
        public ItemProcessor<DetailedReportDto, DetailedReportDto> courseReportProcessor() {
            return report -> {
                return report;
            };
        }

        @Bean
        @JobScope
        public FlatFileItemWriter<DetailedReportDto> courseReportWriter() {
            String fileName = directoryPath + "/weekly_course_report_" + LocalDate.now().minusWeeks(1) + "_" +  LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) +  ".csv";

            return new FlatFileItemWriterBuilder<DetailedReportDto>()
                    .name("detailedReportWriter")
                    .resource(new FileSystemResource(fileName))
                    .delimited()
                    .delimiter(",")
                    .names("courseName", "courseStatus", "numberOfEnrollments")
                    .headerCallback(writer -> writer.write("Course Name,Course Status,Number of Enrollments"))
                    .build();
        }

        @Bean
        @JobScope
        public StepExecutionListener reportStepExecutionListener() {
            return new StepExecutionListener() {
                @Override
                public void beforeStep(StepExecution stepExecution) {
                    try {
                        Files.createDirectories(Paths.get(directoryPath));
                    } catch (IOException e) {
                        log.error("Failed to create report directory.", e);
                        throw new RuntimeException("Cannot create report directory", e);
                    }
                }

                @Override
                public ExitStatus afterStep(StepExecution stepExecution) {
                    //lưu thông tin báo cáo vào db
                    String filePath = directoryPath + "/weekly_course_report_" + LocalDate.now().minusWeeks(1) + "_" +  LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) +  ".csv";
                    WeeklyReport report = new WeeklyReport();
                    report.setTitle("Weekly Course Enrollment Report");
                    report.setCreatedBy("System");
                    report.setCreatedAt(LocalDate.now());
                    report.setReportStartDate(LocalDate.now().minusWeeks(1).with(DayOfWeek.MONDAY));
                    report.setReportEndDate(LocalDate.now().minusWeeks(1).with(DayOfWeek.SUNDAY));
                    report.setFilePath(filePath);
                    reportRepository.save(report);
                    log.info("Weekly report saved to database with file path: {}", filePath);
                    return ExitStatus.COMPLETED;
                }
            };
        }

    }
