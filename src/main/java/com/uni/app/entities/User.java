package com.uni.app.entities;

import com.uni.app.enums.UserRole;
import com.uni.app.exceptions.ValidationException;

import java.io.Serial;
import java.io.Serializable;

/**
 * Immutable record representing an application user loaded from the credentials file.
 *
 * <p>Passwords are stored as SHA-256 hex digests only; plain-text passwords are
 * never held in memory.</p>
 *
 * @param username     the unique login name
 * @param passwordHash the SHA-256 hex digest of the user's password
 * @param role         the role that determines what the user may do in the application
 * @author Jan Grdanjski
 * @version 1.0
 * @since 1.0
 */
public record User(String username, String passwordHash, UserRole role) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Validates that no component is {@code null}.
     *
     * @throws ValidationException if any component is {@code null}
     */
    public User {
        if (username == null) {
            throw new ValidationException("User 'username' must not be null");
        }
        if (passwordHash == null) {
            throw new ValidationException("User 'passwordHash' must not be null");
        }
        if (role == null) {
            throw new ValidationException("User 'role' must not be null");
        }
    }
}
