package dev.nampd.coursehub.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "course")
@Getter
@Setter
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    private String description;
    @Column(nullable = false)
    private int maxSize;
    private int numberOfSessions;
    @Column(nullable = false)
    private boolean isFull;
    private LocalDate startDate;
    private LocalDate endDate;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private Set<Enrollment> enrollments = new HashSet<>();

    public Course() {}

    public Course(String name, String description, int maxSize, int numberOfSessions, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.description = description;
        this.maxSize = maxSize;
        this.numberOfSessions = numberOfSessions;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isFull = false;
    }

    public void updateStatus() {
        this.isFull = enrollments.size() >= maxSize;
    }
}
