package dev.nampd.coursehub.repository;

import dev.nampd.coursehub.model.entity.WeeklyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeeklyReportRepository extends JpaRepository<WeeklyReport, Long> {
}
