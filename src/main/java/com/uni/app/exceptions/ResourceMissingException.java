package com.uni.app.exceptions;

import java.io.Serial;

/**
 * Checked exception thrown when a required file resource cannot be accessed.
 *
 * <p>Wraps the underlying {@link java.io.IOException} so callers receive a
 * single, application-level exception type for all file I/O failures.</p>
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @since 1.0
 */
public class ResourceMissingException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new exception with a detail message and the cause.
     *
     * @param message description of the failed operation
     * @param cause   the underlying I/O exception
     */
    public ResourceMissingException(String message, Throwable cause) {
        super(message, cause);
    }
}
