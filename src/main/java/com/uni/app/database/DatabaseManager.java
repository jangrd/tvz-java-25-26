package com.uni.app.database;

import com.uni.app.exceptions.DatabaseException;
import com.uni.app.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages a single held connection to the embedded H2 database and runs SQL
 * statements against it.
 *
 * <p>The configuration (URL and credentials) is fixed at construction; call
 * {@link #connect()} to open the connection and {@link #close()} to release it.
 * Every {@link SQLException} is logged with Logback and rethrown as a
 * {@link DatabaseException}.</p>
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @since 1.0
 */
public final class DatabaseManager {
    private static final Logger log = LoggerFactory.getLogger(DatabaseManager.class);

    private final String url;
    private final String username;
    private final String password;

    private Connection connection;

    /**
     * Creates a database manager with the given configuration.
     *
     * <p>Does not open the connection; call {@link #connect()} for that.</p>
     *
     * @param url      the JDBC URL of the database
     * @param username the database user name
     * @param password the database password
     * @throws ValidationException if any argument is {@code null}
     */
    public DatabaseManager(String url, String username, String password) {
        if (url == null) {
            throw new ValidationException("DatabaseManager 'url' must not be null");
        }
        if (username == null) {
            throw new ValidationException("DatabaseManager 'username' must not be null");
        }
        if (password == null) {
            throw new ValidationException("DatabaseManager 'password' must not be null");
        }

        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * Opens the connection to the database using the configured URL and credentials.
     *
     * @throws DatabaseException if the connection cannot be opened
     */
    public void connect() throws DatabaseException {
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            log.error("Database connection failed", e);
            throw new DatabaseException("Database connection failed", e);
        }
    }

    /**
     * Executes an update statement (INSERT, UPDATE, DELETE or DDL).
     *
     * @param query  the SQL statement, optionally with {@code ?} placeholders
     * @param params the values bound to the placeholders, in order
     * @return the number of rows affected
     * @throws ValidationException if {@code query} is {@code null}
     * @throws DatabaseException   if the statement fails
     */
    public int execute(String query, Object... params) throws DatabaseException {
        if (query == null) {
            throw new ValidationException("DatabaseManager 'query' must not be null");
        }
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            log.error("DatabaseManager query execution failed", e);
            throw new DatabaseException("DatabaseManager query execution failed", e);
        }
    }

    /**
     * Runs a query and maps each returned row with the given mapper.
     *
     * @param query  the SQL query, optionally with {@code ?} placeholders
     * @param mapper maps a single result-set row to an object
     * @param params the values bound to the placeholders, in order
     * @param <T>    the type produced for each row
     * @return a list of mapped objects, empty if the query returns no rows
     * @throws ValidationException if {@code query} or {@code mapper} is {@code null}
     * @throws DatabaseException   if the query fails
     */
    public <T> List<T> get(String query, RowMapper<T> mapper, Object... params) throws DatabaseException {
        if (query == null) {
            throw new ValidationException("DatabaseManager 'query' must not be null");
        }
        if (mapper == null) {
            throw new ValidationException("DatabaseManager 'mapper' must not be null");
        }
        List<T> results = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapper.map(rs));
                }
            }
        } catch (SQLException e) {
            log.error("DatabaseManager query failed", e);
            throw new DatabaseException("DatabaseManager query failed", e);
        }
        return results;
    }

    /**
     * Closes the database connection if one is open; does nothing otherwise.
     *
     * @throws DatabaseException if closing the connection fails
     */
    public void close() throws DatabaseException {
        if (connection == null) {
            log.debug("DatabaseManager.close() called on null connection");
            return;
        }
        try {
            connection.close();
        } catch (SQLException e) {
            log.error("Database close failed", e);
            throw new DatabaseException("Database close failed", e);
        }
    }
}
