package com.uni.app.database;

import com.uni.app.entities.Building;
import com.uni.app.exceptions.DatabaseException;
import com.uni.app.exceptions.ValidationException;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Provides persistence for {@link Building} entities backed by the
 * {@code building} table.
 *
 * <p>Inherits the read and delete operations from {@link AbstractRepository}
 * and adds the entity-specific {@link #save(Building)} and
 * {@link #update(Building)} writes.</p>
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @since 1.0
 */
public final class BuildingRepository extends AbstractRepository<Building> {
    private final String sqlInsert;
    private final String sqlUpdate;

    /**
     * Creates a repository bound to the building table.
     *
     * @param db the database manager used to run statements
     * @throws ValidationException if {@code db} is {@code null}, or the table or
     *                             id column does not exist
     * @throws DatabaseException   if checking the schema fails
     */
    public BuildingRepository(DatabaseManager db) throws DatabaseException {
        super(db, BuildingRepository::mapRow, "building", "id");
        sqlInsert = """
                INSERT INTO %s (id, name, address)
                VALUES (?, ?, ?);
                """.formatted(getTable());
        sqlUpdate = """
                UPDATE %s SET name = ?, address = ?
                WHERE %s = ?;
                """.formatted(getTable(), getIdColumn());
    }

    /**
     * Inserts a new building into the database.
     *
     * @param building the building to insert
     * @return the number of rows inserted
     * @throws ValidationException if {@code building} is {@code null}
     * @throws DatabaseException   if the statement fails
     */
    public int save(Building building) throws DatabaseException {
        if (building == null) {
            throw new ValidationException("BuildingRepository 'building' must not be null");
        }
        return execute(
                sqlInsert,
                building.getId(),
                building.getName(),
                building.getAddress()
        );
    }

    /**
     * Updates the stored building selected by the given building's id.
     *
     * @param building the building carrying the new values; its id selects the row
     * @return the number of rows updated
     * @throws ValidationException if {@code building} is {@code null}
     * @throws DatabaseException   if the statement fails
     */
    public int update(Building building) throws DatabaseException {
        if (building == null) {
            throw new ValidationException("BuildingRepository 'building' must not be null");
        }
        return execute(
                sqlUpdate,
                building.getName(),
                building.getAddress(),
                building.getId()
        );
    }

    private static Building mapRow(ResultSet rs) throws SQLException {
        return new Building(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("address")
        );
    }
}
