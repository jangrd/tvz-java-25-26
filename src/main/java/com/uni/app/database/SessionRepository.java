package com.uni.app.database;

import com.uni.app.entities.Building;
import com.uni.app.entities.Course;
import com.uni.app.entities.Professor;
import com.uni.app.entities.Room;
import com.uni.app.entities.Session;
import com.uni.app.enums.RoomType;
import com.uni.app.enums.SessionType;
import com.uni.app.exceptions.DatabaseException;
import com.uni.app.exceptions.ValidationException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Provides persistence for {@link Session} entities backed by the
 * {@code session} table.
 *
 * <p>Adds the entity-specific {@link #save(Session)} and
 * {@link #update(Session)} writes, and overrides the reads to resolve the
 * session's course, professor and room. Because both the professor's office and
 * the session's own location are {@code room} rows (each with a {@code building}),
 * the read joins {@code room} and {@code building} twice; both clashing
 * occurrences are aliased symmetrically ({@code prof_*} for the professor's
 * office, {@code sess_*} for the session's location).</p>
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @since 1.0
 */
public final class SessionRepository extends AbstractRepository<Session> {
    private static final String SELECT_JOIN = """
            SELECT s.session_id, s.session_type, s.session_date_time,
                   c.course_code, c.course_name, c.course_ects, c.course_semester,
                   p.professor_oib, p.professor_first_name, p.professor_last_name, p.professor_email,
                   p.professor_dob, p.professor_title, p.professor_department,
                   pr.room_id AS prof_room_id, pr.room_type AS prof_room_type, pr.room_capacity AS prof_room_capacity,
                   pb.building_id AS prof_building_id, pb.building_name AS prof_building_name, pb.building_address AS prof_building_address,
                   sr.room_id AS sess_room_id, sr.room_type AS sess_room_type, sr.room_capacity AS sess_room_capacity,
                   sb.building_id AS sess_building_id, sb.building_name AS sess_building_name, sb.building_address AS sess_building_address
            FROM %s AS s
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
     * Creates a repository bound to the session table.
     *
     * @param db the database manager used to run statements
     * @throws ValidationException if {@code db} is {@code null}, or the table or
     *                             id column does not exist
     * @throws DatabaseException   if checking the schema fails
     */
    public SessionRepository(DatabaseManager db) throws DatabaseException {
        super(db, SessionRepository::mapRow, "session", "session_id");
        sqlInsert = """
                INSERT INTO %s
                (session_id, session_type, session_course_code, session_professor_oib,
                 session_date_time, session_room_id)
                VALUES
                (?, ?, ?, ?, ?, ?);
                """.formatted(getTable());
        sqlUpdate = """
                UPDATE %s SET
                session_type = ?, session_course_code = ?, session_professor_oib = ?,
                session_date_time = ?, session_room_id = ?
                WHERE %s = ?;
                """.formatted(getTable(), getIdColumn());
        sqlSelectOne = (SELECT_JOIN + " WHERE %s = ?;").formatted(getTable(), getIdColumn());
        sqlSelectAll = (SELECT_JOIN + ";").formatted(getTable());
    }

    /**
     * Inserts a new session into the database.
     *
     * @param session the session to insert
     * @return the number of rows inserted
     * @throws ValidationException if {@code session} is {@code null}
     * @throws DatabaseException   if the statement fails
     */
    public int save(Session session) throws DatabaseException {
        if (session == null) {
            throw new ValidationException("SessionRepository 'session' must not be null");
        }
        return execute(
                sqlInsert,
                session.getId(),
                session.getType().name(),
                session.getCourse().getId(),
                session.getProfessor().getId(),
                session.getDateTime(),
                session.getRoom().getId()
        );
    }

    /**
     * Updates the stored session selected by the given session's id.
     *
     * @param session the session carrying the new values; its id selects the row
     * @return the number of rows updated
     * @throws ValidationException if {@code session} is {@code null}
     * @throws DatabaseException   if the statement fails
     */
    public int update(Session session) throws DatabaseException {
        if (session == null) {
            throw new ValidationException("SessionRepository 'session' must not be null");
        }
        return execute(
                sqlUpdate,
                session.getType().name(),
                session.getCourse().getId(),
                session.getProfessor().getId(),
                session.getDateTime(),
                session.getRoom().getId(),
                session.getId()
        );
    }

    @Override
    public Optional<Session> findById(String id) throws DatabaseException {
        if (id == null) {
            throw new ValidationException("SessionRepository 'id' must not be null");
        }
        return query(sqlSelectOne, id).stream().findFirst();
    }

    @Override
    public List<Session> findAll() throws DatabaseException {
        return query(sqlSelectAll);
    }

    private static Session mapRow(ResultSet rs) throws SQLException {
        Course course = new Course(
                rs.getString("course_code"),
                rs.getString("course_name"),
                rs.getInt("course_ects"),
                rs.getInt("course_semester")
        );
        Professor professor = mapProfessor(rs);
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
                professor,
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
