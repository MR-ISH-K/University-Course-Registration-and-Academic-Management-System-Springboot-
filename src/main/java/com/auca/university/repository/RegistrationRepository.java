package com.auca.university.repository;

import com.auca.university.model.Registration;
import com.auca.university.model.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByStudentId(Long studentId);
    List<Registration> findByCourseId(Long courseId);
    List<Registration> findByStatus(RegistrationStatus status);
    boolean existsByStudentIdAndCourseIdAndAcademicYearAndSemester(
            Long studentId, Long courseId, String academicYear, Integer semester);
}
