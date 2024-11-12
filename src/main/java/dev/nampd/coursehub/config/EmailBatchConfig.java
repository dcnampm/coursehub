package dev.nampd.coursehub.config;

import dev.nampd.coursehub.model.dto.CourseDto;
import dev.nampd.coursehub.model.dto.StudentCourseDto;
import dev.nampd.coursehub.model.dto.StudentDto;
import dev.nampd.coursehub.repository.CourseRepository;
import dev.nampd.coursehub.repository.StudentRepository;
import dev.nampd.coursehub.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
@EnableBatchProcessing
public class EmailBatchConfig {

    private final EmailService emailService;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EmailBatchConfig(EmailService emailService, StudentRepository studentRepository, CourseRepository courseRepository) {
        this.emailService = emailService;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Bean
    public Job emailBatchJob(JobRepository jobRepository,  PlatformTransactionManager transactionManager) {
        return new JobBuilder("emailBatchJob", jobRepository)
                .start(sendStudentReminderStep(jobRepository, transactionManager))
                .next(sendCourseExpirationStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step sendStudentReminderStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("sendReminderStep", jobRepository)
                .<StudentDto, StudentDto>chunk(100, transactionManager)
                .reader(notEnrolledStudentReader())
                .writer(studentEmailWriter())
                .build();
    }

    @Bean
    public ListItemReader<StudentDto> notEnrolledStudentReader() {
        List<StudentDto> students = studentRepository.findStudentsWithNoEnrollments();
        return new ListItemReader<>(students);
    }

    @Bean
    public ItemWriter<StudentDto> studentEmailWriter() {
        return students -> {
            for (StudentDto student : students) {
                emailService.sendStudentReminderEmail(student);

            }
        };
    }

    @Bean
    public Step sendCourseExpirationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("sendCourseExpirationStep", jobRepository)
                .<StudentCourseDto, StudentCourseDto>chunk(100, transactionManager)
                .reader(courseVacancyReader())
//                .writer(courseEmailWriter())
                .build();
    }

    @Bean
    public ListItemReader<StudentCourseDto> courseVacancyReader() {
        List<StudentCourseDto> studentCourses = new ArrayList<>();

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<CourseDto> courses = courseRepository.findCoursesWithVacanciesStartingOn(tomorrow);
        for (CourseDto course : courses) {
            List<StudentDto> students = studentRepository.findStudentsNotEnrolledInCourse(course.getId());
            for (StudentDto student : students) {
                studentCourses.add(new StudentCourseDto(student, course));
            }
        }

        return new ListItemReader<>(studentCourses);
    }

//    @Bean
//    public ItemWriter<StudentCourseDto> courseEmailWriter() {
//        return studentCourseDtos -> {
//            for (StudentCourseDto scDto : studentCourseDtos) {
//                try {
//                    emailService.sendCourseExpirationEmail(scDto.getStudent(), scDto.getCourse());
//                } catch (Exception e) {
//                    logger.error("Failed to send email to " + scDto.getStudent().getEmail(), e);
//                }
//            }
//        };
//    }
}
