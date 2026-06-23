package com.uni.app.exceptions;

import java.io.Serial;

/**
 * Unchecked exception thrown when a requested entity cannot be found.
 *
 * <p>Typically thrown when a lookup by identifier returns no result and the
 * caller requires the entity to exist.</p>
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @since 1.0
 */
public class EntityNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new exception with the given detail message.
     *
     * @param message description of which entity was not found
     */
    public EntityNotFoundException(String message) {
        super(message);
    }
}
