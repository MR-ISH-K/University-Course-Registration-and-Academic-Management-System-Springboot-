package com.auca.university.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Join entity for the Student <-> Course many-to-many relationship.
 * It needs its own entity because each registration carries extra data
 * (date, academic year, semester, status, grade).
 */
@Entity
@Table(name = "registrations",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_registration_student_course_term",
                columnNames = {"student_id", "course_id", "academic_year", "semester"}))
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;

    @Column(name = "academic_year", nullable = false, length = 9)
    private String academicYear; // e.g. 2026/2027

    @Column(nullable = false)
    private Integer semester; // 1 or 2

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private RegistrationStatus status = RegistrationStatus.PENDING;

    // Nullable: no grade until the course is assessed
    @Column(precision = 5, scale = 2)
    private BigDecimal grade;

    public Registration() {}

    public Registration(Student student, Course course, LocalDate registrationDate,
                        String academicYear, Integer semester, RegistrationStatus status, BigDecimal grade) {
        this.student = student;
        this.course = course;
        this.registrationDate = registrationDate;
        this.academicYear = academicYear;
        this.semester = semester;
        this.status = status;
        this.grade = grade;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }
    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }
    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }
    public RegistrationStatus getStatus() { return status; }
    public void setStatus(RegistrationStatus status) { this.status = status; }
    public BigDecimal getGrade() { return grade; }
    public void setGrade(BigDecimal grade) { this.grade = grade; }
}
