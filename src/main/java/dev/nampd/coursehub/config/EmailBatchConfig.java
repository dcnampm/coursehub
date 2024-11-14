package dev.nampd.coursehub.config;

import dev.nampd.coursehub.mapper.CourseMapper;
import dev.nampd.coursehub.mapper.StudentMapper;
import dev.nampd.coursehub.model.dto.CourseDto;
import dev.nampd.coursehub.model.dto.StudentDto;
import dev.nampd.coursehub.model.dto.StudentReminderDto;
import dev.nampd.coursehub.model.entity.Course;
import dev.nampd.coursehub.model.entity.Role;
import dev.nampd.coursehub.model.entity.Student;
import dev.nampd.coursehub.repository.CourseRepository;
import dev.nampd.coursehub.repository.StudentRepository;
import dev.nampd.coursehub.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.List;

@Configuration
@Slf4j
@EnableBatchProcessing
public class EmailBatchConfig {

    private final EmailService emailService;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final StudentMapper studentMapper;
    private final CourseMapper courseMapper;

    public EmailBatchConfig(EmailService emailService, StudentRepository studentRepository, CourseRepository courseRepository, StudentMapper studentMapper, CourseMapper courseMapper) {
        this.emailService = emailService;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.studentMapper = studentMapper;
        this.courseMapper = courseMapper;
    }

    @Bean
    public Job emailReminderJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("emailReminderJob", jobRepository)
                .start(emailReminderStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step emailReminderStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("emailReminderStep", jobRepository)
                .<Student, StudentReminderDto>chunk(10, transactionManager)
                .reader(studentReader())
                .processor(emailReminderProcessor())
                .writer(emailWriter())
                .build();
    }

    @Bean
    public ItemReader<Student> studentReader() {
        return new ListItemReader<>(studentRepository.findAllByRole(Role.STUDENT));
    }

    @Bean
    public ItemProcessor<Student, StudentReminderDto> emailReminderProcessor() {
        return student -> {

            List<Course> coursesToRecommend = courseRepository.findCoursesForStudentReminder(student.getId(), LocalDate.now());

            if (coursesToRecommend.isEmpty()) {
                return null;
            }

            StudentDto studentDto = studentMapper.toStudentDto(student);

            List<CourseDto> courseDtosToRecommend = coursesToRecommend.stream()
                    .map(courseMapper::toCourseDto)
                    .toList();

            return new StudentReminderDto(studentDto, courseDtosToRecommend);
        };
    }

    @Bean
    public ItemWriter<StudentReminderDto> emailWriter() {
        return reminders -> {
            for (StudentReminderDto reminder : reminders) {
                try {
                    emailService.sendStudentReminderEmail(reminder.getStudent(), reminder.getCourses());
                } catch (Exception e) {
                    log.error("Failed to send email to {}", reminder.getStudent().getEmail());
                }
            }
        };
    }
}
