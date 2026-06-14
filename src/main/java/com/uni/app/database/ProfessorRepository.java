package com.uni.app.database;

import com.uni.app.entities.Building;
import com.uni.app.entities.Professor;
import com.uni.app.entities.Room;
import com.uni.app.enums.RoomType;
import com.uni.app.exceptions.DatabaseException;
import com.uni.app.exceptions.ValidationException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Provides persistence for {@link Professor} entities backed by the
 * {@code professor} table.
 *
 * <p>Adds the entity-specific {@link #save(Professor)} and
 * {@link #update(Professor)} writes, and overrides the reads to resolve the
 * professor's office through a join over {@code room} and {@code building}, so
 * each {@link Professor} is returned with a fully built {@link Room}.</p>
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @since 1.0
 */
public final class ProfessorRepository extends AbstractRepository<Professor> {
    private final String sqlInsert;
    private final String sqlUpdate;
    private final String sqlSelectOne;
    private final String sqlSelectAll;

    /**
     * Creates a repository bound to the professor table.
     *
     * @param db the database manager used to run statements
     * @throws ValidationException if {@code db} is {@code null}, or the table or
     *                             id column does not exist
     * @throws DatabaseException   if checking the schema fails
     */
    public ProfessorRepository(DatabaseManager db) throws DatabaseException {
        super(db, ProfessorRepository::mapRow, "professor", "oib");
        sqlInsert = """
                INSERT INTO %s
                (oib, first_name, last_name, email, dob, title, office_room_id, department)
                VALUES
                (?, ?, ?, ?, ?, ?, ?, ?);
                """.formatted(getTable());
        sqlSelectOne = """
                SELECT p.oib, p.first_name, p.last_name, p.email, p.dob, p.title, p.department,
                       r.id AS r_id, r.type AS r_type, r.capacity AS r_capacity,
                       b.id AS b_id, b.name AS b_name, b.address AS b_address
                FROM %s AS p
                JOIN room AS r ON p.office_room_id = r.id
                JOIN building AS b ON r.building_id = b.id
                WHERE %s = ?;
                """.formatted(getTable(), getIdColumn());
        sqlSelectAll = """
                SELECT p.oib, p.first_name, p.last_name, p.email, p.dob, p.title, p.department,
                       r.id AS r_id, r.type AS r_type, r.capacity AS r_capacity,
                       b.id AS b_id, b.name AS b_name, b.address AS b_address
                FROM %s AS p
                JOIN room AS r ON p.office_room_id = r.id
                JOIN building AS b ON r.building_id = b.id;
                """.formatted(getTable());
        sqlUpdate = """
                UPDATE %s SET
                oib = ?, first_name = ?, last_name = ?, email = ?,
                dob = ?, title = ?, office_room_id = ?, department = ?
                WHERE %s = ?;
                """.formatted(getTable(), getIdColumn());
    }

    /**
     * Inserts a new professor into the database.
     *
     * @param professor the professor to insert
     * @return the number of rows inserted
     * @throws ValidationException if {@code professor} is {@code null}
     * @throws DatabaseException   if the statement fails
     */
    public int save(Professor professor) throws DatabaseException {
        if (professor == null) {
            throw new ValidationException("ProfessorRepository 'professor' must not be null");
        }
        return execute(
                sqlInsert,
                professor.getOib(),
                professor.getFirstName(),
                professor.getLastName(),
                professor.getEmail(),
                professor.getDob(),
                professor.getTitle(),
                professor.getOffice().getId(),
                professor.getDepartment()
        );
    }

    /**
     * Updates the stored professor selected by the given professor's OIB.
     *
     * @param professor the professor carrying the new values; its OIB selects the row
     * @return the number of rows updated
     * @throws ValidationException if {@code professor} is {@code null}
     * @throws DatabaseException   if the statement fails
     */
    public int update(Professor professor) throws DatabaseException {
        if (professor == null) {
            throw new ValidationException("ProfessorRepository 'professor' must not be null");
        }
        return execute(
                sqlUpdate,
                professor.getFirstName(),
                professor.getLastName(),
                professor.getEmail(),
                professor.getDob(),
                professor.getTitle(),
                professor.getOffice().getId(),
                professor.getDepartment(),
                professor.getOib()
        );
    }

    @Override
    public Optional<Professor> findById(String id) throws DatabaseException {
        if (id == null) {
            throw new ValidationException("ProfessorRepository 'id' must not be null");
        }
        List<Professor> results = query(sqlSelectOne, id);
        return results.stream().findFirst();
    }

    @Override
    public List<Professor> findAll() throws DatabaseException {
        return query(sqlSelectAll);
    }

    private static Professor mapRow(ResultSet rs) throws SQLException {
        Building building = new Building(
                rs.getString("b_id"),
                rs.getString("b_name"),
                rs.getString("b_address")
        );
        Room officeRoom = new Room(
                rs.getString("r_id"),
                RoomType.valueOf(rs.getString("r_type")),
                rs.getInt("r_capacity"),
                building
        );
        return new Professor(
                rs.getString("oib"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getObject("dob", LocalDate.class),
                rs.getString("title"),
                officeRoom,
                rs.getString("department")
        );
    }
}
