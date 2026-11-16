package com.auca.university.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false)
    private Integer credits;

    @Column(length = 500)
    private String description;

    // Many courses belong to one department
    @ManyToOne(optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    // Lecturer <-> Course many-to-many, kept in a pure join table (no separate entity)
    @ManyToMany
    @JoinTable(
            name = "course_lecturer",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "lecturer_id"))
    private Set<Lecturer> lecturers = new HashSet<>();

    // Student <-> Course goes through the Registration join entity
    @OneToMany(mappedBy = "course")
    private List<Registration> registrations = new ArrayList<>();

    public Course() {}

    public Course(String code, String title, Integer credits, String description, Department department) {
        this.code = code;
        this.title = title;
        this.credits = credits;
        this.description = description;
        this.department = department;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
    public Set<Lecturer> getLecturers() { return lecturers; }
    public void setLecturers(Set<Lecturer> lecturers) { this.lecturers = lecturers; }
    public List<Registration> getRegistrations() { return registrations; }
    public void setRegistrations(List<Registration> registrations) { this.registrations = registrations; }
}
