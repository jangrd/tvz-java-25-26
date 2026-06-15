package com.uni.app.database;

import com.uni.app.entities.Attendance;
import com.uni.app.entities.Building;
import com.uni.app.entities.Course;
import com.uni.app.entities.Professor;
import com.uni.app.entities.Relation;
import com.uni.app.entities.Room;
import com.uni.app.entities.Session;
import com.uni.app.entities.Student;
import com.uni.app.enums.AttendanceStatus;
import com.uni.app.enums.RoomType;
import com.uni.app.enums.SessionType;
import com.uni.app.enums.StudyProgramme;
import com.uni.app.exceptions.DatabaseException;
import com.uni.app.exceptions.ValidationException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Provides persistence for {@link Attendance} entities backed by the
 * {@code attendance} table.
 *
 * <p>Adds the entity-specific {@link #save(Attendance)} and
 * {@link #update(Attendance)} writes, and overrides the reads to resolve the
 * whole {@link Relation} of {@link Student} and {@link Session} in a single
 * join. The {@code room} and {@code building} tables are joined twice (the
 * professor's office and the session's location); those clashing occurrences
 * are aliased symmetrically ({@code prof_*} and {@code sess_*}).</p>
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @since 1.0
 */
public final class AttendanceRepository extends AbstractRepository<Attendance> {
    private static final String SELECT_JOIN = """
            SELECT a.attendance_id, a.attendance_status, a.attendance_recorded_at,
                   st.student_jmbag, st.student_oib, st.student_first_name, st.student_last_name,
                   st.student_email, st.student_dob, st.student_year_of_study, st.student_study_programme,
                   s.session_id, s.session_type, s.session_date_time,
                   c.course_code, c.course_name, c.course_ects, c.course_semester,
                   p.professor_oib, p.professor_first_name, p.professor_last_name, p.professor_email,
                   p.professor_dob, p.professor_title, p.professor_department,
                   pr.room_id AS prof_room_id, pr.room_type AS prof_room_type, pr.room_capacity AS prof_room_capacity,
                   pb.building_id AS prof_building_id, pb.building_name AS prof_building_name, pb.building_address AS prof_building_address,
                   sr.room_id AS sess_room_id, sr.room_type AS sess_room_type, sr.room_capacity AS sess_room_capacity,
                   sb.building_id AS sess_building_id, sb.building_name AS sess_building_name, sb.building_address AS sess_building_address
            FROM %s AS a
            JOIN student AS st ON a.attendance_student_jmbag = st.student_jmbag
            JOIN session AS s ON a.attendance_session_id = s.session_id
            JOIN course AS c ON s.session_course_code = c.course_code
            JOIN professor AS p ON s.session_professor_oib = p.professor_oib
            JOIN room AS pr ON p.professor_office_room_id = pr.room_id
            JOIN building AS pb ON pr.room_building_id = pb.building_id
            JOIN room AS sr ON s.session_room_id = sr.room_id
            JOIN building AS sb ON sr.room_building_id = sb.building_id""";

    private final String sqlInsert;
    private final String sqlUpdate;
    private final String sqlSelectOne;
    private final String sqlSelectAll;

    /**
     * Creates a repository bound to the attendance table.
     *
     * @param db the database manager used to run statements
     * @throws ValidationException if {@code db} is {@code null}, or the table or
     *                             id column does not exist
     * @throws DatabaseException   if checking the schema fails
     */
    public AttendanceRepository(DatabaseManager db) throws DatabaseException {
        super(db, AttendanceRepository::mapRow, "attendance", "attendance_id");
        sqlInsert = """
                INSERT INTO %s
                (attendance_id, attendance_student_jmbag, attendance_session_id,
                 attendance_status, attendance_recorded_at)
                VALUES
                (?, ?, ?, ?, ?);
                """.formatted(getTable());
        sqlUpdate = """
                UPDATE %s SET
                attendance_student_jmbag = ?, attendance_session_id = ?,
                attendance_status = ?, attendance_recorded_at = ?
                WHERE %s = ?;
                """.formatted(getTable(), getIdColumn());
        sqlSelectOne = (SELECT_JOIN + " WHERE %s = ?;").formatted(getTable(), getIdColumn());
        sqlSelectAll = (SELECT_JOIN + ";").formatted(getTable());
    }

    /**
     * Inserts a new attendance record into the database.
     *
     * @param attendance the attendance to insert
     * @return the number of rows inserted
     * @throws ValidationException if {@code attendance} is {@code null}
     * @throws DatabaseException   if the statement fails
     */
    public int save(Attendance attendance) throws DatabaseException {
        if (attendance == null) {
            throw new ValidationException("AttendanceRepository 'attendance' must not be null");
        }
        return execute(
                sqlInsert,
                attendance.getId(),
                attendance.getRelation().getLeft().getId(),
                attendance.getRelation().getRight().getId(),
                attendance.getStatus().name(),
                attendance.getRecordedAt()
        );
    }

    /**
     * Updates the stored attendance selected by the given attendance's id.
     *
     * @param attendance the attendance carrying the new values; its id selects the row
     * @return the number of rows updated
     * @throws ValidationException if {@code attendance} is {@code null}
     * @throws DatabaseException   if the statement fails
     */
    public int update(Attendance attendance) throws DatabaseException {
        if (attendance == null) {
            throw new ValidationException("AttendanceRepository 'attendance' must not be null");
        }
        return execute(
                sqlUpdate,
                attendance.getRelation().getLeft().getId(),
                attendance.getRelation().getRight().getId(),
                attendance.getStatus().name(),
                attendance.getRecordedAt(),
                attendance.getId()
        );
    }

    @Override
    public Optional<Attendance> findById(String id) throws DatabaseException {
        if (id == null) {
            throw new ValidationException("AttendanceRepository 'id' must not be null");
        }
        return query(sqlSelectOne, id).stream().findFirst();
    }

    @Override
    public List<Attendance> findAll() throws DatabaseException {
        return query(sqlSelectAll);
    }

    private static Attendance mapRow(ResultSet rs) throws SQLException {
        Relation<Student, Session> relation = new Relation<>(mapStudent(rs), mapSession(rs));
        return new Attendance(
                rs.getString("attendance_id"),
                relation,
                AttendanceStatus.valueOf(rs.getString("attendance_status")),
                rs.getObject("attendance_recorded_at", LocalDateTime.class)
        );
    }

    private static Student mapStudent(ResultSet rs) throws SQLException {
        return new Student.Builder()
                .jmbag(rs.getString("student_jmbag"))
                .oib(rs.getString("student_oib"))
                .firstName(rs.getString("student_first_name"))
                .lastName(rs.getString("student_last_name"))
                .email(rs.getString("student_email"))
                .dob(rs.getObject("student_dob", LocalDate.class))
                .yearOfStudy(rs.getInt("student_year_of_study"))
                .studyProgramme(StudyProgramme.valueOf(rs.getString("student_study_programme")))
                .build();
    }

    private static Session mapSession(ResultSet rs) throws SQLException {
        Course course = new Course(
                rs.getString("course_code"),
                rs.getString("course_name"),
                rs.getInt("course_ects"),
                rs.getInt("course_semester")
        );
        Building sessionBuilding = new Building(
                rs.getString("sess_building_id"),
                rs.getString("sess_building_name"),
                rs.getString("sess_building_address")
        );
        Room sessionRoom = new Room(
                rs.getString("sess_room_id"),
                RoomType.valueOf(rs.getString("sess_room_type")),
                rs.getInt("sess_room_capacity"),
                sessionBuilding
        );
        return new Session(
                rs.getString("session_id"),
                SessionType.valueOf(rs.getString("session_type")),
                course,
                mapProfessor(rs),
                rs.getObject("session_date_time", LocalDateTime.class),
                sessionRoom
        );
    }

    private static Professor mapProfessor(ResultSet rs) throws SQLException {
        Building officeBuilding = new Building(
                rs.getString("prof_building_id"),
                rs.getString("prof_building_name"),
                rs.getString("prof_building_address")
        );
        Room office = new Room(
                rs.getString("prof_room_id"),
                RoomType.valueOf(rs.getString("prof_room_type")),
                rs.getInt("prof_room_capacity"),
                officeBuilding
        );
        return new Professor(
                rs.getString("professor_oib"),
                rs.getString("professor_first_name"),
                rs.getString("professor_last_name"),
                rs.getString("professor_email"),
                rs.getObject("professor_dob", LocalDate.class),
                rs.getString("professor_title"),
                office,
                rs.getString("professor_department")
        );
    }
}
