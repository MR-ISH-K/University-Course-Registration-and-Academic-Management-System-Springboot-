package com.auca.university.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(length = 500)
    private String description;

    // One department has exactly one head lecturer (FK head_lecturer_id, unique => 1:1)
    @OneToOne
    @JoinColumn(name = "head_lecturer_id", unique = true)
    private Lecturer head;

    // A department employs many lecturers (FK lives in lecturers.department_id)
    @OneToMany(mappedBy = "department")
    private List<Lecturer> lecturers = new ArrayList<>();

    // A department offers many courses (FK lives in courses.department_id)
    @OneToMany(mappedBy = "department")
    private List<Course> courses = new ArrayList<>();

    // A department admits many students (FK lives in students.department_id)
    @OneToMany(mappedBy = "department")
    private List<Student> students = new ArrayList<>();

    public Department() {}

    public Department(String name, String code, String description) {
        this.name = name;
        this.code = code;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Lecturer getHead() { return head; }
    public void setHead(Lecturer head) { this.head = head; }
    public List<Lecturer> getLecturers() { return lecturers; }
    public void setLecturers(List<Lecturer> lecturers) { this.lecturers = lecturers; }
    public List<Course> getCourses() { return courses; }
    public void setCourses(List<Course> courses) { this.courses = courses; }
    public List<Student> getStudents() { return students; }
    public void setStudents(List<Student> students) { this.students = students; }
}
