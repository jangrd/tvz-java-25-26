-- Schema for the student attendance application (H2).
-- Tables are created in foreign-key order: parents first, children after.
-- Enum columns (type, status, study_programme) store the enum constant name.

-- Building: id, name, address
CREATE TABLE IF NOT EXISTS building (
    id      VARCHAR(50)  PRIMARY KEY,
    name    VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL
);

-- Course: code (id), name, ects, semester
CREATE TABLE IF NOT EXISTS course (
    code     VARCHAR(20)  PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    ects     INT          NOT NULL,
    semester INT          NOT NULL
);

-- Room: id, type (RoomType), capacity, building
CREATE TABLE IF NOT EXISTS room (
    id          VARCHAR(50) PRIMARY KEY,
    type        VARCHAR(30) NOT NULL,
    capacity    INT         NOT NULL,
    building_id VARCHAR(50) NOT NULL REFERENCES building(id)
);

-- Professor: extends Person (oib = id); office is a Room of type OFFICE
CREATE TABLE IF NOT EXISTS professor (
    oib            CHAR(11)     PRIMARY KEY,
    first_name     VARCHAR(50)  NOT NULL,
    last_name      VARCHAR(50)  NOT NULL,
    email          VARCHAR(255) NOT NULL,
    dob            DATE         NOT NULL,
    title          VARCHAR(50)  NOT NULL,
    office_room_id VARCHAR(50)  NOT NULL REFERENCES room(id),
    department     VARCHAR(100) NOT NULL
);

-- Student: extends Person (jmbag = id)
CREATE TABLE IF NOT EXISTS student (
    jmbag           CHAR(10)     PRIMARY KEY,
    oib             CHAR(11)     NOT NULL UNIQUE,
    first_name      VARCHAR(50)  NOT NULL,
    last_name       VARCHAR(50)  NOT NULL,
    email           VARCHAR(255) NOT NULL,
    dob             DATE         NOT NULL,
    year_of_study   INT          NOT NULL,
    study_programme VARCHAR(50)  NOT NULL
);

-- Session: one class meeting (id); references course, professor and room
CREATE TABLE IF NOT EXISTS session (
    id            VARCHAR(50) PRIMARY KEY,
    type          VARCHAR(30) NOT NULL,
    course_code   VARCHAR(20) NOT NULL REFERENCES course(code),
    professor_oib CHAR(11)    NOT NULL REFERENCES professor(oib),
    date_time     TIMESTAMP   NOT NULL,
    room_id       VARCHAR(50) NOT NULL REFERENCES room(id)
);

-- Attendance: Relation<Student, Session> + status + recorded time
CREATE TABLE IF NOT EXISTS attendance (
    id            VARCHAR(50) PRIMARY KEY,
    student_jmbag CHAR(10)    NOT NULL REFERENCES student(jmbag),
    session_id    VARCHAR(50) NOT NULL REFERENCES session(id),
    status        VARCHAR(20) NOT NULL,
    recorded_at   TIMESTAMP   NOT NULL
);
