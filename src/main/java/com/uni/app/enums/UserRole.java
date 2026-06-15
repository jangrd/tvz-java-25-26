package com.uni.app.enums;

/**
 * Roles that an application user may hold, controlling which screens and
 * operations are accessible after login.
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @since 1.0
 */
public enum UserRole {
    /** Administrator with full access. */
    ADMIN("Administrator"),
    /** Professor who manages sessions and attendance. */
    PROFESSOR("Profesor"),
    /** Student with read-only access to their own records. */
    STUDENT("Student");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the human-readable name of this role.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }
}
