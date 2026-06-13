package com.uni.app.database;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps a single row of a {@link ResultSet} to an object of type {@code T}.
 *
 * <p>Used by {@link DatabaseManager} to turn query results into objects. An
 * implementation reads the current row and must not advance the result set.</p>
 *
 * @param <T> the type produced from a row
 * @author Jan Grdanjski
 * @version 1.0
 * @since 1.0
 */
@FunctionalInterface
public interface RowMapper<T> {
    /**
     * Maps the current row of the result set to an object.
     *
     * @param rs the result set positioned at the row to map
     * @return the object created from the current row
     * @throws SQLException if reading from the result set fails
     */
    T mapRow(ResultSet rs) throws SQLException;
}
