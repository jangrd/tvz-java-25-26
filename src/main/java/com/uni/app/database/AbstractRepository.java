package com.uni.app.database;

import com.uni.app.entities.Identifiable;
import com.uni.app.exceptions.DatabaseException;
import com.uni.app.exceptions.ValidationException;

import java.util.List;
import java.util.Optional;

/**
 * Base class for entity repositories backed by {@link DatabaseManager}.
 *
 * <p>Provides generic read and delete operations ({@code findAll},
 * {@code findById}, {@code deleteById}) using the configured table name, id
 * column and {@link RowMapper}. Subclasses add the entity-specific writes
 * (insert/update).</p>
 *
 * @param <T> the entity type, which must be {@link Identifiable}
 * @author Jan Grdanjski
 * @version 1.0
 * @since 1.0
 */
public abstract class AbstractRepository<T extends Identifiable> {
    private final DatabaseManager db;
    private final RowMapper<T> mapper;
    private final String table;
    private final String idColumn;

    /**
     * Creates a repository bound to one table.
     *
     * @param db       the database manager used to run statements
     * @param mapper   maps a result-set row to an entity
     * @param table    the table name
     * @param idColumn the name of the identity column
     * @throws ValidationException if any argument is {@code null}, or the table
     *                             with the given id column does not exist
     * @throws DatabaseException   if checking the schema fails
     */
    public AbstractRepository(DatabaseManager db, RowMapper<T> mapper, String table, String idColumn) throws DatabaseException {
        if (db == null) {
            throw new ValidationException("AbstractRepository 'db' must not be null");
        }
        if (mapper == null) {
            throw new ValidationException("AbstractRepository 'mapper' must not be null");
        }
        if (table == null) {
            throw new ValidationException("AbstractRepository 'table' must not be null");
        }
        if (idColumn == null) {
            throw new ValidationException("AbstractRepository 'idColumn' must not be null");
        }
        this.db = db;
        if (!this.db.tableWithColumnExist(table, idColumn)) {
            throw new ValidationException("AbstractRepository table " + table + " with id column " + idColumn + " does not exist");
        }
        this.mapper = mapper;
        this.table = table;
        this.idColumn = idColumn;
    }

    /**
     * Returns all entities in the table.
     *
     * @return a list of all entities, empty if the table has no rows
     * @throws DatabaseException if the query fails
     */
    public List<T> findAll() throws DatabaseException {
        return db.get("SELECT * FROM " + table, mapper);
    }

    /**
     * Finds the entity with the given id.
     *
     * @param id the id to look for
     * @return an {@link Optional} with the entity, or empty if none has that id
     * @throws ValidationException if {@code id} is {@code null}
     * @throws DatabaseException   if the query fails
     */
    public Optional<T> findById(String id) throws DatabaseException {
        if (id == null) {
            throw new ValidationException("AbstractRepository 'id' must not be null");
        }
        List<T> results = db.get(
                "SELECT * FROM " + table + " WHERE " + idColumn + " = ?",
                mapper,
                id
        );
        return results.stream().findFirst();
    }

    /**
     * Executes a data-modifying statement (INSERT, UPDATE or DELETE) for subclasses.
     *
     * @param query  the SQL statement, optionally with {@code ?} placeholders
     * @param params the values bound to the placeholders, in order
     * @return the number of rows affected
     * @throws DatabaseException if the statement fails
     */
    protected final int execute(String query, Object... params) throws DatabaseException {
        return db.execute(query, params);
    }

    /**
     * Deletes the entity with the given id, if present.
     *
     * @param id the id of the entity to delete
     * @return number of affected rows
     * @throws ValidationException if {@code id} is {@code null}
     * @throws DatabaseException if the statement fails
     */
    public int deleteById(String id) throws DatabaseException {
        if (id == null) {
            throw new ValidationException("AbstractRepository 'id' must not be null");
        }
        return execute(
                "DELETE FROM " + table + " WHERE " + idColumn + " = ?",
                id
        );
    }

    /**
     * Returns the bound table name.
     *
     * @return the table name
     */
    protected final String getTable() {
        return table;
    }

    /**
     * Returns the bound id column name.
     *
     * @return the id column name
     */
    protected final String getIdColumn() {
        return idColumn;
    }
}
