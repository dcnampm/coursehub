package dev.nampd.coursehub.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
    private Long id;
    private String name;
    private String description;
    private int maxSize;
    private int numberOfSessions;
    private boolean isFull;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<EnrollmentDto> enrollments;

    private String status;
    private int remainingSlots;

    public String getStatus() {
        LocalDate today = LocalDate.now();
        if (startDate != null && endDate != null) {
            if (today.isBefore(startDate)) {
                return "Chưa bắt đầu";
            } else if (today.isAfter(endDate)) {
                return "Đã kết thúc";
            } else {
                return "Đang diễn ra";
            }
        }
        return "N/A";
    }

    public CourseDto(Long id, String name, String description, int maxSize, int numberOfSessions, boolean isFull, LocalDate startDate, LocalDate endDate, List<EnrollmentDto> enrollments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.maxSize = maxSize;
        this.numberOfSessions = numberOfSessions;
        this.isFull = isFull;
        this.startDate = startDate;
        this.endDate = endDate;
        this.enrollments = enrollments;
        this.remainingSlots = maxSize - (enrollments != null ? enrollments.size() : 0);
    }

    public void updateStatus() {
        this.isFull = enrollments.size() >= maxSize;
    }
}
