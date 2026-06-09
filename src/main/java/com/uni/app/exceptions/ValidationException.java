package com.uni.app.exceptions;

import java.io.Serial;

/**
 * Thrown when supplied data fails validation, such as an invalid OIB or JMBAG
 * or an out-of-range value.
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @since 1.0
 */
public class ValidationException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception with a message describing the validation failure.
     *
     * @param message the detail message
     */
    public ValidationException(String message) {
        super(message);
    }
}
