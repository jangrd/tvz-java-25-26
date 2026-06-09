package com.uni.app.exceptions;

import java.io.Serial;

/**
 * Thrown when database calls fail.
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @since 1.0
 */
public class DatabaseException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception with a message describing the database fault.
     *
     * @param message the detail message
     * @param cause what triggered the exception
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
