package com.uni.app.enums;

/**
 * Enumerates the types of a teaching session.
 *
 * <p>Each constant carries a human-readable name intended for display in the
 * user interface, while the constant itself is used throughout the code.</p>
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @since 1.0
 */
public enum SessionType {
    /** Lecture session. */
    LECTURE("Predavanje"),
    /** Laboratory exercise session. */
    LAB("Vjezbe"),
    /** Seminar session. */
    SEMINAR("Seminar"),
    /** Exam session. */
    EXAM("Ispit");

    private final String displayName;

    SessionType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the human-readable name of this session type.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }
}
