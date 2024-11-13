package dev.nampd.coursehub.repository;

import dev.nampd.coursehub.model.dto.StudentDto;
import dev.nampd.coursehub.model.entity.Role;
import dev.nampd.coursehub.model.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Student> findAllByRole(Role role);
}
