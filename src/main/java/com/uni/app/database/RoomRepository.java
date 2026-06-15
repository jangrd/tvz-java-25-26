package com.uni.app.database;

import com.uni.app.entities.Building;
import com.uni.app.entities.Room;
import com.uni.app.enums.RoomType;
import com.uni.app.exceptions.DatabaseException;
import com.uni.app.exceptions.ValidationException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Provides persistence for {@link Room} entities backed by the {@code room}
 * table.
 *
 * <p>Adds the entity-specific {@link #save(Room)} and {@link #update(Room)}
 * writes, and overrides the reads to resolve the room's building through a join
 * over {@code building}, so each {@link Room} is returned with a fully built
 * {@link Building}.</p>
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @since 1.0
 */
public final class RoomRepository extends AbstractRepository<Room> {
    private final String sqlInsert;
    private final String sqlUpdate;
    private final String sqlSelectOne;
    private final String sqlSelectAll;

    /**
     * Creates a repository bound to the room table.
     *
     * @param db the database manager used to run statements
     * @throws ValidationException if {@code db} is {@code null}, or the table or
     *                             id column does not exist
     * @throws DatabaseException   if checking the schema fails
     */
    public RoomRepository(DatabaseManager db) throws DatabaseException {
        super(db, RoomRepository::mapRow, "room", "room_id");
        sqlInsert = """
                INSERT INTO %s (room_id, room_type, room_capacity, room_building_id)
                VALUES (?, ?, ?, ?);
                """.formatted(getTable());
        sqlUpdate = """
                UPDATE %s SET room_type = ?, room_capacity = ?, room_building_id = ?
                WHERE %s = ?;
                """.formatted(getTable(), getIdColumn());
        sqlSelectAll = """
                SELECT room_id, room_type, room_capacity,
                       building_id, building_name, building_address
                FROM %s
                JOIN building ON room_building_id = building_id;
                """.formatted(getTable());
        sqlSelectOne = """
                SELECT room_id, room_type, room_capacity,
                       building_id, building_name, building_address
                FROM %s
                JOIN building ON room_building_id = building_id
                WHERE %s = ?;
                """.formatted(getTable(), getIdColumn());
    }

    /**
     * Inserts a new room into the database.
     *
     * @param room the room to insert
     * @return the number of rows inserted
     * @throws ValidationException if {@code room} is {@code null}
     * @throws DatabaseException   if the statement fails
     */
    public int save(Room room) throws DatabaseException {
        if (room == null) {
            throw new ValidationException("RoomRepository 'room' must not be null");
        }
        return execute(
                sqlInsert,
                room.getId(),
                room.getType().name(),
                room.getCapacity(),
                room.getBuilding().getId()
        );
    }

    /**
     * Updates the stored room selected by the given room's id.
     *
     * @param room the room carrying the new values; its id selects the row
     * @return the number of rows updated
     * @throws ValidationException if {@code room} is {@code null}
     * @throws DatabaseException   if the statement fails
     */
    public int update(Room room) throws DatabaseException {
        if (room == null) {
            throw new ValidationException("RoomRepository 'room' must not be null");
        }
        return execute(
                sqlUpdate,
                room.getType().name(),
                room.getCapacity(),
                room.getBuilding().getId(),
                room.getId()
        );
    }

    @Override
    public Optional<Room> findById(String id) throws DatabaseException {
        if (id == null) {
            throw new ValidationException("RoomRepository 'id' must not be null");
        }
        return query(sqlSelectOne, id).stream().findFirst();
    }

    @Override
    public List<Room> findAll() throws DatabaseException {
        return query(sqlSelectAll);
    }

    private static Room mapRow(ResultSet rs) throws SQLException {
        Building building = new Building(
                rs.getString("building_id"),
                rs.getString("building_name"),
                rs.getString("building_address")
        );
        return new Room(
                rs.getString("room_id"),
                RoomType.valueOf(rs.getString("room_type")),
                rs.getInt("room_capacity"),
                building
        );
    }
}
