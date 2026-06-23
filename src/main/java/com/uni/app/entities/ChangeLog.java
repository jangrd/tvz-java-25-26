package com.uni.app.entities;

import com.uni.app.enums.UserRole;
import com.uni.app.exceptions.ValidationException;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Immutable record of a single field change made by an authenticated user.
 *
 * <p>Instances are serialized to {@code changes.dat} and displayed on the
 * change-history screen.</p>
 *
 * @param changedField the name of the attribute that was modified
 * @param oldValue     the value before the change
 * @param newValue     the value after the change
 * @param changedBy    the user who performed the change
 * @param roleAtChange the role the user held at the time of the change
 * @param changedAt    the date and time when the change was recorded
 * @author Jan Grdanjski
 * @version 1.0
 * @since 1.0
 */
public record ChangeLog(String changedField, String oldValue, String newValue,
                        User changedBy, UserRole roleAtChange, LocalDateTime changedAt) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Validates that no component is {@code null}.
     *
     * @throws ValidationException if any component is {@code null}
     */
    public ChangeLog {
        if (changedField == null) {
            throw new ValidationException("ChangeLog 'changedField' must not be null");
        }
        if (oldValue == null) {
            throw new ValidationException("ChangeLog 'oldValue' must not be null");
        }
        if (newValue == null) {
            throw new ValidationException("ChangeLog 'newValue' must not be null");
        }
        if (changedBy == null) {
            throw new ValidationException("ChangeLog 'changedBy' must not be null");
        }
        if (roleAtChange == null) {
            throw new ValidationException("ChangeLog 'roleAtChange' must not be null");
        }
        if (changedAt == null) {
            throw new ValidationException("ChangeLog 'changedAt' must not be null");
        }
    }
}
