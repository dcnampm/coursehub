package dev.nampd.coursehub.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class DetailedReportDto {
    private String courseName;
    private String courseStatus;
    private Long numberOfEnrollments;

    public DetailedReportDto(String courseName, String courseStatus, Long numberOfEnrollments) {
        this.courseName = courseName;
        this.courseStatus = courseStatus;
        this.numberOfEnrollments = numberOfEnrollments;
    }
}
