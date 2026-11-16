package com.auca.university;

import com.auca.university.model.*;
import com.auca.university.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Boots the app against the real PostgreSQL database, lets Hibernate generate the
 * tables, and saves sample data through every relationship.
 * @Commit keeps the rows so you can show them in pgAdmin for your screenshots.
 */
@SpringBootTest
@Transactional
@Commit
class EntityMappingTest {

    @Autowired DepartmentRepository departmentRepository;
    @Autowired LecturerRepository lecturerRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired StudentRepository studentRepository;
    @Autowired StudentProfileRepository profileRepository;
    @Autowired RegistrationRepository registrationRepository;

    @Test
    void mappingsWork() {
        String suffix = String.valueOf(System.currentTimeMillis() % 100000);

        // Department (head assigned after lecturers exist)
        Department cs = departmentRepository.save(
                new Department("Computer Science " + suffix, "CS" + suffix, "Software and computing"));

        // Lecturers (many-to-one -> department)
        Lecturer head = lecturerRepository.save(
                new Lecturer("Alice", "Mukamana", "alice" + suffix + "@auca.ac.rw", "0788000001", cs));
        Lecturer other = lecturerRepository.save(
                new Lecturer("Bob", "Habimana", "bob" + suffix + "@auca.ac.rw", "0788000002", cs));

        // One-to-one: department head
        cs.setHead(head);
        departmentRepository.save(cs);

        // Courses (many-to-one -> department, many-to-many <-> lecturers via course_lecturer)
        Course web = new Course("WT" + suffix, "Web Technology", 3, "Spring Boot basics", cs);
        web.getLecturers().add(head);
        web.getLecturers().add(other);
        web = courseRepository.save(web);

        Course db = new Course("DB" + suffix, "Databases", 3, "SQL and design", cs);
        db.getLecturers().add(head);
        db = courseRepository.save(db);

        // Student + one-to-one profile
        Student student = new Student("REG" + suffix, "Arsene", "Kunda",
                "arsene" + suffix + "@auca.ac.rw", LocalDate.of(2024, 9, 1), cs);
        student.setProfile(new StudentProfile(LocalDate.of(2003, 5, 14), Gender.MALE,
                "0788111222", "Gishushu, Kigali"));
        student = studentRepository.save(student);

        // Registrations (join entity with extra attributes)
        registrationRepository.save(new Registration(student, web, LocalDate.now(),
                "2026/2027", 1, RegistrationStatus.APPROVED, new BigDecimal("85.50")));
        registrationRepository.save(new Registration(student, db, LocalDate.now(),
                "2026/2027", 1, RegistrationStatus.PENDING, null));

        // Assertions
        assertNotNull(student.getId());
        assertTrue(profileRepository.findByStudentId(student.getId()).isPresent());
        assertEquals(2, registrationRepository.findByStudentId(student.getId()).size());
        assertEquals(2, courseRepository.findByLecturersId(head.getId()).size());
        assertEquals(1, courseRepository.findByLecturersId(other.getId()).size());
        List<Registration> approved = registrationRepository.findByStatus(RegistrationStatus.APPROVED);
        assertFalse(approved.isEmpty());
        assertEquals(head.getId(), departmentRepository.findByCode(cs.getCode()).orElseThrow().getHead().getId());
    }
}
