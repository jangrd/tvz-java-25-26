-- Schema for the student attendance application (H2).
-- Every column is prefixed with its table name, so column names are globally
-- unique and joins never need column aliases.
-- Tables are created in foreign-key order: parents first, children after.
-- Enum columns (room_type, session_type, attendance_status, student_study_programme)
-- store the enum constant name.

-- Building: id, name, address
CREATE TABLE IF NOT EXISTS building (
    building_id      VARCHAR(50)  PRIMARY KEY,
    building_name    VARCHAR(100) NOT NULL,
    building_address VARCHAR(255) NOT NULL
);

-- Course: code (id), name, ects, semester
CREATE TABLE IF NOT EXISTS course (
    course_code     VARCHAR(20)  PRIMARY KEY,
    course_name     VARCHAR(100) NOT NULL,
    course_ects     INT          NOT NULL,
    course_semester INT          NOT NULL
);

-- Room: id, type (RoomType), capacity, building
CREATE TABLE IF NOT EXISTS room (
    room_id          VARCHAR(50) PRIMARY KEY,
    room_type        VARCHAR(30) NOT NULL,
    room_capacity    INT         NOT NULL,
    room_building_id VARCHAR(50) NOT NULL REFERENCES building(building_id)
);

-- Professor: extends Person (oib = id); office is a Room of type OFFICE
CREATE TABLE IF NOT EXISTS professor (
    professor_oib            CHAR(11)     PRIMARY KEY,
    professor_first_name     VARCHAR(50)  NOT NULL,
    professor_last_name      VARCHAR(50)  NOT NULL,
    professor_email          VARCHAR(255) NOT NULL,
    professor_dob            DATE         NOT NULL,
    professor_title          VARCHAR(50)  NOT NULL,
    professor_office_room_id VARCHAR(50)  NOT NULL REFERENCES room(room_id),
    professor_department     VARCHAR(100) NOT NULL
);

-- Student: extends Person (jmbag = id)
CREATE TABLE IF NOT EXISTS student (
    student_jmbag           CHAR(10)     PRIMARY KEY,
    student_oib             CHAR(11)     NOT NULL UNIQUE,
    student_first_name      VARCHAR(50)  NOT NULL,
    student_last_name       VARCHAR(50)  NOT NULL,
    student_email           VARCHAR(255) NOT NULL,
    student_dob             DATE         NOT NULL,
    student_year_of_study   INT          NOT NULL,
    student_study_programme VARCHAR(50)  NOT NULL
);

-- Session: one class meeting (id); references course, professor and room
CREATE TABLE IF NOT EXISTS session (
    session_id            VARCHAR(50) PRIMARY KEY,
    session_type          VARCHAR(30) NOT NULL,
    session_course_code   VARCHAR(20) NOT NULL REFERENCES course(course_code),
    session_professor_oib CHAR(11)    NOT NULL REFERENCES professor(professor_oib),
    session_date_time     TIMESTAMP   NOT NULL,
    session_room_id       VARCHAR(50) NOT NULL REFERENCES room(room_id)
);

-- Attendance: Relation<Student, Session> + status + recorded time
CREATE TABLE IF NOT EXISTS attendance (
    attendance_id            VARCHAR(50) PRIMARY KEY,
    attendance_student_jmbag CHAR(10)    NOT NULL REFERENCES student(student_jmbag),
    attendance_session_id    VARCHAR(50) NOT NULL REFERENCES session(session_id),
    attendance_status        VARCHAR(20) NOT NULL,
    attendance_recorded_at   TIMESTAMP   NOT NULL
);
