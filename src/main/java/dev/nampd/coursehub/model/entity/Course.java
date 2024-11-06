package dev.nampd.coursehub.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
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
    private int remainingSlots;
    private int maxSize;
    @Column(nullable = false)
    private boolean isFull;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private Set<Enrollment> enrollments = new HashSet<>();

    public void updateStatus() {
        this.isFull = enrollments.size() >= maxSize;
    }

    public void decrementRemainingSlots() {
        if (remainingSlots > 0 && remainingSlots <= maxSize) {
            remainingSlots--;
            updateStatus();
        }
    }

    public void incrementRemainingSlots() {
        if (remainingSlots < maxSize) {
            remainingSlots++;
            updateStatus();
        }
    }
}
