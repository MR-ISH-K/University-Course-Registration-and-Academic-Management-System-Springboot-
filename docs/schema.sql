-- Reference relational schema (Hibernate generates the same tables automatically)
-- CREATE DATABASE university_academic_db;

CREATE TABLE departments (
    id               BIGSERIAL PRIMARY KEY,
    name             VARCHAR(100) NOT NULL UNIQUE,
    code             VARCHAR(20)  NOT NULL UNIQUE,
    description      VARCHAR(500),
    head_lecturer_id BIGINT UNIQUE            -- FK added below (circular with lecturers)
);

CREATE TABLE lecturers (
    id            BIGSERIAL PRIMARY KEY,
    first_name    VARCHAR(60)  NOT NULL,
    last_name     VARCHAR(60)  NOT NULL,
    email         VARCHAR(120) NOT NULL UNIQUE,
    phone_number  VARCHAR(20),
    department_id BIGINT NOT NULL REFERENCES departments(id)
);

ALTER TABLE departments
    ADD CONSTRAINT fk_department_head FOREIGN KEY (head_lecturer_id) REFERENCES lecturers(id);

CREATE TABLE courses (
    id            BIGSERIAL PRIMARY KEY,
    code          VARCHAR(20)  NOT NULL UNIQUE,
    title         VARCHAR(150) NOT NULL,
    credits       INTEGER      NOT NULL,
    description   VARCHAR(500),
    department_id BIGINT NOT NULL REFERENCES departments(id)
);

CREATE TABLE course_lecturer (                 -- join table (Course <-> Lecturer)
    course_id   BIGINT NOT NULL REFERENCES courses(id),
    lecturer_id BIGINT NOT NULL REFERENCES lecturers(id),
    PRIMARY KEY (course_id, lecturer_id)
);

CREATE TABLE students (
    id                  BIGSERIAL PRIMARY KEY,
    registration_number VARCHAR(30)  NOT NULL UNIQUE,
    first_name          VARCHAR(60)  NOT NULL,
    last_name           VARCHAR(60)  NOT NULL,
    email               VARCHAR(120) NOT NULL UNIQUE,
    admission_date      DATE NOT NULL,
    department_id       BIGINT NOT NULL REFERENCES departments(id)
);

CREATE TABLE student_profiles (
    id                  BIGSERIAL PRIMARY KEY,
    date_of_birth       DATE NOT NULL,
    gender              VARCHAR(10) NOT NULL,
    phone_number        VARCHAR(20),
    residential_address VARCHAR(255),
    student_id          BIGINT NOT NULL UNIQUE REFERENCES students(id)
);

CREATE TABLE registrations (                    -- join entity (Student <-> Course)
    id                BIGSERIAL PRIMARY KEY,
    student_id        BIGINT NOT NULL REFERENCES students(id),
    course_id         BIGINT NOT NULL REFERENCES courses(id),
    registration_date DATE NOT NULL,
    academic_year     VARCHAR(9) NOT NULL,
    semester          INTEGER NOT NULL,
    status            VARCHAR(10) NOT NULL CHECK (status IN ('PENDING','APPROVED','REJECTED')),
    grade             NUMERIC(5,2),
    CONSTRAINT uk_registration_student_course_term
        UNIQUE (student_id, course_id, academic_year, semester)
);
