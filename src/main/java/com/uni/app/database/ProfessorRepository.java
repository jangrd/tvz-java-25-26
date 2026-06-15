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
        super(db, ProfessorRepository::mapRow, "professor", "professor_oib");
        sqlInsert = """
                INSERT INTO %s
                (professor_oib, professor_first_name, professor_last_name, professor_email,
                 professor_dob, professor_title, professor_office_room_id, professor_department)
                VALUES
                (?, ?, ?, ?, ?, ?, ?, ?);
                """.formatted(getTable());
        sqlSelectOne = """
                SELECT professor_oib, professor_first_name, professor_last_name, professor_email,
                       professor_dob, professor_title, professor_department,
                       room_id, room_type, room_capacity,
                       building_id, building_name, building_address
                FROM %s
                JOIN room ON professor_office_room_id = room_id
                JOIN building ON room_building_id = building_id
                WHERE %s = ?;
                """.formatted(getTable(), getIdColumn());
        sqlSelectAll = """
                SELECT professor_oib, professor_first_name, professor_last_name, professor_email,
                       professor_dob, professor_title, professor_department,
                       room_id, room_type, room_capacity,
                       building_id, building_name, building_address
                FROM %s
                JOIN room ON professor_office_room_id = room_id
                JOIN building ON room_building_id = building_id;
                """.formatted(getTable());
        sqlUpdate = """
                UPDATE %s SET
                professor_first_name = ?, professor_last_name = ?, professor_email = ?,
                professor_dob = ?, professor_title = ?, professor_office_room_id = ?, professor_department = ?
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
        return query(sqlSelectOne, id).stream().findFirst();
    }

    @Override
    public List<Professor> findAll() throws DatabaseException {
        return query(sqlSelectAll);
    }

    private static Professor mapRow(ResultSet rs) throws SQLException {
        Building building = new Building(
                rs.getString("building_id"),
                rs.getString("building_name"),
                rs.getString("building_address")
        );
        Room officeRoom = new Room(
                rs.getString("room_id"),
                RoomType.valueOf(rs.getString("room_type")),
                rs.getInt("room_capacity"),
                building
        );
        return new Professor(
                rs.getString("professor_oib"),
                rs.getString("professor_first_name"),
                rs.getString("professor_last_name"),
                rs.getString("professor_email"),
                rs.getObject("professor_dob", LocalDate.class),
                rs.getString("professor_title"),
                officeRoom,
                rs.getString("professor_department")
        );
    }
}
